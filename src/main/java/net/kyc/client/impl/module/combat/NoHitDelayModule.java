package net.kyc.client.impl.module.combat;

import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.AttackCooldownEvent;

public class NoHitDelayModule extends ToggleModule {
    public NoHitDelayModule() {
        super("NoHitDelay", "Removes vanilla attack delay", ModuleCategory.COMBAT);
    }

    @EventListener
    public void onAttackCooldown(AttackCooldownEvent event) {
        event.cancel();
    }

    @Override
    public void onUpdate() {

    }
}
