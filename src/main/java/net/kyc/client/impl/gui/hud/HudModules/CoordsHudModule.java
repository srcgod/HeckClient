package net.kyc.client.impl.gui.hud.HudModules;

import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.render.RenderManager;
import net.kyc.client.impl.gui.hud.HudModule;
import net.kyc.client.impl.gui.hud.RenderStage;
import net.kyc.client.impl.module.client.HUDModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class CoordsHudModule extends HudModule {

    public CoordsHudModule(HUDModule hudModule) {
        super("Coords",
                2,
                30,
                true,
                hudModule
        );

        configs.add(new BooleanConfig("NetherCoords", "Displays nether coordinates", true, toggleConfig::getValue));
    }

    @Override
    public void render(RenderStage renderStage, DrawContext drawContext) {
        if (renderStage == RenderStage.TEXT) {
            double x = mc.player.getX();
            double y = mc.player.getY();
            double z = mc.player.getZ();
            boolean nether = mc.world.getRegistryKey() == World.NETHER;
            String text = "XYZ " + Formatting.WHITE + (((Boolean) getConfig("NetherCoords").getValue()) ?
                    Formatting.GRAY + "[" + Formatting.WHITE + "%s, %s" + Formatting.GRAY + "]" : "");
            RenderManager.renderText(drawContext, String.format(
                            text,
                            decimal.format(x),
                            decimal.format(y),
                            decimal.format(z),
                            nether ? decimal.format(x * 8) : decimal.format(x / 8),
                            nether ? decimal.format(z * 8) : decimal.format(z / 8)),
                    getX(), getY(), hudModule.getHudColor(hudModule.rainbowOffset));
            hudModule.rainbowOffset++;

            setWidth(RenderManager.textWidth(text));
            setHeight(mc.textRenderer.fontHeight);
        }
    }
}
