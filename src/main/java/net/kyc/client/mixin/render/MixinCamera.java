package net.kyc.client.mixin.render;

import net.kyc.client.Heckk;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.kyc.client.impl.event.camera.CameraPositionEvent;
import net.kyc.client.impl.event.camera.CameraRotationEvent;
import net.kyc.client.impl.event.gui.hud.RenderOverlayEvent;
import net.kyc.client.impl.event.render.CameraClipEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author linus
 * @since 1.0
 */
@Mixin(Camera.class)
public abstract class MixinCamera {
    @Shadow private float lastTickDelta;

    @Shadow protected abstract void setPos(double x, double y, double z);

    @Shadow protected abstract void setRotation(float yaw, float pitch);

    /**
     * @param cir
     */
    @Inject(method = "getSubmersionType", at = @At(value = "HEAD"),
            cancellable = true)
    private void hookGetSubmersionType(CallbackInfoReturnable<CameraSubmersionType> cir) {
        RenderOverlayEvent.Water renderOverlayEvent =
                new RenderOverlayEvent.Water(null);
        Heckk.EVENT_HANDLER.dispatch(renderOverlayEvent);
        if (renderOverlayEvent.isCanceled()) {
            cir.setReturnValue(CameraSubmersionType.NONE);
            cir.cancel();
        }
    }

    /**
     * @param desiredCameraDistance
     * @param cir
     */
    @Inject(method = "clipToSpace", at = @At(value = "HEAD"), cancellable = true)
    private void hookClipToSpace(double desiredCameraDistance,
                                 CallbackInfoReturnable<Double> cir) {
        CameraClipEvent cameraClipEvent =
                new CameraClipEvent(desiredCameraDistance);
        Heckk.EVENT_HANDLER.dispatch(cameraClipEvent);
        if (cameraClipEvent.isCanceled()) {
            cir.setReturnValue(cameraClipEvent.getDistance());
            cir.cancel();
        }
    }

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
    private void hookUpdatePosition(Camera instance, double x, double y, double z) {
        CameraPositionEvent cameraPositionEvent = new CameraPositionEvent(x, y, z, lastTickDelta);
        Heckk.EVENT_HANDLER.dispatch(cameraPositionEvent);
        setPos(cameraPositionEvent.getX(), cameraPositionEvent.getY(), cameraPositionEvent.getZ());

    }

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
    private void hookUpdateRotation(Camera instance, float yaw, float pitch) {
        CameraRotationEvent cameraRotationEvent = new CameraRotationEvent(yaw, pitch, lastTickDelta);
        Heckk.EVENT_HANDLER.dispatch(cameraRotationEvent);
        setRotation(cameraRotationEvent.getYaw(), cameraRotationEvent.getPitch());
    }
}
