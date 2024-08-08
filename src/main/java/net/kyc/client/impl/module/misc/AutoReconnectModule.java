package net.kyc.client.impl.module.misc;

import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.mixin.gui.screen.MixinDisconnectedScreen;

/**
 * @author linus
 * @see MixinDisconnectedScreen
 * @since 1.0
 */
public class AutoReconnectModule extends ToggleModule {
    //
    Config<Integer> delayConfig = new NumberConfig<>("Delay", "The delay between reconnects to a server", 0, 5, 100);

    /**
     *
     */
    public AutoReconnectModule() {
        super("AutoReconnect", "Automatically reconnects to a server " +
                "immediately after disconnecting", ModuleCategory.MISC);
    }

    /**
     * @return
     */
    public int getDelay() {
        return delayConfig.getValue();
    }

    @Override
    public void onUpdate() {

    }
}
