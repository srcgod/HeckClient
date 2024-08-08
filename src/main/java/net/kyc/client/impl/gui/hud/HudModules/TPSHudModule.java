package net.kyc.client.impl.gui.hud.HudModules;

import net.kyc.client.api.render.RenderManager;
import net.kyc.client.impl.gui.hud.HudModule;
import net.kyc.client.impl.gui.hud.RenderStage;
import net.kyc.client.impl.module.client.HUDModule;
import net.kyc.client.init.Managers;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;

public class TPSHudModule extends HudModule {

    public TPSHudModule(HUDModule hudModule) {
        super("TPS",
                2,
                70,
                true,
                hudModule
        );
    }

    @Override
    public void render(RenderStage renderStage, DrawContext drawContext) {
        if (renderStage == RenderStage.TEXT) {
            float curr = Managers.TICK.getTpsCurrent();
            float avg = Managers.TICK.getTpsAverage();
            String text = String.format("TPS " + Formatting.WHITE + "%s " + Formatting.GRAY + "[" + Formatting.WHITE + "%s" + Formatting.GRAY + "]",
                    decimal.format(avg),
                    decimal.format(curr));
            RenderManager.renderText(drawContext, text,
                    getX(), getY(),
                    hudModule.getHudColor(hudModule.rainbowOffset));
            hudModule.rainbowOffset++;

            setWidth(RenderManager.textWidth(text));
            setHeight(mc.textRenderer.fontHeight);
        }
    }
}
