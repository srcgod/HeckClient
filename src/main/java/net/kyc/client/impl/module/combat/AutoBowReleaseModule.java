package net.kyc.client.impl.module.combat;

import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.EventStage;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.TickEvent;
import net.kyc.client.init.Managers;
import net.kyc.client.init.Modules;

/**
 * @author linus
 * @since 1.0
 */
public class AutoBowReleaseModule extends ToggleModule {
    //
    Config<Boolean> crossbowConfig = new BooleanConfig("Crossbow", "Automatically releases crossbow when fully charged", false);
    Config<Integer> ticksConfig = new NumberConfig<>("Ticks", "Ticks before releasing the bow charge", 3, 5, 20);
    Config<Boolean> tpsSyncConfig = new BooleanConfig("TPS-Sync", "Sync bow release to server ticks", false);

    /**
     *
     */
    public AutoBowReleaseModule() {
        super("AutoBowRelease", "Automatically releases a charged bow",
                ModuleCategory.COMBAT);
    }

    @EventListener
    public void onTick(TickEvent event) {
        if (Modules.SELF_BOW.isEnabled()) {
            return;
        }
        if (event.getStage() == EventStage.POST) {
            ItemStack mainhand = mc.player.getMainHandStack();
            if (mainhand.getItem() == Items.BOW) {
                float off = tpsSyncConfig.getValue() ? 20.0f - Managers.TICK.getTpsAverage() : 0.0f;
                if (mc.player.getItemUseTime() + off >= ticksConfig.getValue()) {
                    Managers.NETWORK.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM,
                            BlockPos.ORIGIN, Direction.DOWN));
                    // Managers.NETWORK.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id));
                    mc.player.stopUsingItem();
                }
            } else if (crossbowConfig.getValue() && mainhand.getItem() == Items.CROSSBOW
                    && mc.player.getItemUseTime() / (float) CrossbowItem.getPullTime(mc.player.getMainHandStack()) > 1.0f) {
                Managers.NETWORK.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM,
                        BlockPos.ORIGIN, Direction.DOWN));
                mc.player.stopUsingItem();
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            }
        }
    }

    @Override
    public void onUpdate() {

    }
}
