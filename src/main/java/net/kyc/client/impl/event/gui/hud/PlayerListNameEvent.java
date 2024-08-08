package net.kyc.client.impl.event.gui.hud;

import net.minecraft.text.Text;
import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

import java.util.UUID;

@Cancelable
public class PlayerListNameEvent extends Event {
    private Text playerName;
    private final UUID id;

    public PlayerListNameEvent(Text playerName, UUID id) {
        this.playerName = playerName;
        this.id = id;
    }

    public void setPlayerName(Text playerName) {
        this.playerName = playerName;
    }

    public Text getPlayerName() {
        return playerName;
    }

    public UUID getId() {
        return id;
    }
}
