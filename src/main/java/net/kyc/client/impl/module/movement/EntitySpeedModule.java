package net.kyc.client.impl.module.movement;

import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.EventStage;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.TickEvent;
import net.kyc.client.impl.event.network.PacketEvent;
import net.kyc.client.init.Managers;
import net.kyc.client.util.math.timer.CacheTimer;
import net.kyc.client.util.math.timer.Timer;

import java.text.DecimalFormat;

/**
 * @author linus
 * @since 1.0
 */
public class EntitySpeedModule extends ToggleModule {

    //
    Config<Float> speedConfig = new NumberConfig<>("Speed", "The speed of the entity while moving", 0.1f, 0.5f, 4.0f);
    Config<Boolean> antiStuckConfig = new BooleanConfig("AntiStuck", "Prevents entities from getting stuck when moving up", false);
    Config<Boolean> strictConfig = new BooleanConfig("Strict", "The NCP-Updated bypass for speeding up entity movement", false);
    //
    private final Timer entityJumpTimer = new CacheTimer();

    /**
     *
     */
    public EntitySpeedModule() {
        super("EntitySpeed", "Increases riding entity speeds", ModuleCategory.MOVEMENT);
    }

    @Override
    public String getModuleData() {
        DecimalFormat decimal = new DecimalFormat("0.0");
        return decimal.format(speedConfig.getValue());
    }

    @EventListener
    public void onTick(TickEvent event) {
        if (event.getStage() != EventStage.PRE) {
            return;
        }
        if (mc.player.isRiding() && mc.player.getControllingVehicle() != null) {
            double d = Math.cos(Math.toRadians(mc.player.getYaw() + 90.0f));
            double d2 = Math.sin(Math.toRadians(mc.player.getYaw() + 90.0f));
            BlockPos pos1 = BlockPos.ofFloored(mc.player.getX() + (2.0 * d),
                    mc.player.getY() - 1.0, mc.player.getZ() + (2.0 * d2));
            BlockPos pos2 = BlockPos.ofFloored(mc.player.getX() + (2.0 * d),
                    mc.player.getY() - 2.0, mc.player.getZ() + (2.0 * d2));
            if (antiStuckConfig.getValue() && !mc.player.getControllingVehicle().isOnGround()
                    && !mc.world.getBlockState(pos1).blocksMovement()
                    && !mc.world.getBlockState(pos2).blocksMovement()) {
                entityJumpTimer.reset();
                return;
            }
            BlockPos pos3 = BlockPos.ofFloored(mc.player.getX() + (2.0 * d),
                    mc.player.getY(), mc.player.getZ() + (2.0 * d2));
            if (antiStuckConfig.getValue() && mc.world.getBlockState(pos3).blocksMovement()) {
                entityJumpTimer.reset();
                return;
            }
            BlockPos pos4 = BlockPos.ofFloored(mc.player.getX() + d,
                    mc.player.getY() + 1.0, mc.player.getZ() + d2);
            if (antiStuckConfig.getValue() && mc.world.getBlockState(pos4).blocksMovement()) {
                entityJumpTimer.reset();
                return;
            }
            if (mc.player.input.jumping) {
                entityJumpTimer.reset();
            }
            if (entityJumpTimer.passed(10000) || !antiStuckConfig.getValue()) {
                if (!mc.player.getControllingVehicle().isTouchingWater() || mc.player.input.jumping
                        || !entityJumpTimer.passed(1000)) {
                    if (mc.player.getControllingVehicle().isOnGround()) {
                        mc.player.getControllingVehicle().setVelocity(mc.player.getVelocity().x,
                                0.4, mc.player.getVelocity().z);
                    }
                    mc.player.getControllingVehicle().setVelocity(mc.player.getVelocity().x,
                            -0.4, mc.player.getVelocity().z);
                }
                if (strictConfig.getValue()) {
                    Managers.NETWORK.sendPacket(PlayerInteractEntityC2SPacket.interact(
                            mc.player.getControllingVehicle(), false, Hand.MAIN_HAND));
                }
                handleEntityMotion(speedConfig.getValue(), d, d2);
                entityJumpTimer.reset();
            }
        }
    }

    @EventListener
    public void onPacketInbound(PacketEvent.Inbound event) {
        if (mc.player == null || !mc.player.isRiding() || mc.options.sneakKey.isPressed()
                || mc.player.getControllingVehicle() == null) {
            return;
        }
        if (strictConfig.getValue()) {
            if (event.getPacket() instanceof EntityPassengersSetS2CPacket) {
                event.cancel();
            } else if (event.getPacket() instanceof PlayerPositionLookS2CPacket) {
                event.cancel();
            }
        }
    }

    private void handleEntityMotion(float entitySpeed, double d, double d2) {
        Vec3d motion = mc.player.getControllingVehicle().getVelocity();
        //
        float forward = mc.player.input.movementForward;
        float strafe = mc.player.input.movementSideways;
        if (forward == 0 && strafe == 0) {
            mc.player.getControllingVehicle().setVelocity(0.0, motion.y, 0.0);
            return;
        }
        mc.player.getControllingVehicle().setVelocity((forward * entitySpeed * d) + (strafe * entitySpeed * d2),
                motion.y, (forward * entitySpeed * d2) - (strafe * entitySpeed * d));
    }

    @Override
    public void onUpdate() {

    }
}
