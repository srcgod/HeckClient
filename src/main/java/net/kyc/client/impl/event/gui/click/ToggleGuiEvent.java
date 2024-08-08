package net.kyc.client.impl.event.gui.click;

import net.kyc.client.api.event.Event;
import net.kyc.client.api.module.ToggleModule;

/**
 * @author linus
 * @since 1.0
 */
public class ToggleGuiEvent extends Event {
    private final ToggleModule module;

    public ToggleGuiEvent(ToggleModule module) {
        this.module = module;
    }

    public ToggleModule getModule() {
        return module;
    }

    public boolean isEnabled() {
        return module.isEnabled();
    }
}
