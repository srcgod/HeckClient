package net.kyc.client.impl.module.misc;

import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;

public class CharSuffix extends ToggleModule {
    public CharSuffix() {
        super("ChatSuffix", "green", ModuleCategory.MISC);
    }

    @Override
    public void onUpdate() {

    }
}

