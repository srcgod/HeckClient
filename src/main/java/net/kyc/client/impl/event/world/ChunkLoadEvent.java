package net.kyc.client.impl.event.world;

import net.minecraft.util.math.ChunkPos;
import net.kyc.client.api.event.StageEvent;

/**
 * @author linus
 * @since 1.0
 */
public class ChunkLoadEvent extends StageEvent {
    // Chunk position. Needs scaling
    private final ChunkPos pos;

    /**
     * @param pos
     */
    public ChunkLoadEvent(ChunkPos pos) {
        this.pos = pos;
    }

    /**
     * @return
     */
    public ChunkPos getPos() {
        return pos;
    }
}
