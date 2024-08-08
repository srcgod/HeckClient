package net.kyc.client.api.discord.callbacks;

import net.kyc.client.api.discord.DiscordUser;
import com.sun.jna.Callback;

public interface JoinRequestCallback extends Callback {
    void apply(final DiscordUser p0);
}
