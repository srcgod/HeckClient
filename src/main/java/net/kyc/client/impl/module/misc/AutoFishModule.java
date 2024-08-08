package net.kyc.client.impl.module.misc;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.EventStage;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.TickEvent;
import net.kyc.client.impl.event.network.PacketEvent;
import net.kyc.client.impl.imixin.IMinecraftClient;

/**
 * @author linus
 * @since 1.0
 */
public class AutoFishModule extends ToggleModule {
    //
    Config<Boolean> openInventoryConfig = new BooleanConfig("OpenInventory", "Allows you to fish while in the inventory", true);
    Config<Integer> castDelayConfig = new NumberConfig<>("CastingDelay", "The delay between fishing rod casts", 10, 15, 25);
    Config<Float> maxSoundDistConfig = new NumberConfig<>("MaxSoundDist", "The maximum distance from the splash sound", 0.0f, 2.0f, 5.0f);
    //
    private boolean autoReel;
    private int autoReelTicks;
    //
    private int autoCastTicks;

    /**
     *
     */
    public AutoFishModule() {
        super("AutoFish", "Automatically casts and reels fishing rods",
                ModuleCategory.MISC);
    }

    @EventListener
    public void onPacketInbound(PacketEvent.Inbound event) {
        if (mc.player == null) {
            return;
        }
        if (event.getPacket() instanceof PlaySoundS2CPacket packet
                && packet.getSound().value() == SoundEvents.ENTITY_FISHING_BOBBER_SPLASH
                && mc.player.getMainHandStack().getItem() == Items.FISHING_ROD) {
            FishingBobberEntity fishHook = mc.player.fishHook;
            if (fishHook == null || fishHook.getPlayerOwner() != mc.player) {
                return;
            }
            double dist = fishHook.squaredDistanceTo(packet.getX(),
                    packet.getY(), packet.getZ());
            if (dist <= maxSoundDistConfig.getValue()) {
                autoReel = true;
                autoReelTicks = 4;
            }
        }
    }

    @EventListener
    public void onTick(TickEvent event) {
        if (event.getStage() != EventStage.PRE) {
            return;
        }
        if (mc.currentScreen == null || mc.currentScreen instanceof ChatScreen
                || openInventoryConfig.getValue()) {
            if (mc.player.getMainHandStack().getItem() != Items.FISHING_ROD) {
                return;
            }
            FishingBobberEntity fishHook = mc.player.fishHook;
            if ((fishHook == null || fishHook.getHookedEntity() != null)
                    && autoCastTicks <= 0) {
                ((IMinecraftClient) mc).rightClick();
                autoCastTicks = castDelayConfig.getValue();
                return;
            }
            if (autoReel) {
                if (autoReelTicks <= 0) {
                    ((IMinecraftClient) mc).rightClick();
                    autoReel = false;
                    return;
                }
                autoReelTicks--;
            }
        }
        autoCastTicks--;
    }

    @Override
    public void onUpdate() {

    }
}
