package net.kyc.client.impl.gui.hud.HudModules;

import net.kyc.client.api.render.RenderManager;
import net.kyc.client.impl.gui.hud.HudModule;
import net.kyc.client.impl.gui.hud.RenderStage;
import net.kyc.client.impl.module.client.HUDModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;

public class FPSHudModule extends HudModule {

    public FPSHudModule(HUDModule hudModule) {
        super("FPS",
                2,
                80,
                true,
                hudModule
        );
    }

    @Override
    public void render(RenderStage renderStage, DrawContext drawContext) {
        if (renderStage == RenderStage.TEXT) {
            String text = String.format("FPS " + Formatting.GRAY + "%d", mc.getCurrentFps());
            RenderManager.renderText(drawContext, text,
                    getX(), getY(),
                    hudModule.getHudColor(hudModule.rainbowOffset));
            // bottomRight -= 9.0f;
            hudModule.rainbowOffset++;

            setWidth(RenderManager.textWidth(text));
            setHeight(mc.textRenderer.fontHeight);
        }
    }
}
