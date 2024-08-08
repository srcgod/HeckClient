package net.kyc.client.impl.event;

import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;
import net.kyc.client.mixin.MixinMinecraftClient;

/**
 * Allows mining and eating at the same time
 *
 * @see MixinMinecraftClient
 */
@Cancelable
public class ItemMultitaskEvent extends Event {

}
