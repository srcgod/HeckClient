package net.kyc.client.impl.module.client;

import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;

/**
 * @author linus
 * @since 1.0
 */
public class FontModule extends ToggleModule {
    //
    Config<Boolean> shadowConfig = new BooleanConfig("Shadow", "Renders text with a shadow background", true);

    /**
     *
     */
    public FontModule() {
        super("Font", "Changes the client text to custom font rendering",
                ModuleCategory.CLIENT);
    }

    /**
     * @return
     */
    public boolean getShadow() {
        return shadowConfig.getValue();
    }

    @Override
    public void onUpdate() {

    }
}
