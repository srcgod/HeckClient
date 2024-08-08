package net.kyc.client.impl.gui.hud.HudModules;

import net.kyc.client.api.render.RenderManager;
import net.kyc.client.impl.gui.hud.HudModule;
import net.kyc.client.impl.gui.hud.RenderStage;
import net.kyc.client.impl.module.client.HUDModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

public class TextRadarHudModule extends HudModule {

    public TextRadarHudModule(HUDModule hudModule) {
        super("TextRadar",
                100,
                5,
                true,
                hudModule
        );
    }

    @Override
    public void render(RenderStage renderStage, DrawContext drawContext) {
        if (renderStage == RenderStage.TEXT) {
            int y = 0;
            int maxWidth = 0;
            for (PlayerEntity player : mc.world.getPlayers()) {
                if (player.getDisplayName().getString().equals(mc.player.getDisplayName().getString())) continue;
                String text = player.getDisplayName().getString() + " " + Formatting.GRAY + "[" + Formatting.WHITE + decimal.format(player.distanceTo(mc.player)) + "m." + Formatting.GRAY + "]";

                RenderManager.renderText(drawContext, text, getX(), getY() + y, hudModule.getHudColor(hudModule.rainbowOffset));
                hudModule.rainbowOffset++;

                if (maxWidth < RenderManager.textWidth(text)) {
                    maxWidth = RenderManager.textWidth(text);
                }
                y += mc.textRenderer.fontHeight + 1;
            }

            setWidth(maxWidth);
            setHeight(y);
        }
    }
}
