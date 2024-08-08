package net.kyc.client.mixin.render;

import net.kyc.client.Heckk;
import net.kyc.client.init.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.kyc.client.impl.event.network.ReachEvent;
import net.kyc.client.impl.event.render.*;
import net.kyc.client.impl.event.world.UpdateCrosshairTargetEvent;
import net.kyc.client.init.Programs;
import net.kyc.client.util.Globals;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author linus
 * @see GameRenderer
 * @since 1.0
 */
@Mixin(GameRenderer.class)
public class MixinGameRenderer implements Globals {
    //
    @Shadow
    @Final
    MinecraftClient client;

    @Shadow
    private float lastFovMultiplier;

    @Shadow
    private float fovMultiplier;

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 1))
    private void hookRenderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci) {
        RenderWorldEvent.Game renderWorldEvent = new RenderWorldEvent.Game(matrices, tickDelta);
        Heckk.EVENT_HANDLER.dispatch(renderWorldEvent);
    }

    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = Opcodes.GETFIELD, ordinal = 0), method = "renderWorld")
    void render3dHook(float tickDelta, long limitTime, @NotNull MatrixStack matrix, CallbackInfo ci) {
        if (Modules.POP_CHAMS.isEnabled()) Modules.POP_CHAMS.onRender3D(matrix);
    }

    @Inject(method = "updateTargetedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void hookUpdateTargetedEntity$1(final float tickDelta, final CallbackInfo info) {
        UpdateCrosshairTargetEvent event = new UpdateCrosshairTargetEvent(tickDelta, client.getCameraEntity());
        Heckk.EVENT_HANDLER.dispatch(event);
    }

    /**
     * @param matrices
     * @param tickDelta
     * @param ci
     */
    @Inject(method = "tiltViewWhenHurt", at = @At(value = "HEAD"),
            cancellable = true)
    private void hookTiltViewWhenHurt(MatrixStack matrices, float tickDelta,
                                      CallbackInfo ci) {
        HurtCamEvent hurtCamEvent = new HurtCamEvent();
        Heckk.EVENT_HANDLER.dispatch(hurtCamEvent);
        if (hurtCamEvent.isCanceled()) {
            ci.cancel();
        }
    }

    /**
     * @param floatingItem
     * @param ci
     */
    @Inject(method = "showFloatingItem", at = @At(value = "HEAD"),
            cancellable = true)
    private void hookShowFloatingItem(ItemStack floatingItem, CallbackInfo ci) {
        RenderFloatingItemEvent renderFloatingItemEvent =
                new RenderFloatingItemEvent(floatingItem);
        Heckk.EVENT_HANDLER.dispatch(renderFloatingItemEvent);
        if (renderFloatingItemEvent.isCanceled()) {
            ci.cancel();
        }
    }

    /**
     * @param distortionStrength
     * @param ci
     */
    @Inject(method = "renderNausea", at = @At(value = "HEAD"), cancellable = true)
    private void hookRenderNausea(DrawContext context, float distortionStrength, CallbackInfo ci) {
        RenderNauseaEvent renderNauseaEvent = new RenderNauseaEvent();
        Heckk.EVENT_HANDLER.dispatch(renderNauseaEvent);
        if (renderNauseaEvent.isCanceled()) {
            ci.cancel();
        }
    }

    /**
     * @param cir
     */
    @Inject(method = "shouldRenderBlockOutline", at = @At(value = "HEAD"),
            cancellable = true)
    private void hookShouldRenderBlockOutline(CallbackInfoReturnable<Boolean> cir) {
        RenderBlockOutlineEvent renderBlockOutlineEvent =
                new RenderBlockOutlineEvent();
        Heckk.EVENT_HANDLER.dispatch(renderBlockOutlineEvent);
        if (renderBlockOutlineEvent.isCanceled()) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    /**
     * @param tickDelta
     * @param info
     */
    @Inject(method = "updateTargetedEntity", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast" +
                    "(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/" +
                    "Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/" +
                    "math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/" +
                    "util/hit/EntityHitResult;"), cancellable = true)
    private void hookUpdateTargetedEntity$2(float tickDelta, CallbackInfo info) {
        TargetEntityEvent targetEntityEvent = new TargetEntityEvent();
        Heckk.EVENT_HANDLER.dispatch(targetEntityEvent);
        if (targetEntityEvent.isCanceled() && client.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            client.getProfiler().pop();
            info.cancel();
        }
    }

    /**
     * @param d
     * @return
     */
    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 9))
    private double updateTargetedEntityModifySquaredMaxReach(double d) {
        ReachEvent reachEvent = new ReachEvent();
        Heckk.EVENT_HANDLER.dispatch(reachEvent);
        double reach = reachEvent.getReach() + 3.0;
        return reachEvent.isCanceled() ? reach * reach : 9.0;
    }


    /**
     * @param matrices
     * @param tickDelta
     * @param ci
     */
    @Inject(method = "bobView", at = @At(value = "HEAD"), cancellable = true)
    private void hookBobView(MatrixStack matrices, float tickDelta,
                             CallbackInfo ci) {
        BobViewEvent bobViewEvent = new BobViewEvent();
        Heckk.EVENT_HANDLER.dispatch(bobViewEvent);
        if (bobViewEvent.isCanceled()) {
            ci.cancel();
        }
    }

    /**
     *
     * @param camera
     * @param tickDelta
     * @param changingFov
     * @param cir
     */
    @Inject(method = "getFov", at = @At(value = "HEAD"), cancellable = true)
    private void hookGetFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        FovEvent fovEvent = new FovEvent();
        Heckk.EVENT_HANDLER.dispatch(fovEvent);
        if (fovEvent.isCanceled()) {
            cir.cancel();
            cir.setReturnValue(fovEvent.getFov() * (double) MathHelper.lerp(tickDelta, lastFovMultiplier, fovMultiplier));
        }
    }

    /**
     * @param factory
     * @param ci
     */
    @Inject(method = "loadPrograms", at = @At(value = "INVOKE",
            target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
            ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void initPrograms(ResourceFactory factory, CallbackInfo ci) {
        Programs.initPrograms();
    }
}
