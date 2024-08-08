package net.kyc.client.impl.module.client;

import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.util.Timer;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.jetbrains.annotations.NotNull;
import net.kyc.client.impl.event.PacketEvent;
import static net.kyc.client.util.chat.ChatUtils.sendMessage;

public class AntiCrashModule extends ToggleModule { //https://github.com/Bram1903/MinecraftPlayerCrasher
    Config<Boolean> debug = new BooleanConfig("Debug", "meow",Boolean.TRUE);

    private Timer debugTimer = new Timer();

    public AntiCrashModule() {
        super("AntiCrash", "lol", ModuleCategory.CLIENT);
    }

    @EventListener
    public void onPacketReceive(PacketEvent.@NotNull Receive receive) {
        if (receive.getPacket() instanceof ExplosionS2CPacket exp && (exp.getX() > 1E9 || exp.getY() > 1E9 || exp.getZ() > 1E9 || exp.getRadius() > 1E9)) {
            if (debug.getValue() && debugTimer.passedMs(1000)) {
                sendMessage("ExplosionS2CPacket canceled");
                debugTimer.reset();
            }
            receive.cancel();
        } else if (receive.getPacket() instanceof ParticleS2CPacket p && (p.getX() > 1E9 || p.getY() > 1E9 || p.getZ() > 1E9 || p.getSpeed() > 1E9 || p.getOffsetX() > 1E9 || p.getOffsetY() > 1E9 || p.getOffsetZ() > 1E9)) {
            if (debug.getValue() && debugTimer.passedMs(1000)) {
                sendMessage("ParticleS2CPacket canceled");
                debugTimer.reset();
            }
            receive.cancel();
        } else if (receive.getPacket() instanceof PlayerPositionLookS2CPacket pos && (pos.getX() > 1E9 || pos.getY() > 1E9 || pos.getZ() > 1E9 || pos.getYaw() > 1E9 || pos.getPitch() > 1E9)) {
            if (debug.getValue() && debugTimer.passedMs(1000)) {
                sendMessage("PlayerPositionLookS2CPacket canceled");
                debugTimer.reset();
            }
            receive.cancel();
        }

    }

    @Override
    public void onUpdate() {

    }
}