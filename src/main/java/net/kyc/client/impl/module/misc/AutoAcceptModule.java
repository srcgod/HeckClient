package net.kyc.client.impl.module.misc;

import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.network.PacketEvent;
import net.kyc.client.init.Managers;
import net.kyc.client.util.chat.ChatUtil;
import net.kyc.client.util.math.timer.CacheTimer;
import net.kyc.client.util.math.timer.Timer;

/**
 * @author linus
 * @since 1.0
 */
public class AutoAcceptModule extends ToggleModule {
    //
    private final Timer acceptTimer = new CacheTimer();
    //
    Config<Float> delayConfig = new NumberConfig<>("Delay", "The delay before" +
            " accepting teleport requests", 0.0f, 3.0f, 10.0f);

    /**
     *
     */
    public AutoAcceptModule() {
        super("AutoAccept", "Automatically accepts teleport requests",
                ModuleCategory.MISC);
    }

    @EventListener
    public void onPacketInbound(PacketEvent.Inbound event) {
        if (event.getPacket() instanceof ChatMessageS2CPacket packet) {
            String text = packet.body().content();
            if ((text.contains("has requested to teleport to you.")
                    || text.contains("has requested you teleport to them."))
                    && acceptTimer.passed(delayConfig.getValue() * 1000)) {
                for (String friend : Managers.SOCIAL.getFriends()) {
                    if (text.contains(friend)) {
                        ChatUtil.serverSendMessage("/tpaccept");
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onUpdate() {

    }
}
