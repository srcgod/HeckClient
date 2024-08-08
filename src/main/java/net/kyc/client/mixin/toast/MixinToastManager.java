package net.kyc.client.mixin.toast;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.ToastManager;
import net.kyc.client.Heckk;
import net.kyc.client.impl.event.toast.RenderToastEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToastManager.class)
public class MixinToastManager {

    /**
     *
     * @param context
     * @param ci
     */
    @Inject(method = "draw", at = @At(value = "HEAD"), cancellable = true)
    private void hookDraw(DrawContext context, CallbackInfo ci) {
        RenderToastEvent renderToastEvent = new RenderToastEvent();
        Heckk.EVENT_HANDLER.dispatch(renderToastEvent);
        if (renderToastEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
