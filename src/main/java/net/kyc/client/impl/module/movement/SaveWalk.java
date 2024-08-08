package net.kyc.client.impl.module.movement;

import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.player.ClipAtLedgeEvent;

public class SaveWalk extends ToggleModule {
    public SaveWalk() {
        super("SafWalk", "meowmewo", ModuleCategory.MOVEMENT);
    }
    private void onClipAtLedge(ClipAtLedgeEvent event) {
        if (!mc.player.isSneaking()) event.setClip(true);
    }

    @Override
    public void onUpdate() {

    }
}