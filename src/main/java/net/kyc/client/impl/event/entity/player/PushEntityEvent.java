package net.kyc.client.impl.event.entity.player;

import net.minecraft.entity.Entity;
import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

/**
 * @author linus
 * @since 1.0
 */
@Cancelable
public class PushEntityEvent extends Event {
    private final Entity pushed, pusher;

    public PushEntityEvent(Entity pushed, Entity pusher) {
        this.pushed = pushed;
        this.pusher = pusher;
    }

    public Entity getPushed() {
        return pushed;
    }

    public Entity getPusher() {
        return pusher;
    }
}
