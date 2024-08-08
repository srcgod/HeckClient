package net.kyc.client.impl.gui.hud.HudModules;

import net.kyc.client.api.render.RenderManager;
import net.kyc.client.impl.gui.hud.HudModule;
import net.kyc.client.impl.gui.hud.RenderStage;
import net.kyc.client.impl.module.client.HUDModule;
import net.kyc.client.init.Managers;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;

public class PingHudModule extends HudModule {
    public PingHudModule(HUDModule hudModule) {
        super("Ping",
                2,
                60,
                true,
                hudModule
        );
    }

    @Override
    public void render(RenderStage renderStage, DrawContext drawContext) {
        if (renderStage == RenderStage.TEXT && !mc.isInSingleplayer()) {
            int latency = Managers.NETWORK.getClientLatency();
            String text = String.format("Ping " + Formatting.WHITE + "%dms", latency);
            RenderManager.renderText(drawContext, text,
                    getX(), getY(),
                    hudModule.getHudColor(hudModule.rainbowOffset));
            hudModule.rainbowOffset++;

            setWidth(RenderManager.textWidth(text));
            setHeight(mc.textRenderer.fontHeight);
        }
    }
}
