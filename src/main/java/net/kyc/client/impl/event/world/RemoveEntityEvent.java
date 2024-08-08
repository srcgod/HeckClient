package net.kyc.client.impl.event.world;

import net.minecraft.entity.Entity;
import net.kyc.client.api.event.Event;
import net.kyc.client.util.Globals;

public class RemoveEntityEvent extends Event implements Globals {
    private final Entity entity;
    private final Entity.RemovalReason removalReason;

    public RemoveEntityEvent(Entity entity, Entity.RemovalReason removalReason) {
        this.entity = entity;
        this.removalReason = removalReason;
    }

    /**
     * @return
     */
    public Entity getEntity() {
        return entity;
    }

    public Entity.RemovalReason getRemovalReason() {
        return removalReason;
    }
}
