package net.kyc.client.api.config.setting;

import net.kyc.client.Heckk;
import net.kyc.client.api.config.ConfigContainer;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.api.render.anim.Animation;

/**
 * @author linus
 * @see BooleanConfig
 * @since 1.0
 */
public class ToggleConfig extends BooleanConfig {
    public ToggleConfig(String name, String desc, Boolean val) {
        super(name, desc, val);
    }

    /**
     * @param val The param value
     */
    @Override
    public void setValue(Boolean val) {
        super.setValue(val);
        ConfigContainer container = getContainer();
        if (container instanceof ToggleModule toggle) {
            Animation anim = toggle.getAnimation();
            anim.setState(val);
            if (val) {
                Heckk.EVENT_HANDLER.subscribe(toggle);
            } else {
                Heckk.EVENT_HANDLER.unsubscribe(toggle);
            }
        }
    }

    public void enable() {
        ConfigContainer container = getContainer();
        if (container instanceof ToggleModule toggle) {
            toggle.enable();
        }
    }

    public void disable() {
        ConfigContainer container = getContainer();
        if (container instanceof ToggleModule toggle) {
            toggle.disable();
        }
    }
}
