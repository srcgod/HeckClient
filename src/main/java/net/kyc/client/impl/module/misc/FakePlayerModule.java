package net.kyc.client.impl.module.misc;

import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.entity.player.PushEntityEvent;
import net.kyc.client.impl.event.network.DisconnectEvent;
import net.kyc.client.util.world.FakePlayerEntity;

/**
 * @author srcgod
 * @see FakePlayerEntity
 * @since 1.0
 */
public class FakePlayerModule extends ToggleModule {

    private FakePlayerEntity fakePlayer;
    //private FakePenisEntity penisEntity;

    /**
     *
     */
    public FakePlayerModule() {
        super("FakePlayer", "Spawns an indestructible client-side player",
                ModuleCategory.MISC);
    }

    @Override
    public void onEnable() {
        if (mc.player != null && mc.world != null) {
            fakePlayer = new FakePlayerEntity(mc.player, "FakePlayer");
            fakePlayer.spawnPlayer();
        }
    }

    @Override
    public void onDisable() {
        if (fakePlayer != null) {
            fakePlayer.despawnPlayer();
            fakePlayer = null;
        }
    }

    @Override
    public void onUpdate() {

    }

    @EventListener
    public void onDisconnect(DisconnectEvent event) {
        fakePlayer = null;
        disable();
    }

    @EventListener
    public void onPushEntity(PushEntityEvent event)
    {
        // Prevents Simulation flags (as the FakePlayer is client only, so Grim rightfully
        // flags us for that push motion that shouldn't happen
        if (event.getPushed().equals(mc.player) && event.getPusher().equals(fakePlayer))
        {
            event.setCanceled(true);
        }
    }
}
