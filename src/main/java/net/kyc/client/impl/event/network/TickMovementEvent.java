package net.kyc.client.impl.event.network;

import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

/**
 * @author linus
 * @since 1.0
 */
@Cancelable
public class TickMovementEvent extends Event {
    //
    private int iterations;

    /**
     * @return
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * @param iterations
     */
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }
}
