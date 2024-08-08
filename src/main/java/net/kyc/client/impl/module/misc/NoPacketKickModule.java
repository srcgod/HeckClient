package net.kyc.client.impl.module.misc;

import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.network.DecodePacketEvent;

/**
 * @author linus
 * @since 1.0
 */
public class NoPacketKickModule extends ToggleModule {

    /**
     *
     */
    public NoPacketKickModule() {
        super("NoPacketKick", "Prevents getting kicked by packets", ModuleCategory.MISC);
    }

    // TODO: Add more packet kick checks
    @EventListener
    public void onDecodePacket(DecodePacketEvent event) {
        event.cancel();
    }

    @Override
    public void onUpdate() {

    }
}
