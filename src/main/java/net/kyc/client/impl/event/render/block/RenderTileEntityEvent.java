package net.kyc.client.impl.event.render.block;

import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

/**
 * @author linus
 * @since 1.0
 */
public class RenderTileEntityEvent extends Event {
    @Cancelable
    public static class EnchantingTableBook extends RenderTileEntityEvent {

    }
}
