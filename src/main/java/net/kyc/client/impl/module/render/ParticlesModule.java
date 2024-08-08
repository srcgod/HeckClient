package net.kyc.client.impl.module.render;

import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.config.setting.ColorConfig;
import net.kyc.client.api.config.setting.EnumConfig;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.particle.ParticleEvent;
import net.kyc.client.impl.event.particle.TotemParticleEvent;

import java.awt.*;

/**
 * @author Heckk
 * @since 1.0
 */
public class ParticlesModule extends ToggleModule {

    Config<TotemParticle> totemConfig = new EnumConfig<>("Totem", "Renders totem particles", TotemParticle.OFF, TotemParticle.values());
    Config<Color> totemColorConfig = new ColorConfig("TotemColor", "Color of the totem particles", new Color(25, 120, 0), false, false, () -> totemConfig.getValue() == TotemParticle.COLOR);
    Config<Boolean> fireworkConfig = new BooleanConfig("Firework", "Renders firework particles", false);
    Config<Boolean> potionConfig = new BooleanConfig("Effects", "Renders potion effect particles", true);
    Config<Boolean> bottleConfig = new BooleanConfig("BottleSplash", "Render bottle splash particles", true);
    Config<Boolean> portalConfig = new BooleanConfig("Portal", "Render portal particles", true);

    public ParticlesModule() {
        super("Particles", "Change the rendering of particles", ModuleCategory.RENDER);
    }

    @EventListener
    public void onParticle(ParticleEvent event) {
        if (potionConfig.getValue() && event.getParticleType() == ParticleTypes.ENTITY_EFFECT
                || fireworkConfig.getValue() && event.getParticleType() == ParticleTypes.FIREWORK
                || bottleConfig.getValue() && (event.getParticleType() == ParticleTypes.EFFECT || event.getParticleType() == ParticleTypes.INSTANT_EFFECT)
                || portalConfig.getValue() && event.getParticleType() == ParticleTypes.PORTAL) {
            event.cancel();
        }
    }

    @EventListener
    public void onTotemParticle(TotemParticleEvent event) {
        if (totemConfig.getValue() == TotemParticle.COLOR) {
            event.cancel();
            Color color = totemColorConfig.getValue();
            float r = color.getRed() / 255.0f;
            float g = color.getGreen() / 255.0f;
            float b = color.getBlue() / 255.0f;
            event.setColor(new Color(MathHelper.clamp(r + RANDOM.nextFloat() * 0.1f, 0.0f, 1.0f),
                    MathHelper.clamp(g + RANDOM.nextFloat() * 0.1f, 0.0f, 1.0f),
                    MathHelper.clamp(b + RANDOM.nextFloat() * 0.1f, 0.0f, 1.0f)));
        }
    }

    @EventListener
    public void onParticleEmitter(ParticleEvent.Emitter event) {
        if (totemConfig.getValue() == TotemParticle.REMOVE && event.getParticleType() == ParticleTypes.TOTEM_OF_UNDYING) {
            event.cancel();
        }
    }

    @Override
    public void onUpdate() {

    }

    private enum TotemParticle {
        OFF,
        REMOVE,
        COLOR
    }
}
