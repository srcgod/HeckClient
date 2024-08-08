package net.kyc.client.mixin.particle;

import net.kyc.client.Heckk;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.particle.TotemParticle;
import net.minecraft.client.world.ClientWorld;
import net.kyc.client.impl.event.particle.TotemParticleEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(TotemParticle.class)
public abstract class MixinTotemParticle extends MixinParticle {

    /**
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @param velocityX
     * @param velocityY
     * @param velocityZ
     * @param spriteProvider
     * @param ci
     */
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void hookInit(ClientWorld world, double x, double y, double z, double velocityX,
                          double velocityY, double velocityZ, SpriteProvider spriteProvider, CallbackInfo ci) {
        TotemParticleEvent totemParticleEvent = new TotemParticleEvent();
        Heckk.EVENT_HANDLER.dispatch(totemParticleEvent);
        if (totemParticleEvent.isCanceled()) {
            Color color = totemParticleEvent.getColor();
            setColor(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f);
        }
    }
}
