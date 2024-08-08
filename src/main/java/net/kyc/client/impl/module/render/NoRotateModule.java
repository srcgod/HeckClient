package net.kyc.client.impl.module.render;

import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.network.PacketEvent;
import net.kyc.client.mixin.accessor.AccessorPlayerMoveC2SPacket;
import net.kyc.client.mixin.accessor.AccessorPlayerPositionLookS2CPacket;

/**
 * @author linus
 * @since 1.0
 */
public class NoRotateModule extends ToggleModule {
    Config<Boolean> positionAdjustConfig = new BooleanConfig("PositionAdjust", "Adjusts outgoing rotation packets", false);
    private float yaw, pitch;
    private boolean cancelRotate;
    public NoRotateModule() {
        super("NoRotate", "Prevents server from forcing rotations", ModuleCategory.RENDER);
    }

    @EventListener
    public void onPacketInbound(PacketEvent.Inbound event) {
        if (mc.player == null || mc.currentScreen instanceof DownloadingTerrainScreen) {
            return;
        }
        if (event.getPacket() instanceof PlayerPositionLookS2CPacket packet) {
            yaw = packet.getYaw();
            pitch = packet.getPitch();
            ((AccessorPlayerPositionLookS2CPacket) packet).setYaw(mc.player.getYaw());
            ((AccessorPlayerPositionLookS2CPacket) packet).setPitch(mc.player.getPitch());
            packet.getFlags().remove(PositionFlag.X_ROT);
            packet.getFlags().remove(PositionFlag.Y_ROT);
            cancelRotate = true;
        }
    }

    @EventListener
    public void onPacketOutbound(PacketEvent.Outbound event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket.Full packet && cancelRotate) {
            if (positionAdjustConfig.getValue()) {
                ((AccessorPlayerMoveC2SPacket) packet).hookSetYaw(yaw);
                ((AccessorPlayerMoveC2SPacket) packet).hookSetPitch(pitch);
            }
            cancelRotate = false;
        }
    }

    @Override
    public void onUpdate() {

    }
}
