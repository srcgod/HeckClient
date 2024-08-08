package net.kyc.client.impl.gui.hud.HudModules;

import net.kyc.client.HeckMod;
import net.kyc.client.api.render.RenderManager;
import net.kyc.client.impl.gui.hud.HudModule;
import net.kyc.client.impl.gui.hud.RenderStage;
import net.kyc.client.impl.module.client.HUDModule;
import net.minecraft.client.gui.DrawContext;

import static net.kyc.client.HeckMod.finaluid;

public class WatermarkHudModule extends HudModule {

    public WatermarkHudModule(HUDModule hudModule) {
        super("Watermark",
                2,
                2,
                true,
                hudModule
        );
    }

    @Override
    public void render(RenderStage renderStage, DrawContext drawContext) {
        if (renderStage == RenderStage.TEXT) {
            String text = String.format("%s %s %s%s",
                    HeckMod.MOD_NAME, HeckMod.MOD_VER,
                    "+ UID", ": " + finaluid);

            RenderManager.renderText(drawContext, text , getX(), getY(), hudModule.getHudColor(hudModule.rainbowOffset));

            setWidth(RenderManager.textWidth(text));
            setHeight(mc.textRenderer.fontHeight);
        }
    }
}
