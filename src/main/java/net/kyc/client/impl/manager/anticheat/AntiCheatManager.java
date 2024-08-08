package net.kyc.client.impl.manager.anticheat;

import net.kyc.client.Heckk;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Vec3d;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.impl.event.network.DisconnectEvent;
import net.kyc.client.impl.event.network.PacketEvent;
import net.kyc.client.util.Globals;

import java.util.Arrays;

/**
 * @author xgraza
 * @since 1.0
 */
public final class AntiCheatManager implements Globals
{
    private SetbackData lastSetback;

    private final int[] transactions = new int[4];
    private int index;
    private boolean isGrim;

    public AntiCheatManager()
    {
        Heckk.EVENT_HANDLER.subscribe(this);
        Arrays.fill(transactions, -1);
    }

    @EventListener
    public void onPacketInbound(final PacketEvent.Inbound event)
    {
        if (event.getPacket() instanceof CommonPingS2CPacket packet)
        {
            if (index > 3)
            {
                return;
            }
            final int uid = packet.getParameter();
            transactions[index] = uid;
            ++index;
            if (index == 4)
            {
                grimCheck();
            }
        }
        else if (event.getPacket() instanceof PlayerPositionLookS2CPacket packet)
        {
            lastSetback = new SetbackData(new Vec3d(packet.getX(), packet.getY(), packet.getZ()),
                    System.currentTimeMillis(), packet.getTeleportId());
        }
    }

    @EventListener
    public void onDisconnect(final DisconnectEvent event)
    {
        Arrays.fill(transactions, -1);
        index = 0;
        isGrim = false;
    }

    private void grimCheck()
    {
        for (int i = 0; i < 4; ++i)
        {
            if (transactions[i] != -i)
            {
                break;
            }
        }
        isGrim = true;
        Heckk.LOGGER.info("Server is running GrimAC.");
    }

    public boolean isGrim()
    {
        return isGrim;
    }

    public boolean hasPassed(final long timeMS)
    {
        return lastSetback != null && lastSetback.timeSince() >= timeMS;
    }
}
