package net.kyc.client.impl.module.combat;

import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;

/**
 * @author linus
 * @since 1.0
 */
public class AutoWebModule extends ToggleModule {

    /**
     *
     */
    public AutoWebModule() {
        super("AutoWeb", "Automatically traps nearby entities in webs",
                ModuleCategory.COMBAT);
    }

    @Override
    public void onUpdate() {

    }
}
