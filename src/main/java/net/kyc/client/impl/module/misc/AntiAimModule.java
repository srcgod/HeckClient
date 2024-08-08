package net.kyc.client.impl.module.misc;

import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.EnumConfig;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.RotationModule;
import net.kyc.client.impl.event.network.PlayerTickEvent;

/**
 * @author linus
 * @since 1.0
 */
public class AntiAimModule extends RotationModule {
    //
    Config<YawMode> yawModeConfig = new EnumConfig<>("Yaw", "The mode for the rotation yaw spin ", YawMode.SPIN, YawMode.values());
    Config<PitchMode> pitchModeConfig = new EnumConfig<>("Pitch", "The mode for the rotation pitch spin", PitchMode.DOWN, PitchMode.values());
    Config<Float> yawAddConfig = new NumberConfig<>("YawAdd", "The yaw to add during each rotation", -180.0f, 20.0f, 180.0f);
    Config<Float> pitchAddConfig = new NumberConfig<>("CustomPitch", "The pitch to add during each rotation", -90.0f, 20.0f, 90.0f);
    Config<Float> spinSpeedConfig = new NumberConfig<>("SpinSpeed", "The yaw speed to rotate", 1.0f, 16.0f, 40.0f);
    Config<Integer> flipTicksConfig = new NumberConfig<>("FlipTicks", "The number of ticks to wait between jitter", 2, 2, 20);
    //
    private float yaw;
    private float pitch;
    //
    private float prevYaw, prevPitch;

    /**
     *
     */
    public AntiAimModule() {
        super("AntiAim", "Makes it harder to accurately aim at the player",
                ModuleCategory.MISC, 50);
    }

    /**
     *
     */
    @Override
    public void onEnable() {
        if (mc.player == null) {
            return;
        }
        prevYaw = mc.player.getYaw();
        prevPitch = mc.player.getPitch();
    }

    /**
     * @param event
     */
    @EventListener
    public void onPlayerUpdate(PlayerTickEvent event) {
        if (mc.options.attackKey.isPressed() || mc.options.useKey.isPressed()) {
            return;
        }
        yaw = switch (yawModeConfig.getValue()) {
            case OFF -> mc.player.getYaw();
            case STATIC -> mc.player.getYaw() + yawAddConfig.getValue();
            case ZERO -> prevYaw;
            case SPIN -> {
                float spin = yaw + spinSpeedConfig.getValue();
                if (spin > 360.0f) {
                    yield spin - 360.0f;
                }
                yield spin;
            }
            case JITTER -> mc.player.getYaw() + ((mc.player.age % flipTicksConfig.getValue() == 0) ?
                    yawAddConfig.getValue() : -yawAddConfig.getValue());
        };
        pitch = switch (pitchModeConfig.getValue()) {
            case OFF -> mc.player.getPitch();
            case STATIC -> pitchAddConfig.getValue();
            case ZERO -> prevPitch;
            case UP -> -90.0f;
            case DOWN -> 90.0f;
            case JITTER -> {
                float jitter = pitch + 30.0f;
                if (jitter > 90.0f) {
                    yield -90.0f;
                }
                if (jitter < -90.0f) {
                    yield 90.0f;
                }
                yield jitter;
            }
        };
        //
        setRotation(yaw, pitch);
    }

    public enum YawMode {
        OFF, STATIC, ZERO, SPIN, JITTER
    }

    public enum PitchMode {
        OFF, STATIC, ZERO, UP, DOWN, JITTER
    }
}
