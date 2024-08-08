package net.kyc.client.impl.gui.hud.HudModules;

import net.kyc.client.api.render.RenderManager;
import net.kyc.client.impl.gui.hud.HudModule;
import net.kyc.client.impl.gui.hud.RenderStage;
import net.kyc.client.impl.module.client.HUDModule;
import net.kyc.client.init.Modules;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;

public class SpeedHudModule extends HudModule {

    public SpeedHudModule(HUDModule hudModule) {
        super("Speed",
                2,
                50,
                true,
                hudModule
        );
    }

    @Override
    public void render(RenderStage renderStage, DrawContext drawContext) {
        if (renderStage == RenderStage.TEXT) {
            double x = mc.player.getX() - mc.player.prevX;
            // double y = mc.player.getY() - mc.player.prevY;
            double z = mc.player.getZ() - mc.player.prevZ;
            double dist = Math.sqrt(x * x + z * z) / 1000.0;
            double div = 0.05 / 3600.0;
            float timer = Modules.TIMER.isEnabled() ? Modules.TIMER.getTimer() : 1.0f;
            final double speed = dist / div * timer;
            String text = String.format("Speed " + Formatting.WHITE + "%skm/h",
                    decimal.format(speed));
            RenderManager.renderText(drawContext, text,
                    getX(), getY(),
                    hudModule.getHudColor(hudModule.rainbowOffset));
            hudModule.rainbowOffset++;

            setWidth(RenderManager.textWidth(text));
            setHeight(mc.textRenderer.fontHeight);
        }
    }
}
