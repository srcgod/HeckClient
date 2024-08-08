package net.kyc.client.impl.gui.hud.HudModules;

import net.kyc.client.api.render.RenderManager;
import net.kyc.client.impl.gui.hud.HudModule;
import net.kyc.client.impl.gui.hud.RenderStage;
import net.kyc.client.impl.module.client.HUDModule;
import net.minecraft.client.gui.DrawContext;

public class ServerBrandHudModule extends HudModule {

    public ServerBrandHudModule(HUDModule hudModule) {
        super("ServerBrand",
                2,
                40,
                true,
                hudModule
        );
    }

    @Override
    public void render(RenderStage renderStage, DrawContext drawContext) {
        if (renderStage == RenderStage.TEXT && mc.getServer() != null) {
            String brand = mc.getServer().getVersion();
            RenderManager.renderText(drawContext, brand,
                    getX(), getY(),
                    hudModule.getHudColor(hudModule.rainbowOffset));
            hudModule.rainbowOffset++;

            setWidth(RenderManager.textWidth(brand));
            setHeight(mc.textRenderer.fontHeight);
        }
    }
}
