package net.kyc.client.impl.module.misc;

import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.util.chat.ChatUtils;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.kyc.client.impl.event.PacketEvent;

public class GhostMeowModule extends ToggleModule {
    private boolean bypass = false;

    public GhostMeowModule() {
        super("Ghost","lol", ModuleCategory.MISC);
    }

    @Override
    public void onEnable() {
        bypass = false;
        ChatUtils.sendMessage( "Для возрождения выключи модуль!" + "To revive, turn off the module!");
    }

    @Override
    public void onDisable() {
        if (mc.player != null) mc.player.requestRespawn();
        bypass = false;
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) return;
        if (mc.player.getHealth() == 0.0f) {
            mc.player.setHealth(20.0f);
            bypass = true;
            mc.setScreen(null);
            mc.player.setPosition(mc.player.getX(), mc.player.getY(), mc.player.getZ());
        }
    }

    @EventListener
    public void onPacketSend(PacketEvent.Send event) {
        if (bypass && event.getPacket() instanceof PlayerMoveC2SPacket) event.cancel();
    }
}
