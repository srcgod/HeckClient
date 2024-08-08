package net.kyc.client.mixin.render.entity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.WitherSkullEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.kyc.client.Heckk;
import net.kyc.client.impl.event.render.entity.RenderWitherSkullEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author linus
 * @since 1.0
 */
@Mixin(WitherSkullEntityRenderer.class)
public class MixinWitherSkullEntityRenderer {
    /**
     * @param witherSkullEntity
     * @param f
     * @param g
     * @param matrixStack
     * @param vertexConsumerProvider
     * @param i
     */
    @Inject(method = "render(Lnet/minecraft/entity/projectile" +
            "/WitherSkullEntity;FFLnet/minecraft/client/util/math" +
            "/MatrixStack;Lnet/minecraft/client/render/" +
            "VertexConsumerProvider;I)V", at = @At(value = "HEAD"), cancellable = true)
    private void hookRender(WitherSkullEntity witherSkullEntity, float f,
                            float g, MatrixStack matrixStack,
                            VertexConsumerProvider vertexConsumerProvider,
                            int i, CallbackInfo ci) {
        RenderWitherSkullEvent renderWitherSkullEvent =
                new RenderWitherSkullEvent();
        Heckk.EVENT_HANDLER.dispatch(renderWitherSkullEvent);
        if (renderWitherSkullEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
