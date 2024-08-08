package net.kyc.client.mixin.gui.hud;

import net.kyc.client.Heckk;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.kyc.client.impl.event.gui.hud.RenderOverlayEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author linus
 * @since 1.0
 */
@Mixin(BossBarHud.class)
public class MixinBossBarHud {
    /**
     * @param context
     * @param ci
     */
    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    private void hookRender(DrawContext context, CallbackInfo ci) {
        RenderOverlayEvent.BossBar renderOverlayEvent =
                new RenderOverlayEvent.BossBar(context);
        Heckk.EVENT_HANDLER.dispatch(renderOverlayEvent);
        if (renderOverlayEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
