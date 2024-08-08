package net.kyc.client.impl.module.client;

import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.module.ConcurrentModule;
import net.kyc.client.api.module.ModuleCategory;

/**
 * @author linus
 * @since 1.0
 */
public class ChatModule extends ConcurrentModule {
    //
    Config<Boolean> debugConfig = new BooleanConfig("ChatDebug", "Allows client debug messages to be printed in the chat", false);

    /**
     *
     */
    public ChatModule() {
        super("Chat", "Manages the client chat", ModuleCategory.CLIENT);
    }
}
