package net.kyc.client.impl.manager.combat;

import net.kyc.client.Heckk;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.impl.event.entity.EntityDeathEvent;
import net.kyc.client.impl.event.network.DisconnectEvent;
import net.kyc.client.impl.event.network.PacketEvent;
import net.kyc.client.util.Globals;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author linus
 * @since 1.0
 */
public class TotemManager implements Globals {
    //
    private final ConcurrentMap<UUID, Integer> totems = new ConcurrentHashMap<>();

    /**
     *
     */
    public TotemManager() {
        Heckk.EVENT_HANDLER.subscribe(this);
    }

    @EventListener
    public void onPacketInbound(PacketEvent.Inbound event) {
        if (mc.world != null) {
            if (event.getPacket() instanceof EntityStatusS2CPacket packet
                    && packet.getStatus() == EntityStatuses.USE_TOTEM_OF_UNDYING) {
                Entity entity = packet.getEntity(mc.world);
                if (entity != null && entity.isAlive()) {
                    totems.put(entity.getUuid(), totems.containsKey(entity.getUuid()) ?
                            totems.get(entity.getUuid()) + 1 : 1);
                }
            }
        }
    }

    @EventListener(priority = Integer.MIN_VALUE)
    public void onRemoveEntity(EntityDeathEvent event) {
        if (event.getEntity() == mc.player) {
            return;
        }
        totems.remove(event.getEntity().getUuid());
    }

    @EventListener
    public void onDisconnect(DisconnectEvent event) {
        totems.clear();
    }

    /**
     * Returns the number of totems popped by the given {@link PlayerEntity}
     *
     * @param entity
     * @return Ehe number of totems popped by the player
     */
    public int getTotems(Entity entity) {
        return totems.getOrDefault(entity.getUuid(), 0);
    }
}
