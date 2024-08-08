package net.kyc.client.impl.module.misc;

import net.minecraft.client.gui.screen.DeathScreen;
import net.kyc.client.api.event.EventStage;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.ScreenOpenEvent;
import net.kyc.client.impl.event.TickEvent;

/**
 * @author linus
 * @since 1.0
 */
public class AutoRespawnModule extends ToggleModule {
    //
    private boolean respawn;

    /**
     *
     */
    public AutoRespawnModule() {
        super("AutoRespawn", "Respawns automatically after a death",
                ModuleCategory.MISC);
    }

    @EventListener
    public void onTick(TickEvent event) {
        if (event.getStage() == EventStage.PRE && respawn && mc.player.isDead()) {
            mc.player.requestRespawn();
            respawn = false;
        }
    }

    @EventListener
    public void onScreenOpen(ScreenOpenEvent event) {
        if (event.getScreen() instanceof DeathScreen) {
            respawn = true;
        }
    }

    @Override
    public void onUpdate() {

    }
}
