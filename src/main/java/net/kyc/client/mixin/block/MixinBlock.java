package net.kyc.client.mixin.block;

import net.kyc.client.Heckk;
import net.minecraft.block.Block;
import net.kyc.client.impl.event.block.BlockSlipperinessEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author linus
 * @since 1.0
 */
@Mixin(Block.class)
public class MixinBlock {
    /**
     * @param cir
     */
    @Inject(method = "getSlipperiness", at = @At(value = "RETURN"),
            cancellable = true)
    private void hookGetSlipperiness(CallbackInfoReturnable<Float> cir) {
        BlockSlipperinessEvent blockSlipperinessEvent =
                new BlockSlipperinessEvent((Block) (Object) this, cir.getReturnValueF());
        Heckk.EVENT_HANDLER.dispatch(blockSlipperinessEvent);
        if (blockSlipperinessEvent.isCanceled()) {
            cir.cancel();
            cir.setReturnValue(blockSlipperinessEvent.getSlipperiness());
        }
    }
}
