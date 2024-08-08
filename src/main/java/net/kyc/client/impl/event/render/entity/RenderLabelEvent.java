package net.kyc.client.impl.event.render.entity;

import net.minecraft.entity.Entity;
import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

/**
 * @author linus
 * @since 1.0
 */
@Cancelable
public class RenderLabelEvent extends Event {
    private final Entity entity;

    public RenderLabelEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
