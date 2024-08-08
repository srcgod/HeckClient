package net.kyc.client.impl.event.network;

import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

@Cancelable
public class MountJumpStrengthEvent extends Event {
    //
    private float jumpStrength;

    public float getJumpStrength() {
        return jumpStrength;
    }

    public void setJumpStrength(float jumpStrength) {
        this.jumpStrength = jumpStrength;
    }
}
