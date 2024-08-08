package net.kyc.client.impl.module.misc;

import net.minecraft.client.gui.screen.ingame.BeaconScreen;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.network.packet.c2s.play.UpdateBeaconC2SPacket;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.ScreenOpenEvent;
import net.kyc.client.impl.event.network.PacketEvent;
import net.kyc.client.impl.gui.beacon.BeaconSelectorScreen;
import net.kyc.client.mixin.accessor.AccessorUpdateBeaconC2SPacket;

import java.util.Optional;

/**
 * @author linus
 * @since 1.0
 */
public class BeaconSelectorModule extends ToggleModule {
    //
    private StatusEffect primaryEffect;
    private StatusEffect secondaryEffect;
    //
    private boolean customBeacon;

    /**
     *
     */
    public BeaconSelectorModule() {
        super("BeaconSelector", "Allows you to change beacon effects",
                ModuleCategory.MISC);
    }

    @EventListener
    public void onPacketOutbound(PacketEvent.Outbound event) {
        if (event.getPacket() instanceof UpdateBeaconC2SPacket packet) {
            ((AccessorUpdateBeaconC2SPacket) packet).setPrimaryEffect(Optional.ofNullable(primaryEffect));
            ((AccessorUpdateBeaconC2SPacket) packet).setSecondaryEffect(Optional.ofNullable(secondaryEffect));
        }
    }

    @EventListener
    public void onScreenOpen(ScreenOpenEvent event) {
        if (event.getScreen() instanceof BeaconScreen screen && !customBeacon) {
            event.cancel();
            customBeacon = true;
            mc.setScreen(new BeaconSelectorScreen(screen.getScreenHandler(),
                    mc.player.getInventory(), screen.getTitle()));
            customBeacon = false;
        }
    }

    @Override
    public void onUpdate() {

    }
}
