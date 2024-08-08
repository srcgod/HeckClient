package net.kyc.client.impl.event;

import net.minecraft.client.render.Camera;
import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

@Cancelable
public class PerspectiveEvent extends Event {

    public Camera camera;

    public PerspectiveEvent(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }

}
