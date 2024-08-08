package net.kyc.client.impl.event.entity.decoration;

import net.minecraft.entity.Entity;
import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

/**
 *
 */
@Cancelable
public class TeamColorEvent extends Event {
    private final Entity entity;
    private int color;

    public TeamColorEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
