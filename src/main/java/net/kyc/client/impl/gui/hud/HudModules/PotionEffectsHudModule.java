package net.kyc.client.impl.gui.hud.HudModules;

import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.render.RenderManager;
import net.kyc.client.impl.gui.hud.HudModule;
import net.kyc.client.impl.gui.hud.RenderStage;
import net.kyc.client.impl.module.client.HUDModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PotionEffectsHudModule extends HudModule {

    public PotionEffectsHudModule(HUDModule hudModule) {
        super("PotionEffects",
                50,
                50,
                true,
                hudModule
        );

        configs.add(new BooleanConfig("PotionColors", "Displays active potion colors", true));
    }

    @Override
    public void render(RenderStage renderStage, DrawContext drawContext) {
        if (renderStage == RenderStage.TEXT) {

            for (StatusEffectInstance e : mc.player.getStatusEffects()) {
                final StatusEffect effect = e.getEffectType();
                if (effect == StatusEffects.NIGHT_VISION) {
                    continue;
                }
                boolean amplifier = e.getAmplifier() > 1 && !e.isInfinite();
                Text duration = StatusEffectUtil.getDurationText(e, 1.0f, mc.world.getTickManager().getTickRate());
                String text = String.format("%s %s" + Formatting.WHITE + "%s",
                        effect.getName().getString(),
                        amplifier ? e.getAmplifier() + " " : "",
                        e.isInfinite() ? "" : duration.getString());
                RenderManager.renderText(drawContext, text,
                        getX(), getY(),
                        ((Boolean) getConfig("PotionColors").getValue()) ? effect.getColor() : hudModule.getHudColor(hudModule.rainbowOffset));
                hudModule.rainbowOffset++;

                setWidth(RenderManager.textWidth(text));
                setHeight(mc.textRenderer.fontHeight);
            }
        }
    }
}
