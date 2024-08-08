package net.kyc.client.impl.event.world;

import net.kyc.client.api.event.Event;
import net.minecraft.entity.player.PlayerEntity;

public class TotemPopEvent extends Event {
    private final PlayerEntity entity;

    public TotemPopEvent(PlayerEntity entity) {
        this.entity = entity;
    }

    public PlayerEntity getEntity() {
        return this.entity;
    }
}