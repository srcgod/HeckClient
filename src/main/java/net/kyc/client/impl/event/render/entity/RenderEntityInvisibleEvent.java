package net.kyc.client.impl.event.render.entity;

import net.minecraft.entity.LivingEntity;
import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

@Cancelable
public class RenderEntityInvisibleEvent extends Event {
    private final LivingEntity entity;

    public RenderEntityInvisibleEvent(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }
}
