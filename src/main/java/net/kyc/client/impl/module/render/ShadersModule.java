package net.kyc.client.impl.module.render;

import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;

/**
 * @author linus
 * @since 1.0
 */
public class ShadersModule extends ToggleModule {

    public ShadersModule() {
        super("Shaders", "Renders shaders in-game", ModuleCategory.RENDER);
    }

    @Override
    public void onUpdate() {

    }
}
