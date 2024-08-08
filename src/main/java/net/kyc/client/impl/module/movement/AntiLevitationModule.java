package net.kyc.client.impl.module.movement;

import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.entity.LevitationEvent;

/**
 * @author linus
 * @since 1.0
 */
public class AntiLevitationModule extends ToggleModule {

    /**
     *
     */
    public AntiLevitationModule() {
        super("AntiLevitation", "Prevents the player from being levitated",
                ModuleCategory.MOVEMENT);
    }

    @EventListener
    public void onLevitation(LevitationEvent event) {
        event.cancel();
    }

    @Override
    public void onUpdate() {

    }
}
