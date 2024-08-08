package net.kyc.client.impl.event.entity.projectile;

import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

@Cancelable
public class RemoveFireworkEvent extends Event {
    private final FireworkRocketEntity rocketEntity;

    public RemoveFireworkEvent(FireworkRocketEntity rocketEntity) {
        this.rocketEntity = rocketEntity;
    }

    public FireworkRocketEntity getRocketEntity() {
        return rocketEntity;
    }
}
