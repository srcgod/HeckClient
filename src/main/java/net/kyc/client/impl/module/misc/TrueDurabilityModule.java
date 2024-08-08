package net.kyc.client.impl.module.misc;

import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.item.DurabilityEvent;

/**
 * @author linus
 * @since 1.0
 */
public class TrueDurabilityModule extends ToggleModule {

    /**
     *
     */
    public TrueDurabilityModule() {
        super("TrueDurability", "Displays the true durability of unbreakable items",
                ModuleCategory.MISC);
    }

    @EventListener
    public void onDurability(DurabilityEvent event) {
        // ??? Whats this
        int dura = event.getItemDamage();
        if (event.getDamage() < 0) {
            dura = event.getDamage();
        }
        event.cancel();
        event.setDamage(dura);
    }

    @Override
    public void onUpdate() {

    }
}
