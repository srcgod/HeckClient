package net.kyc.client.impl.module.client;

import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;

/**
 * @author xgraza
 * @since 1.0
 */
public final class CapesModule extends ToggleModule
{
    Config<Boolean> optifineConfig = new BooleanConfig("Optifine", "If to show optifine capes", true);

    public CapesModule()
    {
        super("Capes", "Shows player capes", ModuleCategory.CLIENT);
        enable();
    }

    public Config<Boolean> getOptifineConfig()
    {
        return optifineConfig;
    }

    @Override
    public void onUpdate() {

    }
}
