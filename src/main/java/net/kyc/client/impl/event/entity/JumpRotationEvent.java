package net.kyc.client.impl.event.entity;

import net.kyc.client.api.event.Event;

public final class JumpRotationEvent extends Event {
    private float yaw;


    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
