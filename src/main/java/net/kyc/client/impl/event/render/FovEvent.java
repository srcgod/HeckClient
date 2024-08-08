package net.kyc.client.impl.event.render;

import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

@Cancelable
public class FovEvent extends Event {
    private double fov;

    public double getFov() {
        return fov;
    }

    public void setFov(double fov) {
        this.fov = fov;
    }
}
