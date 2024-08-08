package net.kyc.client.impl.module.movement;

import net.minecraft.util.math.Box;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.EnumConfig;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.EventStage;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.TickEvent;
import net.kyc.client.impl.event.entity.player.PlayerMoveEvent;
import net.kyc.client.impl.event.network.TickMovementEvent;
import net.kyc.client.init.Managers;
import net.kyc.client.init.Modules;
import net.kyc.client.util.math.timer.CacheTimer;
import net.kyc.client.util.math.timer.Timer;

/**
 * @author linus
 * @since 1.0
 */
public class FastFallModule extends ToggleModule {

    //
    Config<Float> heightConfig = new NumberConfig<>("Height", "The maximum fall height", 1.0f, 3.0f, 10.0f);
    Config<FallMode> fallModeConfig = new EnumConfig<>("Mode", "The mode for falling down blocks", FallMode.STEP, FallMode.values());
    Config<Integer> shiftTicksConfig = new NumberConfig<>("ShiftTicks", "Number of ticks to shift ahead", 1, 3, 5, () -> fallModeConfig.getValue() == FallMode.SHIFT);
    //
    private boolean prevOnGround;
    //
    private boolean cancelFallMovement;
    private int fallTicks;
    private final Timer fallTimer = new CacheTimer();

    /**
     *
     */
    public FastFallModule() {
        super("FastFall", "Falls down blocks faster", ModuleCategory.MOVEMENT);
    }

    @Override
    public void onDisable() {
        cancelFallMovement = false;
        fallTicks = 0;
    }

    @Override
    public void onUpdate() {

    }

    @EventListener
    public void onTick(TickEvent event) {
        if (event.getStage() == EventStage.PRE) {
            prevOnGround = mc.player.isOnGround();
            if (fallModeConfig.getValue() == FallMode.STEP) {
                if (mc.player.isRiding()
                        || mc.player.isFallFlying()
                        || mc.player.isHoldingOntoLadder()
                        || mc.player.isInLava()
                        || mc.player.isTouchingWater()
                        || mc.player.input.jumping
                        || mc.player.input.sneaking) {
                    return;
                }
                if (Modules.SPEED.isEnabled() || Modules.LONG_JUMP.isEnabled()
                        || Modules.FLIGHT.isEnabled() || Modules.PACKET_FLY.isEnabled()) {
                    return;
                }
                if (mc.player.isOnGround() && isNearestBlockWithinHeight(heightConfig.getValue())) {
                    Managers.MOVEMENT.setMotionY(-3.0);
                    // Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(false));
                }
            }
        }
    }

    @EventListener
    public void onTickMovement(TickMovementEvent event) {
        if (fallModeConfig.getValue() == FallMode.SHIFT) {
            if (mc.player.isRiding()
                    || mc.player.isFallFlying()
                    || mc.player.isHoldingOntoLadder()
                    || mc.player.isInLava()
                    || mc.player.isTouchingWater()
                    || mc.player.input.jumping
                    || mc.player.input.sneaking) {
                return;
            }
            if (!Managers.ANTICHEAT.hasPassed(1000) || !fallTimer.passed(1000)
                    || Modules.SPEED.isEnabled()
                    || Modules.LONG_JUMP.isEnabled()
                    || Modules.FLIGHT.isEnabled()
                    || Modules.PACKET_FLY.isEnabled()) {
                return;
            }
            if (mc.player.getVelocity().y < 0 && prevOnGround && !mc.player.isOnGround()
                    && isNearestBlockWithinHeight(heightConfig.getValue() + 0.01)) {
                fallTimer.reset();
                event.cancel();
                event.setIterations(shiftTicksConfig.getValue());
                cancelFallMovement = true;
                fallTicks = 0;
            }
        }
    }

    @EventListener
    public void onPlayerMove(PlayerMoveEvent event) {
        if (Modules.FLIGHT.isEnabled() || Modules.PACKET_FLY.isEnabled()) {
            return;
        }
        if (cancelFallMovement && fallModeConfig.getValue() == FallMode.SHIFT) {
            event.setX(0.0);
            event.setZ(0.0);
            Managers.MOVEMENT.setMotionXZ(0.0, 0.0);
            ++fallTicks;
            if (fallTicks > shiftTicksConfig.getValue()) {
                cancelFallMovement = false;
                fallTicks = 0;
            }
        }
    }

    private boolean isNearestBlockWithinHeight(double height) {
        Box bb = mc.player.getBoundingBox();
        for (double i = 0; i < height + 0.5; i += 0.01) {
            if (!mc.world.isSpaceEmpty(mc.player, bb.offset(0, -i, 0))) {
                return true;
            }
        }
        return false;
    }

    public enum FallMode {
        STEP,
        SHIFT
    }
}
