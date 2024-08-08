package net.kyc.client.impl.event.network;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.kyc.client.api.event.Event;
import net.kyc.client.mixin.network.MixinClientPlayerEntity;
import net.kyc.client.util.Globals;

/**
 * @author linus
 * @see MixinClientPlayerEntity
 * @since 1.0
 */
public class SetCurrentHandEvent extends Event implements Globals {
    //
    private final Hand hand;

    public SetCurrentHandEvent(Hand hand) {
        this.hand = hand;
    }

    public Hand getHand() {
        return hand;
    }

    public ItemStack getStackInHand() {
        return mc.player.getStackInHand(hand);
    }
}
