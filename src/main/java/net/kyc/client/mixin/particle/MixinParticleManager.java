package net.kyc.client.mixin.particle;

import net.kyc.client.Heckk;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.kyc.client.impl.event.particle.ParticleEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author linus
 * @since 1.0
 */
@Mixin(ParticleManager.class)
public class MixinParticleManager {
    /**
     * @param parameters
     * @param x
     * @param y
     * @param z
     * @param velocityX
     * @param velocityY
     * @param velocityZ
     * @param cir
     */
    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;" +
            "DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At(value =
            "HEAD"), cancellable = true)
    private void hookAddParticle(ParticleEffect parameters, double x,
                                 double y, double z, double velocityX,
                                 double velocityY, double velocityZ,
                                 CallbackInfoReturnable<Particle> cir) {
        ParticleEvent particleEvent = new ParticleEvent(parameters);
        Heckk.EVENT_HANDLER.dispatch(particleEvent);
        if (particleEvent.isCanceled()) {
            cir.setReturnValue(null);
            cir.cancel();
        }
    }

    /**
     * @param entity
     * @param parameters
     * @param maxAge
     * @param ci
     */
    @Inject(method = "addEmitter(Lnet/minecraft/entity/Entity;Lnet/minecraft" +
            "/particle/ParticleEffect;I)V", at = @At(value = "HEAD"), cancellable = true)
    private void hookAddEmitter(Entity entity, ParticleEffect parameters,
                                int maxAge, CallbackInfo ci) {
        ParticleEvent.Emitter particleEvent =
                new ParticleEvent.Emitter(parameters);
        Heckk.EVENT_HANDLER.dispatch(particleEvent);
        if (particleEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
