package net.kyc.client.api.module;

import net.kyc.client.Heckk;

/**
 * {@link Module} implementation that runs concurrently and cannot be disabled
 * without the use of Commands. Unlike {@link ToggleModule},
 * {@link ConcurrentModule} does not have an enabled state or a keybinding.
 *
 * @author linus
 * @since 1.0
 */
public class ConcurrentModule extends Module {
    /**
     * @param name
     * @param desc
     * @param category
     */
    public ConcurrentModule(String name, String desc, ModuleCategory category) {
        super(name, desc, category);
        Heckk.EVENT_HANDLER.subscribe(this);
    }
}
