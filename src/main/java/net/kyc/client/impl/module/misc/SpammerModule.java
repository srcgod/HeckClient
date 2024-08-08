package net.kyc.client.impl.module.misc;

import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;

/**
 * @author linus
 * @since 1.0
 */
public class SpammerModule extends ToggleModule {

    /**
     *
     */
    public SpammerModule() {
        super("Spammer", "Spams messages in the chat", ModuleCategory.MISC);
    }

    @Override
    public void onUpdate() {

    }
}
