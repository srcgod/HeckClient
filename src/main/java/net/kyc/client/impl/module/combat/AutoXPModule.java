package net.kyc.client.impl.module.combat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.NumberDisplay;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.RotationModule;
import net.kyc.client.impl.event.network.PlayerTickEvent;
import net.kyc.client.init.Managers;
import net.kyc.client.util.math.timer.TickTimer;

/**
 * @author hockeyl8
 * @since 1.0
 */
public class AutoXPModule extends RotationModule {

    Config<Float> delayConfig = new NumberConfig<>("Delay", "Delay to throw xp in ticks.", 1.0f, 1.0f, 10.0f, NumberDisplay.DEFAULT);
    Config<Boolean> durabilityCheckConfig = new BooleanConfig("DurabilityCheck", "Check if your armor and held item durability is full then disables if it is.", true);
    Config<Boolean> rotateConfig = new BooleanConfig("Rotate", "Rotates the player while throwing xp.", false);
    Config<Boolean> swingConfig = new BooleanConfig("Swing", "Swings hand while throwing xp.", false);

    private final TickTimer delayTimer = new TickTimer();

    public AutoXPModule() {
        super("AutoXP", "Automatically throws xp silently.", ModuleCategory.COMBAT, 850);
    }

    @EventListener
    public void onPlayerTick(PlayerTickEvent event) {

        if (mc.player == null || !delayTimer.passed(delayConfig.getValue())) return;

        if (durabilityCheckConfig.getValue() && areItemsFullDura(mc.player)) {
            disable();
            return;
        }

        int prev = mc.player.getInventory().selectedSlot;
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() instanceof ExperienceBottleItem) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            disable();
            return;
        }

        Managers.INVENTORY.setSlot(slot);
        if (rotateConfig.getValue()) {
            setRotation(mc.player.getYaw(), 90.0f);
            if (isRotationBlocked()) return;
        }
        Managers.NETWORK.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id));
        if (swingConfig.getValue()) {
            mc.player.swingHand(Hand.MAIN_HAND);
        }
        Managers.INVENTORY.setSlot(prev);
        delayTimer.reset();
    }

    public static boolean areItemsFullDura(PlayerEntity player) {
        if (!isItemFullDura(player.getMainHandStack()) || !isItemFullDura(player.getOffHandStack())) {
            return false;
        }

        for (ItemStack stack : player.getArmorItems()) {
                if (!isItemFullDura(stack)) {
                    return false;
                }
        }

        return true;
    }

    private static boolean isItemFullDura(ItemStack stack) {
        if (stack.isEmpty()) {
            return true;
        }
        int maxDura = stack.getMaxDamage();
        int currentDura = stack.getDamage();
        return currentDura == 0 || maxDura == 0;
    }
}
