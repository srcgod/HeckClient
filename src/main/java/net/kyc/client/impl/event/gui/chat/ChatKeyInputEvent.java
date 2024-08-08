package net.kyc.client.impl.event.gui.chat;

import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

/**
 * @author linus
 * @since 1.0
 */
@Cancelable
public class ChatKeyInputEvent extends Event {
    //
    private final int keycode;
    private String chatText;

    /**
     * @param keycode
     * @param chatText
     */
    public ChatKeyInputEvent(int keycode, String chatText) {
        this.keycode = keycode;
        this.chatText = chatText;
    }

    public int getKeycode() {
        return keycode;
    }

    public String getChatText() {
        return chatText;
    }

    public void setChatText(String chatText) {
        this.chatText = chatText;
    }
}
