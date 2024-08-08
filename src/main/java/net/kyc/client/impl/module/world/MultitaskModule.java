package net.kyc.client.impl.module.world;

import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.ItemMultitaskEvent;

/**
 * @author linus
 * @since 1.0
 */
public class MultitaskModule extends ToggleModule {

    public MultitaskModule() {
        super("MultiTask", "Allows you to mine and use items simultaneously", ModuleCategory.WORLD);
    }

    @EventListener
    public void onItemMultitask(ItemMultitaskEvent event) {
        event.cancel();
    }

    @Override
    public void onUpdate() {

    }
}
