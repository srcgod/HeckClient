package net.kyc.client.impl.event;

import net.minecraft.entity.Entity;
import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

@Cancelable
public class EntityOutlineEvent extends Event {
    private final Entity entity;

    public EntityOutlineEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
