package net.kyc.client.impl.module.movement;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.config.setting.EnumConfig;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.EventStage;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.TickEvent;
import net.kyc.client.impl.event.config.ConfigUpdateEvent;
import net.kyc.client.impl.event.entity.player.PlayerMoveEvent;
import net.kyc.client.impl.event.network.DisconnectEvent;
import net.kyc.client.impl.event.network.PacketEvent;
import net.kyc.client.init.Managers;
import net.kyc.client.init.Modules;
import net.kyc.client.util.math.MathUtil;
import net.kyc.client.util.player.MovementUtil;
import net.kyc.client.util.string.EnumFormatter;
import net.kyc.client.util.world.FakePlayerEntity;

/**
 * @author linus
 * @since 1.0
 */
public class SpeedModule extends ToggleModule {
    //
    Config<Speed> speedModeConfig = new EnumConfig<>("Mode", "Speed mode", Speed.STRAFE, Speed.values());
    Config<Boolean> vanillaStrafeConfig = new BooleanConfig("Strafe-Vanilla", "Applies strafe speeds to vanilla speed", false, () -> speedModeConfig.getValue() == Speed.VANILLA);
    Config<Float> speedConfig = new NumberConfig<>("Speed", "The speed for alternative modes", 0.1f, 4.0f, 10.0f);
    Config<Boolean> timerConfig = new BooleanConfig("UseTimer", "Uses timer to increase acceleration", false);
    Config<Boolean> strafeBoostConfig = new BooleanConfig("StrafeBoost", "Uses explosion velocity to boost Strafe", false);
    Config<Integer> boostTicksConfig = new NumberConfig<>("BoostTicks", "The number of ticks to boost strafe", 10, 20, 40, () -> strafeBoostConfig.getValue());
    Config<Boolean> speedWaterConfig = new BooleanConfig("SpeedInWater", "Applies speed even in water and lava", false);
    //
    private int strafe = 4;
    private boolean accel;
    private int strictTicks;
    private int boostTicks;
    //
    private double speed;
    private double boostSpeed;
    private double distance;
    //
    private boolean prevTimer;

    /**
     *
     */
    public SpeedModule() {
        super("Speed", "Move faster", ModuleCategory.MOVEMENT);
    }

    @Override
    public String getModuleData() {
        if (speedModeConfig.getValue() == Speed.GRIM_COLLIDE) {
            return "Grim";
        }
        return EnumFormatter.formatEnum(speedModeConfig.getValue());
    }

    @Override
    public void onEnable() {
        prevTimer = Modules.TIMER.isEnabled();
        if (timerConfig.getValue() && !prevTimer && isStrafe()) {
            Modules.TIMER.enable();
        }
    }

    @Override
    public void onDisable() {
        resetStrafe();
        if (Modules.TIMER.isEnabled()) {
            Modules.TIMER.resetTimer();
            if (!prevTimer) {
                Modules.TIMER.disable();
            }
        }
    }

    @Override
    public void onUpdate() {

    }

    @EventListener
    public void onDisconnect(DisconnectEvent event) {
        disable();
    }

    @EventListener
    public void onTick(TickEvent event) {
        if (event.getStage() == EventStage.PRE) {
            boostTicks++;
            if (boostTicks > boostTicksConfig.getValue()) {
                boostSpeed = 0.0;
            }
            double dx = mc.player.getX() - mc.player.prevX;
            double dz = mc.player.getZ() - mc.player.prevZ;
            distance = Math.sqrt(dx * dx + dz * dz);
            if (speedModeConfig.getValue() == Speed.GRIM_COLLIDE && MovementUtil.isInputtingMovement()) {
                int collisions = 0;
                for (Entity entity : mc.world.getEntities()) {
                    if (checkIsCollidingEntity(entity) && MathHelper.sqrt((float) mc.player.squaredDistanceTo(entity)) <= 1.5) {
                        collisions++;
                    }
                }
                if (collisions > 0) {
                    Vec3d velocity = mc.player.getVelocity();
                    // double COLLISION_DISTANCE = 1.5;
                    double factor = 0.08 * collisions;
                    Vec2f strafe = handleStrafeMotion((float) factor);
                    mc.player.setVelocity(velocity.x + strafe.x, velocity.y, velocity.z + strafe.y);
                }
            }
        }
    }

    @EventListener
    public void onPlayerMove(PlayerMoveEvent event) {
        if (mc.player != null && mc.world != null) {
            if (!MovementUtil.isInputtingMovement()
                    || Modules.FLIGHT.isEnabled()
                    || Modules.LONG_JUMP.isEnabled()
            //      || Modules.ELYTRA_FLY.isEnabled()
                    || mc.player.isRiding()
                    || mc.player.isFallFlying()
                    || mc.player.isHoldingOntoLadder()
                    || mc.player.fallDistance > 2.0f
                    || (mc.player.isInLava() || mc.player.isTouchingWater())
                    && !speedWaterConfig.getValue()) {
                resetStrafe();
                Modules.TIMER.setTimer(1.0f);
                return;
            }
            event.cancel();
            //
            double speedEffect = 1.0;
            double slowEffect = 1.0;
            if (mc.player.hasStatusEffect(StatusEffects.SPEED)) {
                double amplifier = mc.player.getStatusEffect(StatusEffects.SPEED).getAmplifier();
                speedEffect = 1 + (0.2 * (amplifier + 1));
            }
            if (mc.player.hasStatusEffect(StatusEffects.SLOWNESS)) {
                double amplifier = mc.player.getStatusEffect(StatusEffects.SLOWNESS).getAmplifier();
                slowEffect = 1 + (0.2 * (amplifier + 1));
            }
            final double base = 0.2873f * speedEffect / slowEffect;
            float jumpEffect = 0.0f;
            if (mc.player.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
                jumpEffect += (mc.player.getStatusEffect(StatusEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f;
            }
            // ~29 kmh
            if (speedModeConfig.getValue() == Speed.STRAFE || speedModeConfig.getValue() == Speed.STRAFE_B_HOP) {
                if (!Managers.ANTICHEAT.hasPassed(100)) {
                    return;
                }
                if (timerConfig.getValue()) {
                    Modules.TIMER.setTimer(1.0888f);
                }
                if (strafe == 1) {
                    speed = 1.35f * base - 0.01f;
                } else if (strafe == 2) {
                    if (mc.player.input.jumping || !mc.player.isOnGround()) {
                        return;
                    }
                    float jump = (speedModeConfig.getValue() == Speed.STRAFE_B_HOP ? 0.4000000059604645f : 0.3999999463558197f) + jumpEffect;
                    event.setY(jump);
                    Managers.MOVEMENT.setMotionY(jump);
                    speed *= speedModeConfig.getValue() == Speed.STRAFE_B_HOP ? 1.535 : (accel ? 1.6835 : 1.395);
                } else if (strafe == 3) {
                    double moveSpeed = 0.66 * (distance - base);
                    speed = distance - moveSpeed;
                    accel = !accel;
                } else {
                    if ((!mc.world.isSpaceEmpty(mc.player, mc.player.getBoundingBox().offset(0,
                            mc.player.getVelocity().getY(), 0)) || mc.player.verticalCollision) && strafe > 0) {
                        strafe = MovementUtil.isInputtingMovement() ? 1 : 0;
                    }
                    speed = distance - distance / 159.0;
                }
                speed = Math.max(speed, base);
                if (strafeBoostConfig.getValue()) {
                    speed += boostSpeed;
                }
                final Vec2f motion = handleStrafeMotion((float) speed);
                event.setX(motion.x);
                event.setZ(motion.y);
                strafe++;
            }
            // ~26-27 kmh
            else if (speedModeConfig.getValue() == Speed.STRAFE_STRICT) {
                if (!Managers.ANTICHEAT.hasPassed(100)) {
                    return;
                }
                if (strafe == 1) {
                    speed = 1.35f * base - 0.01f;
                } else if (strafe == 2) {
                    if (mc.player.input.jumping || !mc.player.isOnGround()) {
                        return;
                    }
                    float jump = 0.3999999463558197f + jumpEffect;
                    event.setY(jump);
                    Managers.MOVEMENT.setMotionY(jump);
                    speed *= 2.149;
                } else if (strafe == 3) {
                    double moveSpeed = 0.66 * (distance - base);
                    speed = distance - moveSpeed;
                } else {
                    if ((!mc.world.isSpaceEmpty(mc.player, mc.player.getBoundingBox().offset(0,
                            mc.player.getVelocity().getY(), 0)) || mc.player.verticalCollision) && strafe > 0) {
                        strafe = MovementUtil.isInputtingMovement() ? 1 : 0;
                    }
                    speed = distance - distance / 159.0;
                }
                strictTicks++;
                speed = Math.max(speed, base);
                //
                if (timerConfig.getValue()) {
                    Modules.TIMER.setTimer(1.0888f);
                }
                double baseMax = 0.465 * speedEffect / slowEffect;
                double baseMin = 0.44 * speedEffect / slowEffect;
                speed = Math.min(speed, strictTicks > 25 ? baseMax : baseMin);
                if (strafeBoostConfig.getValue()) {
                    speed += boostSpeed;
                }
                if (strictTicks > 50) {
                    strictTicks = 0;
                }
                final Vec2f motion = handleStrafeMotion((float) speed);
                event.setX(motion.x);
                event.setZ(motion.y);
                strafe++;
            } else if (speedModeConfig.getValue() == Speed.LOW_HOP) {
                if (!Managers.ANTICHEAT.hasPassed(100)) {
                    return;
                }
                if (timerConfig.getValue()) {
                    Modules.TIMER.setTimer(1.0888f);
                }
                if (MathUtil.round(mc.player.getY() - (double) (int) mc.player.getY(), 3) == MathUtil.round(0.4, 3)) {
                    Managers.MOVEMENT.setMotionY(0.31 + jumpEffect);
                    event.setY(0.31 + jumpEffect);
                } else if (MathUtil.round(mc.player.getY() - (double) (int) mc.player.getY(), 3) == MathUtil.round(0.71, 3)) {
                    Managers.MOVEMENT.setMotionY(0.04 + jumpEffect);
                    event.setY(0.04 + jumpEffect);
                } else if (MathUtil.round(mc.player.getY() - (double) (int) mc.player.getY(), 3) == MathUtil.round(0.75, 3)) {
                    Managers.MOVEMENT.setMotionY(-0.2 - jumpEffect);
                    event.setY(-0.2 - jumpEffect);
                } else if (MathUtil.round(mc.player.getY() - (double) (int) mc.player.getY(), 3) == MathUtil.round(0.55, 3)) {
                    Managers.MOVEMENT.setMotionY(-0.14 + jumpEffect);
                    event.setY(-0.14 + jumpEffect);
                } else {
                    if (MathUtil.round(mc.player.getY() - (double) (int) mc.player.getY(), 3) == MathUtil.round(0.41, 3)) {
                        Managers.MOVEMENT.setMotionY(-0.2 + jumpEffect);
                        event.setY(-0.2 + jumpEffect);
                    }
                }
                if (strafe == 1) {
                    speed = 1.35f * base - 0.01f;
                } else if (strafe == 2) {
                    double jump = (isBoxColliding() ? 0.2 : 0.3999) + jumpEffect;
                    Managers.MOVEMENT.setMotionY(jump);
                    event.setY(jump);
                    speed *= accel ? 1.5685 : 1.3445;
                } else if (strafe == 3) {
                    double moveSpeed = 0.66 * (distance - base);
                    speed = distance - moveSpeed;
                    accel = !accel;
                } else {
                    if (mc.player.isOnGround() && strafe > 0) {
                        strafe = MovementUtil.isInputtingMovement() ? 1 : 0;
                    }
                    speed = distance - distance / 159.0;
                }
                speed = Math.max(speed, base);
                Vec2f motion = handleVanillaMotion((float) speed);
                event.setX(motion.x);
                event.setZ(motion.y);
                strafe++;
            } else if (speedModeConfig.getValue() == Speed.GAY_HOP) {
                if (!Managers.ANTICHEAT.hasPassed(100)) {
                    strafe = 1;
                    return;
                }
                if (strafe == 1 && mc.player.verticalCollision
                        && MovementUtil.isInputtingMovement()) {
                    speed = 1.25f * base - 0.01f;
                } else if (strafe == 2 && mc.player.verticalCollision
                        && MovementUtil.isInputtingMovement()) {
                    float jump = (isBoxColliding() ? 0.2f : 0.4f) + jumpEffect;
                    event.setY(jump);
                    Managers.MOVEMENT.setMotionY(jump);
                    speed *= 2.149;
                } else if (strafe == 3) {
                    double moveSpeed = 0.66 * (distance - base);
                    speed = distance - moveSpeed;
                } else {
                    if (mc.player.isOnGround() && strafe > 0) {
                        if (1.35 * base - 0.01 > speed) {
                            strafe = 0;
                        } else {
                            strafe = MovementUtil.isInputtingMovement() ? 1 : 0;
                        }
                    }
                    speed = distance - distance / 159.0;
                }
                speed = Math.max(speed, base);
                if (strafe > 0) {
                    Vec2f motion = handleStrafeMotion((float) speed);
                    event.setX(motion.x);
                    event.setZ(motion.y);
                }
                strafe++;
            } else if (speedModeConfig.getValue() == Speed.V_HOP) {
                if (!Managers.ANTICHEAT.hasPassed(100)) {
                    strafe = 1;
                    return;
                }
                if (MathUtil.round(mc.player.getY() - (double) (int) mc.player.getY(), 3) == MathUtil.round(0.4, 3)) {
                    Managers.MOVEMENT.setMotionY(0.31 + jumpEffect);
                    event.setY(0.31 + jumpEffect);
                } else if (MathUtil.round(mc.player.getY() - (double) (int) mc.player.getY(), 3) == MathUtil.round(0.71, 3)) {
                    Managers.MOVEMENT.setMotionY(0.04 + jumpEffect);
                    event.setY(0.04 + jumpEffect);
                } else if (MathUtil.round(mc.player.getY() - (double) (int) mc.player.getY(), 3) == MathUtil.round(0.75, 3)) {
                    Managers.MOVEMENT.setMotionY(-0.2 - jumpEffect);
                    event.setY(-0.2 - jumpEffect);
                }
                if (!mc.world.isSpaceEmpty(null, mc.player.getBoundingBox().offset(0.0, -0.56, 0.0))
                        && MathUtil.round(mc.player.getY() - (double) (int) mc.player.getY(), 3) == MathUtil.round(0.55, 3)) {
                    Managers.MOVEMENT.setMotionY(-0.14 + jumpEffect);
                    event.setY(-0.14 + jumpEffect);
                }
                if (strafe != 1 || !mc.player.verticalCollision
                        || mc.player.forwardSpeed == 0.0f && mc.player.sidewaysSpeed == 0.0f) {
                    if (strafe != 2 || !mc.player.verticalCollision
                            || mc.player.forwardSpeed == 0.0f && mc.player.sidewaysSpeed == 0.0f) {
                        if (strafe == 3) {
                            double moveSpeed = 0.66 * (distance - base);
                            speed = distance - moveSpeed;
                        } else {
                            if (mc.player.isOnGround() && strafe > 0) {
                                if (1.35 * base - 0.01 > speed) {
                                    strafe = 0;
                                } else {
                                    strafe = MovementUtil.isInputtingMovement() ? 1 : 0;
                                }
                            }
                            speed = distance - distance / 159.0;
                        }
                    } else {
                        double jump = (isBoxColliding() ? 0.2 : 0.4) + jumpEffect;
                        Managers.MOVEMENT.setMotionY(jump);
                        event.setY(jump);
                        speed *= 2.149;
                    }
                } else {
                    speed = 2.0 * base - 0.01;
                }
                if (strafe > 8) {
                    speed = base;
                }
                speed = Math.max(speed, base);
                Vec2f motion = handleStrafeMotion((float) speed);
                event.setX(motion.x);
                event.setZ(motion.y);
                strafe++;
            } else if (speedModeConfig.getValue() == Speed.B_HOP) {
                if (!Managers.ANTICHEAT.hasPassed(100)) {
                    strafe = 4;
                    return;
                }
                if (MathUtil.round(mc.player.getY() - ((int) mc.player.getY()), 3) == MathUtil.round(0.138, 3)) {
                    Managers.MOVEMENT.setMotionY(mc.player.getVelocity().y - (0.08 + jumpEffect));
                    event.setY(event.getY() - (0.0931 + jumpEffect));
                    Managers.POSITION.setPositionY(mc.player.getY() - (0.0931 + jumpEffect));
                }
                if (strafe != 2 || mc.player.forwardSpeed == 0.0f && mc.player.sidewaysSpeed == 0.0f) {
                    if (strafe == 3) {
                        double moveSpeed = 0.66 * (distance - base);
                        speed = distance - moveSpeed;
                    } else {
                        if (mc.player.isOnGround()) {
                            strafe = 1;
                        }
                        speed = distance - distance / 159.0;
                    }
                } else {
                    double jump = (isBoxColliding() ? 0.2 : 0.4) + jumpEffect;
                    Managers.MOVEMENT.setMotionY(jump);
                    event.setY(jump);
                    speed *= 2.149;
                }
                speed = Math.max(speed, base);
                Vec2f motion = handleStrafeMotion((float) speed);
                event.setX(motion.x);
                event.setZ(motion.y);
                strafe++;
            } else if (speedModeConfig.getValue() == Speed.VANILLA) {
                Vec2f motion = handleVanillaMotion(vanillaStrafeConfig.getValue() ? (float) base :
                        speedConfig.getValue() / 10.0f);
                event.setX(motion.x);
                event.setZ(motion.y);
            }
        }
    }

    /**
     * @param speed
     * @return
     */
    public Vec2f handleStrafeMotion(final float speed) {
        float forward = mc.player.input.movementForward;
        float strafe = mc.player.input.movementSideways;
        float yaw = mc.player.prevYaw + (mc.player.getYaw() - mc.player.prevYaw) * mc.getTickDelta();
        if (forward == 0.0f && strafe == 0.0f) {
            return Vec2f.ZERO;
        } else if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += forward > 0.0f ? -45 : 45;
                strafe = 0.0f;
            } else if (strafe <= -1.0f) {
                yaw += forward > 0.0f ? 45 : -45;
                strafe = 0.0f;
            }
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        float rx = (float) Math.cos(Math.toRadians(yaw));
        float rz = (float) -Math.sin(Math.toRadians(yaw));
        return new Vec2f((forward * speed * rz) + (strafe * speed * rx),
                (forward * speed * rx) - (strafe * speed * rz));
    }

    public Vec2f handleVanillaMotion(final float speed) {
        float forward = mc.player.input.movementForward;
        float strafe = mc.player.input.movementSideways;
        if (forward == 0.0f && strafe == 0.0f) {
            return Vec2f.ZERO;
        } else if (forward != 0.0f && strafe != 0.0f) {
            forward *= (float) Math.sin(0.7853981633974483);
            strafe *= (float) Math.cos(0.7853981633974483);
        }
        return new Vec2f((float) (forward * speed * -Math.sin(Math.toRadians(mc.player.getYaw())) + strafe * speed * Math.cos(Math.toRadians(mc.player.getYaw()))),
                (float) (forward * speed * Math.cos(Math.toRadians(mc.player.getYaw())) - strafe * speed * -Math.sin(Math.toRadians(mc.player.getYaw()))));
    }

    @EventListener
    public void onPacketInbound(PacketEvent.Inbound event) {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (event.getPacket() instanceof ExplosionS2CPacket packet) {
            double x = packet.getPlayerVelocityX();
            double z = packet.getPlayerVelocityZ();
            // boostSpeed = Math.sqrt(x * x + z * z);
            // boostTicks = 0;
        } else if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket packet
                && packet.getId() == mc.player.getId()) {
            double x = packet.getVelocityX();
            double z = packet.getVelocityZ();
            // boostSpeed = Math.sqrt(x * x + z * z);
            // boostTicks = 0;
        } else if (event.getPacket() instanceof PlayerPositionLookS2CPacket) {
            resetStrafe();
        }
    }

    @EventListener
    public void onConfigUpdate(ConfigUpdateEvent event) {
        if (event.getConfig() == timerConfig && event.getStage() == EventStage.POST && isStrafe()) {
            if (timerConfig.getValue()) {
                prevTimer = Modules.TIMER.isEnabled();
                if (!prevTimer) {
                    Modules.TIMER.enable();
                    // Modules.TIMER.setTimer(1.0888f);
                }
            } else if (Modules.TIMER.isEnabled()) {
                Modules.TIMER.resetTimer();
                if (!prevTimer) {
                    Modules.TIMER.disable();
                }
            }
        }
    }

    public boolean isBoxColliding() {
        return !mc.world.isSpaceEmpty(mc.player, mc.player.getBoundingBox().offset(0.0, 0.21, 0.0));
    }

    public boolean checkIsCollidingEntity(Entity entity) {
        return entity != null && entity != mc.player && entity instanceof LivingEntity
                && !(entity instanceof FakePlayerEntity) && !(entity instanceof ArmorStandEntity);
    }

    public void setPrevTimer() {
        prevTimer = !prevTimer;
    }

    public boolean isUsingTimer() {
        return isEnabled() && timerConfig.getValue();
    }

    public void resetStrafe() {
        strafe = 4;
        strictTicks = 0;
        speed = 0.0f;
        distance = 0.0;
        accel = false;
    }

    public boolean isStrafe() {
        return speedModeConfig.getValue() != Speed.FIREWORK && speedModeConfig.getValue() != Speed.GRIM_COLLIDE && speedModeConfig.getValue() != Speed.VANILLA;
    }

    private enum Speed {
        STRAFE,
        STRAFE_STRICT,
        STRAFE_B_HOP,
        LOW_HOP,
        GAY_HOP,
        V_HOP,
        B_HOP,
        VANILLA,
        GRIM_COLLIDE,
        FIREWORK
    }
}
