package net.kyc.client.impl.event.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

/**
 * @author linus
 * @since 1.0
 */
@Cancelable
public class VelocityMultiplierEvent extends Event {
    //
    private final BlockState state;

    /**
     * @param state
     */
    public VelocityMultiplierEvent(BlockState state) {
        this.state = state;
    }

    /**
     * @return
     */
    public Block getBlock() {
        return state.getBlock();
    }
}
