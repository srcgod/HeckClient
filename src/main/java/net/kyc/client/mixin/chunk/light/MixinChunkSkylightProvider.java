package net.kyc.client.mixin.chunk.light;

import net.kyc.client.Heckk;
import net.minecraft.world.chunk.light.ChunkSkyLightProvider;
import net.kyc.client.impl.event.chunk.light.RenderSkylightEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author linus
 * @see ChunkSkyLightProvider
 * @since 1.0
 */
@Mixin(ChunkSkyLightProvider.class)
public class MixinChunkSkylightProvider {
    /**
     * @param blockPos
     * @param l
     * @param lightLevel
     * @param ci
     */
    @Inject(method = "method_51531", at = @At(value = "HEAD"), cancellable = true)
    private void hookRecalculateLevel(long blockPos, long l, int lightLevel, CallbackInfo ci) {
        RenderSkylightEvent renderSkylightEvent = new RenderSkylightEvent();
        Heckk.EVENT_HANDLER.dispatch(renderSkylightEvent);
        if (renderSkylightEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
