package net.kyc.client.impl.module.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.config.setting.EnumConfig;
import net.kyc.client.api.event.EventStage;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.TickEvent;
import net.kyc.client.impl.event.network.PacketEvent;
import net.kyc.client.impl.imixin.IPlayerInteractEntityC2SPacket;
import net.kyc.client.init.Managers;
import net.kyc.client.init.Modules;
import net.kyc.client.util.math.timer.CacheTimer;
import net.kyc.client.util.math.timer.Timer;
import net.kyc.client.util.network.InteractType;
import net.kyc.client.util.player.InventoryUtil;
import net.kyc.client.util.string.EnumFormatter;
import net.kyc.client.util.world.EntityUtil;

/**
 * @author linus
 * @since 1.0
 */
public class CriticalsModule extends ToggleModule {

    //
    Config<CritMode> modeConfig = new EnumConfig<>("Mode", "Mode for critical attack modifier", CritMode.PACKET, CritMode.values());
    Config<Boolean> packetSyncConfig = new BooleanConfig("Tick-Sync", "Syncs the cached packet interaction to the next tick", false);
    // The cached attack packets that will be resent after manipulating
    // player position packets
    private PlayerInteractEntityC2SPacket attackPacket;
    private HandSwingC2SPacket swingPacket;
    //
    private final Timer attackTimer = new CacheTimer();

    /**
     *
     */
    public CriticalsModule() {
        super("Criticals", "Modifies attacks to always land critical hits",
                ModuleCategory.COMBAT);
    }

    /**
     * @return
     */
    @Override
    public String getModuleData() {
        return EnumFormatter.formatEnum(modeConfig.getValue());
    }

    @EventListener
    public void onTick(TickEvent event) {
        if (event.getStage() != EventStage.POST) {
            return;
        }
        if (packetSyncConfig.getValue() && attackPacket != null && swingPacket != null) {
            Managers.NETWORK.sendPacket(attackPacket);
            Managers.NETWORK.sendPacket(swingPacket);
            attackPacket = null;
            swingPacket = null;
        }
    }

    /**
     * @param event
     */
    @EventListener
    public void onPacketOutbound(PacketEvent.Outbound event) {
        // Custom aura crit handling
        if (Modules.AURA.isEnabled()) {
            return;
        }
        if (event.getPacket() instanceof IPlayerInteractEntityC2SPacket packet
                && packet.getType() == InteractType.ATTACK && !event.isClientPacket()) {
            if (isGrim()) {
                if (!mc.player.isOnGround()) {
                    double x = mc.player.getX();
                    double y = mc.player.getY();
                    double z = mc.player.getZ();
                    Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                            x, y - 0.000001, z, false));
                }
                return;
            }
            if (!Managers.POSITION.isOnGround()
                    || mc.player.isRiding()
                    || mc.player.isFallFlying()
                    || mc.player.isTouchingWater()
                    || mc.player.isInLava()
                    || mc.player.isHoldingOntoLadder()
                    || mc.player.hasStatusEffect(StatusEffects.BLINDNESS)
                    || mc.player.input.jumping
                    || InventoryUtil.isHolding32k()) {
                return;
            }
            // Attacked entity
            final Entity e = packet.getEntity();
            if (e == null || !e.isAlive() || e instanceof EndCrystalEntity) {
                return;
            }
            if (attackTimer.passed(500)) {
                event.cancel();
                if (EntityUtil.isVehicle(e)) {
                    if (modeConfig.getValue() == CritMode.PACKET) {
                        for (int i = 0; i < 5; ++i) {
                            Managers.NETWORK.sendPacket(PlayerInteractEntityC2SPacket.attack(e,
                                    Managers.POSITION.isSneaking()));
                            Managers.NETWORK.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                        }
                    }
                    return;
                }
                attackPacket = PlayerInteractEntityC2SPacket.attack(e, Managers.POSITION.isSneaking());
                if (!packetSyncConfig.getValue()) {
                    Managers.NETWORK.sendPacket(attackPacket);
                }
                preAttackPacket();
                mc.player.addCritParticles(e);
                attackTimer.reset();
            }
        } else if (event.getPacket() instanceof HandSwingC2SPacket packet) {
            if (packetSyncConfig.getValue() && attackPacket != null) {
                event.cancel();
                swingPacket = packet;
            }
        }
    }

    /**
     * Callback method for pre attack stage, must be called before the attack
     * packet or else the movements will not be registered
     *
     * @see AuraModule#postAttackTarget(Entity)
     */
    public void preAttackPacket() {
        double x = Managers.POSITION.getX();
        double y = Managers.POSITION.getY();
        double z = Managers.POSITION.getZ();
        switch (modeConfig.getValue()) {
            case VANILLA -> {
                double d = 1.0e-7 + 1.0e-7 * (1.0 + RANDOM.nextInt(RANDOM.nextBoolean() ? 34 : 43));
                Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        x, y + 0.1016f + d * 3.0f, z, false));
                Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        x, y + 0.0202f + d * 2.0f, z, false));
                Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        x, y + 3.239e-4 + d, z, false));
            }
            case PACKET -> {
                Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        x, y + 0.05f, z, false));
                Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        x, y, z, false));
                Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        x, y + 0.03f, z, false));
                Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        x, y, z, false));
            }
            case PACKET_STRICT -> {
                Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        x, y + 0.11f, z, false));
                Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        x, y + 0.1100013579f, z, false));
                Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        x, y + 0.0000013579f, z, false));
            }
            case LOW_HOP -> {
                // mc.player.jump();
                Managers.MOVEMENT.setMotionY(0.3425);
            }
        }
    }

    public boolean isGrim() {
        return modeConfig.getValue() == CritMode.GRIM;
    }

    @Override
    public void onUpdate() {

    }

    public enum CritMode {
        PACKET,
        PACKET_STRICT,
        VANILLA,
        GRIM,
        LOW_HOP
    }
}
