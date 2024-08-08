package net.kyc.client.impl.gui.hud.HudModules;

import net.kyc.client.api.render.RenderManager;
import net.kyc.client.impl.gui.hud.HudModule;
import net.kyc.client.impl.gui.hud.RenderStage;
import net.kyc.client.impl.module.client.HUDModule;
import net.kyc.client.util.string.EnumFormatter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;

public class DirectionHudModule extends HudModule {

    public DirectionHudModule(HUDModule hudModule) {
        super("Direction",
                2,
                10,
                true,
                hudModule
        );
    }

    @Override
    public void render(RenderStage renderStage, DrawContext drawContext) {
        if (renderStage == RenderStage.TEXT) {
            final Direction direction = mc.player.getHorizontalFacing();
            String dir = EnumFormatter.formatDirection(direction);
            String axis = EnumFormatter.formatAxis(direction.getAxis());
            boolean pos = direction.getDirection() == Direction.AxisDirection.POSITIVE;
            String text = String.format("%s " + Formatting.GRAY + "[" + Formatting.WHITE + "%s%s" + Formatting.GRAY + "]", dir, axis,
                    pos ? "+" : "-");
            RenderManager.renderText(drawContext, text
                , getX(), getY(),
                    hudModule.getHudColor(hudModule.rainbowOffset));
            // bottomLeft -= 9.0f;
            hudModule.rainbowOffset++;

            setWidth(RenderManager.textWidth(text));
            setHeight(mc.textRenderer.fontHeight);
        }
    }
}
