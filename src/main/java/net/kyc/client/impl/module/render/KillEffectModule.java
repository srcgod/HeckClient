package net.kyc.client.impl.module.render;

import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.RunTickEvent;
import net.kyc.client.impl.event.render.RenderWorldEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KillEffectModule extends ToggleModule {
    public KillEffectModule() {
        super("KillEffect", "", ModuleCategory.RENDER);
    }

    private final Map<Entity, Long> renderEntities = new ConcurrentHashMap<>();
    private final Map<Entity, Long> lightingEntities = new ConcurrentHashMap<>();

    @Override
    public void onUpdate() {
    }

    @EventListener
    public void onRender(RenderWorldEvent event) {
        if (mc.player == null || mc.world == null) return;

        renderEntities.forEach((entity, time) -> {
            LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, mc.world);
            lightningEntity.refreshPositionAfterTeleport(entity.getX(), entity.getY(), entity.getZ());
            EntitySpawnS2CPacket pac = new EntitySpawnS2CPacket(lightningEntity);
            pac.apply(mc.getNetworkHandler());
            renderEntities.remove(entity);
            lightingEntities.put(entity, System.currentTimeMillis());
        });
    }

    @EventListener
    public void onTick(RunTickEvent event) {
        if (mc.player == null || mc.world == null) return;

        mc.world.getEntities().forEach(entity -> {
            if (!(entity instanceof PlayerEntity liv)) return;

            if (entity == mc.player || renderEntities.containsKey(entity) || lightingEntities.containsKey(entity)) return;
            if (entity.isAlive() || liv.getHealth() != 0) return;

            renderEntities.put(entity, System.currentTimeMillis());
        });

        if (!lightingEntities.isEmpty()) {
            lightingEntities.forEach((entity, time) -> {
                if (System.currentTimeMillis() - time > 5000) {
                    lightingEntities.remove(entity);
                }
            });
        }
    }
}