package net.kyc.client.impl.event.particle;

import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

import java.awt.*;

@Cancelable
public class TotemParticleEvent extends Event {
    private Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
