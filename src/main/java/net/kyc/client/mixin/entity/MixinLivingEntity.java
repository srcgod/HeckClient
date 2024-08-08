package net.kyc.client.mixin.entity;

import net.kyc.client.Heckk;
import net.kyc.client.init.Modules;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.kyc.client.impl.event.entity.*;
import net.kyc.client.util.Globals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author linus
 * @since 1.0
 */
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends MixinEntity implements Globals {
    //
    @Shadow
    protected ItemStack activeItemStack;

    /**
     * @param effect
     * @return
     */
    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow
    public abstract float getYaw(float tickDelta);

    @Shadow
    protected abstract float getJumpVelocity();

    @Shadow
    public abstract boolean isDead();

    @Shadow
    public int deathTime;

    @Shadow private int jumpingCooldown;

    @Inject(method = "jump", at = @At(value = "HEAD"), cancellable = true)
    private void hookJump$getYaw(CallbackInfo ci) {
        if ((LivingEntity) (Object) this != mc.player) {
            return;
        }
        final JumpRotationEvent event = new JumpRotationEvent();
        Heckk.EVENT_HANDLER.dispatch(event);
        if (event.isCanceled()) {
            ci.cancel();
            Vec3d vec3d = this.getVelocity();
            setVelocity(new Vec3d(vec3d.x, getJumpVelocity(), vec3d.z));
            if (isSprinting()) {
                float f = event.getYaw() * ((float)Math.PI / 180);
                setVelocity(getVelocity().add(-MathHelper.sin(f) * 0.2f, 0.0, MathHelper.cos(f) * 0.2f));
            }
            velocityDirty = true;
        }
    }

    @Inject(method = "getHandSwingDuration", at = {@At("HEAD")}, cancellable = true)
    private void getArmSwingAnimationEnd(final CallbackInfoReturnable<Integer> info) {
        if (Modules.ANIMATIONS.shouldChangeAnimationDuration() && Modules.ANIMATIONS.slowAnimation.getValue())
            info.setReturnValue(Modules.ANIMATIONS.slowAnimationVal.getValue());
    }

    /**
     * @param instance
     * @param effect
     * @return
     */
    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/" +
            "minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/" +
            "entity/effect/StatusEffect;)Z"))
    private boolean hookHasStatusEffect(LivingEntity instance, StatusEffect effect) {
        if (instance.equals(mc.player)) {
            LevitationEvent levitationEvent = new LevitationEvent();
            Heckk.EVENT_HANDLER.dispatch(levitationEvent);
            return !levitationEvent.isCanceled() && hasStatusEffect(effect);
        }
        return hasStatusEffect(effect);
    }

    /**
     * @param ci
     */
    @Inject(method = "consumeItem", at = @At(value = "INVOKE", target = "Lnet/" +
            "minecraft/item/ItemStack;finishUsing(Lnet/minecraft/world/World;" +
            "Lnet/minecraft/entity/LivingEntity;)" +
            "Lnet/minecraft/item/ItemStack;", shift = At.Shift.AFTER))
    private void hookConsumeItem(CallbackInfo ci) {
        if ((Object) this != mc.player) {
            return;
        }
        ConsumeItemEvent consumeItemEvent = new ConsumeItemEvent(activeItemStack);
        Heckk.EVENT_HANDLER.dispatch(consumeItemEvent);
    }

    @Inject(method = "tickMovement", at = @At(value = "HEAD"), cancellable = true)
    private void hookTickMovement(CallbackInfo ci) {
        JumpDelayEvent jumpDelayEvent = new JumpDelayEvent();
        Heckk.EVENT_HANDLER.dispatch(jumpDelayEvent);
        if (jumpDelayEvent.isCanceled()) {
            jumpingCooldown = 0;
        }
    }
}
