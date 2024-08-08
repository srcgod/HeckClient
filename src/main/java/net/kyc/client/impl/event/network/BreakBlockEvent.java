package net.kyc.client.impl.event.network;

import net.minecraft.util.math.BlockPos;
import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

@Cancelable
public class BreakBlockEvent extends Event {
    private final BlockPos pos;

    public BreakBlockEvent(BlockPos pos) {
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }
}
