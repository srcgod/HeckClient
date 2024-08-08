package net.kyc.client.impl.event.render;

import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

/**
 * @author linus
 * @since 1.0
 */
@Cancelable
public class TickCounterEvent extends Event {
    //
    private float ticks;

    /**
     * @return
     */
    public float getTicks() {
        return ticks;
    }

    /**
     * @param ticks
     */
    public void setTicks(float ticks) {
        this.ticks = ticks;
    }
}
