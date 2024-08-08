package net.kyc.client.impl.module.misc;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.MouseClickEvent;
import net.kyc.client.init.Managers;
import org.lwjgl.glfw.GLFW;

/**
 * @author linus
 * @since 1.0
 */
public class MiddleClickModule extends ToggleModule {

    //
    Config<Boolean> friendConfig = new BooleanConfig("Friend", "Friends players when middle click", true);
    Config<Boolean> pearlConfig = new BooleanConfig("Pearl", "Throws a pearl when middle click", true);
    Config<Boolean> fireworkConfig = new BooleanConfig("Firework", "Uses firework to boost elytra when middle click", false);

    /**
     *
     */
    public MiddleClickModule() {
        super("MiddleClick", "Adds an additional bind on the mouse middle button",
                ModuleCategory.MISC);
    }

    @EventListener
    public void onMouseClick(MouseClickEvent event) {
        if (mc.player == null || mc.interactionManager == null) {
            return;
        }
        if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_MIDDLE
                && event.getAction() == GLFW.GLFW_PRESS && mc.currentScreen == null) {
            if (mc.targetedEntity instanceof PlayerEntity target && target.getDisplayName() != null && friendConfig.getValue()) {
                if (Managers.SOCIAL.isFriend(target.getDisplayName())) {
                    Managers.SOCIAL.remove(target.getDisplayName());
                } else {
                    Managers.SOCIAL.addFriend(target.getDisplayName());
                }
            } else {
                Item item = null;
                if (mc.player.isFallFlying() && fireworkConfig.getValue()) {
                    item = Items.FIREWORK_ROCKET;
                } else if (pearlConfig.getValue()) {
                    item = Items.ENDER_PEARL;
                }
                if (item == null) {
                    return;
                }
                int slot = -1;
                for (int i = 0; i < 9; i++) {
                    ItemStack stack = mc.player.getInventory().getStack(i);
                    if (stack.getItem() == item) {
                        slot = i;
                        break;
                    }
                }
                if (slot != -1) {
                    int prev = mc.player.getInventory().selectedSlot;
                    Managers.INVENTORY.setClientSlot(slot);
                    mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                    Managers.INVENTORY.setClientSlot(prev);
                }
            }
        }
    }

    @Override
    public void onUpdate() {

    }
}
