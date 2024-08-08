package net.kyc.client.impl.event.render.entity;

import net.minecraft.entity.LivingEntity;
import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

/**
 * @author linus
 * @since 1.0
 */
@Cancelable
public class RenderArmorEvent extends Event {
    private final LivingEntity entity;

    public RenderArmorEvent(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }
}
