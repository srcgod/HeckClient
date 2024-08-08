package net.kyc.client.impl.module.render;

import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;

/**
 * @author linus
 * @since 1.0
 */
public class SearchModule extends ToggleModule {

    public SearchModule() {
        super("Search", "Highlights specified blocks in the world", ModuleCategory.RENDER);
    }

    @Override
    public void onUpdate() {

    }
}
