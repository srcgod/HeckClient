package net.kyc.client.impl.module.misc;

import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.config.setting.StringConfig;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.network.PacketEvent;
import net.kyc.client.util.chat.ChatUtil;

/**
 * @author linus
 * @since 1.0
 */
public class AntiAFKModule extends ToggleModule {
    //
    Config<Boolean> messageConfig = new BooleanConfig("Message", "Messages in chat to prevent AFK kick", true);
    Config<Boolean> tabCompleteConfig = new BooleanConfig("TabComplete", "Uses tab complete in chat to prevent AFK kick", true);
    Config<Boolean> rotateConfig = new BooleanConfig("Rotate", "Rotates the player to prevent AFK kick", true);
    Config<Boolean> autoReplyConfig = new BooleanConfig("AutoReply", "Replies to players messaging you in chat", true);
    Config<String> replyConfig = new StringConfig("Reply", "The reply message for AutoReply", "[Heckk] I am currently AFK.");
    Config<Float> delayConfig = new NumberConfig<>("Delay", "The delay between actions", 5.0f, 60.0f, 270.0f);

    /**
     *
     */
    public AntiAFKModule() {
        super("AntiAFK", "Prevents the player from being kicked for AFK",
                ModuleCategory.MISC);
    }

    @EventListener
    public void onPacketInbound(PacketEvent.Inbound event) {
        if (event.getPacket() instanceof ChatMessageS2CPacket packet
                && autoReplyConfig.getValue()) {
            String[] words = packet.body().content().split(" ");
            if (words[1].startsWith("whispers:")) {
                ChatUtil.serverSendMessage("/r " + replyConfig.getValue());
            }
        }
    }

    @Override
    public void onUpdate() {

    }
}
