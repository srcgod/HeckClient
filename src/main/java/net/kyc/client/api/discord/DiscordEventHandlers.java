package net.kyc.client.api.discord;

import java.util.Arrays;
import java.util.List;
import net.kyc.client.api.discord.callbacks.JoinGameCallback;
import net.kyc.client.api.discord.callbacks.ErroredCallback;
import net.kyc.client.api.discord.callbacks.ReadyCallback;
import net.kyc.client.api.discord.callbacks.SpectateGameCallback;
import net.kyc.client.api.discord.callbacks.JoinRequestCallback;
import net.kyc.client.api.discord.callbacks.DisconnectedCallback;
import com.sun.jna.Structure;

public class DiscordEventHandlers extends Structure {
    public DisconnectedCallback disconnected;
    public JoinRequestCallback joinRequest;
    public SpectateGameCallback spectateGame;
    public ReadyCallback ready;
    public ErroredCallback errored;
    public JoinGameCallback joinGame;
    
    protected List<String> getFieldOrder() {
        return Arrays.asList("ready", "disconnected", "errored", "joinGame", "spectateGame", "joinRequest");
    }
    
   
}
