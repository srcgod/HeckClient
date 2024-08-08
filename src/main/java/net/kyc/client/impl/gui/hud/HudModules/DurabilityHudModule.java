package net.kyc.client.impl.gui.hud.HudModules;

import net.kyc.client.api.render.RenderManager;
import net.kyc.client.impl.gui.hud.HudModule;
import net.kyc.client.impl.gui.hud.RenderStage;
import net.kyc.client.impl.module.client.HUDModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;

public class DurabilityHudModule extends HudModule {

    public DurabilityHudModule(HUDModule hudModule) {
        super("Durability",
                2,
                20,
                true,
                hudModule
        );
    }

    @Override
    public void render(RenderStage renderStage, DrawContext drawContext) {
        if (renderStage == RenderStage.TEXT && mc.player.getMainHandStack().isDamageable()) {
            int n = mc.player.getMainHandStack().getMaxDamage();
            int n2 = mc.player.getMainHandStack().getDamage();
            String text1 = "Durability " + Formatting.WHITE + (n - n2);
            //Color color = ColorUtil.hslToColor((float) (n - n2) / (float) n * 120.0f, 100.0f, 50.0f, 1.0f);
            RenderManager.renderText(drawContext, text1,
                    getX(), getY(),
                    hudModule.getHudColor(hudModule.rainbowOffset));
            hudModule.rainbowOffset++;

            setWidth(RenderManager.textWidth(text1));
            setHeight(mc.textRenderer.fontHeight);
        }
    }
}
