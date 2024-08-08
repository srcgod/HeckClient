package net.kyc.client.impl.module.client;

import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.module.ConcurrentModule;
import net.kyc.client.api.module.ModuleCategory;

/**
 * @author linus
 * @since 1.0
 */
public class RotationsModule extends ConcurrentModule {
    //
    Config<Float> preserveTicksConfig = new NumberConfig<>("PreserveTicks", "Time to preserve rotations after reaching the target rotations", 0.0f, 10.0f, 20.0f);
    Config<Boolean> movementFixConfig = new BooleanConfig("MovementFix", "Fixes movement on Grim when rotating", false);
    //
    private float prevYaw;

    /**
     *
     */
    public RotationsModule() {
        super("Rotations", "Manages client rotations",
                ModuleCategory.CLIENT);
    }

    public boolean getMovementFix() {
        return movementFixConfig.getValue();
    }

    /**
     * @return
     */
    public float getPreserveTicks() {
        return preserveTicksConfig.getValue();
    }
}
