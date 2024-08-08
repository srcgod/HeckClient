package net.kyc.client.impl.gui.hud.HudModules;

import net.kyc.client.impl.gui.hud.HudModule;
import net.kyc.client.impl.gui.hud.RenderStage;
import net.kyc.client.impl.module.client.HUDModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class ArmorHudModule extends HudModule {

    public ArmorHudModule(HUDModule hudModule) {
        super("Armor",
                200,
                200,
                true,
                hudModule
        );
    }

    @Override
    public void render(RenderStage renderStage, DrawContext drawContext) {
        if (renderStage == RenderStage.IMAGE) {
            final Entity riding = mc.player.getVehicle();
            //
            int x = getX();
            int y = getY();
            int n1 = mc.player.getMaxAir();
            int n2 = Math.min(mc.player.getAir(), n1);
            /*                  no, please...
            if (mc.player.isSubmergedIn(FluidTags.WATER) || n2 < n1) {
                y -= 65;
            } else if (riding instanceof LivingEntity entity) {
                y -= 45 + (int) Math.ceil((entity.getMaxHealth() - 1.0f) / 20.0f) * 10;
            } else if (riding != null) {
                y -= 45;
            } else {
                y -= mc.player.isCreative() ?
                        (mc.player.isRiding() ? 45 : 38) : 55;
            }

             */
            for (int i = 3; i >= 0; --i) {
                ItemStack armor = mc.player.getInventory().armor.get(i);
                drawContext.drawItem(armor, x, y);
                drawContext.drawItemInSlot(mc.textRenderer, armor, x, y);
                x += 18;
            }

            setWidth(80);
            setHeight(25);
        }
    }
}
