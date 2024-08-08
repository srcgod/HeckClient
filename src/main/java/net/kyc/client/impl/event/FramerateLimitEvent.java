package net.kyc.client.impl.event;

import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

/**
 *
 */
@Cancelable
public class FramerateLimitEvent extends Event {
    private int framerateLimit;

    public int getFramerateLimit() {
        return framerateLimit;
    }

    public void setFramerateLimit(int framerateLimit) {
        this.framerateLimit = framerateLimit;
    }
}
