package net.kyc.client.impl.module.misc;

import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.FramerateLimitEvent;

/**
 * @author linus
 * @since 1.0
 */
public class UnfocusedFPSModule extends ToggleModule {
    //
    Config<Integer> limitConfig = new NumberConfig<>("Limit", "The FPS limit when game is in the background", 5, 30, 120);

    /**
     *
     */
    public UnfocusedFPSModule() {
        super("UnfocusedFPS", "Reduces FPS when game is in the background",
                ModuleCategory.MISC);
    }

    @EventListener
    public void onFramerateLimit(FramerateLimitEvent event) {
        if (!mc.isWindowFocused()) {
            event.cancel();
            event.setFramerateLimit(limitConfig.getValue());
        }
    }

    @Override
    public void onUpdate() {

    }
}
