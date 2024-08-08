package net.kyc.client.impl.module.misc;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.TickEvent;
import net.kyc.client.init.Managers;
import net.kyc.client.mixin.accessor.AccessorKeyBinding;

/**
 * @author linus
 * @since 1.0
 */
public class AutoEatModule extends ToggleModule {
    //
    Config<Float> hungerConfig = new NumberConfig<>("Hunger", "The minimum hunger level before eating", 1.0f, 19.0f, 20.0f);
    //
    private int prevSlot;

    /**
     *
     */
    public AutoEatModule() {
        super("AutoEat", "Automatically eats when losing hunger",
                ModuleCategory.MISC);
    }

    @Override
    public void onEnable() {
        prevSlot = -1;
    }

    @Override
    public void onDisable() {
        KeyBinding.setKeyPressed(((AccessorKeyBinding) mc.options.useKey).getBoundKey(), false);
    }

    @Override
    public void onUpdate() {

    }

    @EventListener
    public void onTick(TickEvent event) {
        if (!mc.player.isUsingItem()) {
            if (prevSlot != -1) {
                Managers.INVENTORY.setClientSlot(prevSlot);
                prevSlot = -1;
            }
            KeyBinding.setKeyPressed(((AccessorKeyBinding) mc.options.useKey).getBoundKey(), false);
            return;
        }
        //
        HungerManager hungerManager = mc.player.getHungerManager();
        if (hungerManager.getFoodLevel() <= hungerConfig.getValue()) {
            int slot = getFoodSlot();
            if (slot == -1) {
                return;
            }
            if (slot == 45) {
                mc.player.setCurrentHand(Hand.OFF_HAND);
            } else {
                prevSlot = mc.player.getInventory().selectedSlot;
                Managers.INVENTORY.setClientSlot(slot);
            }
            KeyBinding.setKeyPressed(((AccessorKeyBinding) mc.options.useKey).getBoundKey(), true);
        }
    }

    public int getFoodSlot() {
        int foodLevel = -1;
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem().isFood()) {
                if (stack.getItem() == Items.PUFFERFISH
                        || stack.getItem() == Items.CHORUS_FRUIT) {
                    continue;
                }
                int hunger = stack.getItem().getFoodComponent().getHunger();
                if (hunger > foodLevel) {
                    slot = i;
                    foodLevel = hunger;
                }
            }
        }
        ItemStack offhand = mc.player.getOffHandStack();
        if (offhand.getItem().isFood()) {
            if (offhand.getItem() == Items.PUFFERFISH
                    || offhand.getItem() == Items.CHORUS_FRUIT) {
                return slot;
            }
            int hunger = offhand.getItem().getFoodComponent().getHunger();
            if (hunger > foodLevel) {
                slot = 45;
            }
        }
        return slot;
    }
}
