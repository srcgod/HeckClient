package net.kyc.client.impl.module.movement;

import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.entity.JumpDelayEvent;

public class NoJumpDelayModule extends ToggleModule {
    public NoJumpDelayModule() {
        super("NoJumpDelay", "Removes the vanilla jump delay", ModuleCategory.MOVEMENT);
    }

    @EventListener
    public void onJumpDelay(JumpDelayEvent event) {
        event.cancel();
    }

    @Override
    public void onUpdate() {

    }
}
