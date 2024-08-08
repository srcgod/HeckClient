package net.kyc.client.impl.event.entity;

import net.minecraft.block.BlockState;
import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

@Cancelable
public class SlowMovementEvent extends Event {
    private final BlockState state;

    public SlowMovementEvent(BlockState state) {
        this.state = state;
    }

    public BlockState getState() {
        return state;
    }
}
