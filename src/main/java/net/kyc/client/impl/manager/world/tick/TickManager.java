package net.kyc.client.impl.manager.world.tick;

import com.google.common.collect.Lists;
import net.kyc.client.Heckk;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.impl.event.network.PacketEvent;
import net.kyc.client.impl.event.render.TickCounterEvent;
import net.kyc.client.util.EvictingQueue;
import net.kyc.client.util.Globals;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * @author linus
 * @since 1.0
 */
public class TickManager implements Globals {

    private final ArrayDeque<Float> ticks = new EvictingQueue<>(20);
    // The TPS tick handler.
    //
    private long time;
    //
    private float clientTick = 1.0f;

    /**
     *
     */
    public TickManager() {
        Heckk.EVENT_HANDLER.subscribe(this);
    }

    /**
     * @param event
     * @see WorldTimeUpdateS2CPacket
     */
    @EventListener
    public void onPacketInbound(PacketEvent.Inbound event) {
        if (mc.player == null || mc.world == null) {
            return;
        }
        // ticks/actual
        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket) {
            float last = 20000.0f / (System.currentTimeMillis() - time);
            ticks.addFirst(last);
            time = System.currentTimeMillis();
        }
    }

    /**
     * @param ticks
     */
    public void setClientTick(float ticks) {
        clientTick = ticks;
    }

    /**
     * @param event
     */
    @EventListener
    public void onTickCounter(TickCounterEvent event) {
        if (clientTick != 1.0f) {
            event.cancel();
            event.setTicks(clientTick);
        }
    }

    /**
     * @return
     */
    public Queue<Float> getTicks() {
        return ticks;
    }

    // So many fucking issues with the EvictingQueue fuck stackoverflow
    // Im just gonna try catch everything atp
    /**
     * @return
     */
    public float getTpsAverage() {
        float avg = 0.0f;
        try {
            // fix ConcurrentModificationException
            ArrayList<Float> ticksCopy = Lists.newArrayList(ticks);
            if (!ticksCopy.isEmpty()) {
                for (float t : ticksCopy) {
                    avg += t;
                }
                avg /= ticksCopy.size();
            }
        } catch (NullPointerException e) {

        }
        return Math.min(100.0f, avg); // Server may compensate
    }

    /**
     * @return
     */
    public float getTpsCurrent() {
        try {
            if (!ticks.isEmpty()) {
                return ticks.getFirst();
            }
        } catch (NoSuchElementException ignored) {

        }
        return 20.0f;
    }

    /**
     * @return
     */
    public float getTpsMin() {
        float min = 20.0f;
        try {
            for (float t : ticks) {
                if (t < min) {
                    min = t;
                }
            }
        } catch (NullPointerException e) {

        }
        return min;
    }

    /**
     * @param tps
     * @return
     */
    public float getTickSync(TickSync tps) {
        return switch (tps) {
            case AVERAGE -> getTpsAverage();
            case CURRENT -> getTpsCurrent();
            case MINIMAL -> getTpsMin();
            case NONE -> 20.0f;
        };
    }
}
