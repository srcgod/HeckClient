package net.kyc.client.api.discord.callbacks;

import net.kyc.client.api.discord.DiscordUser;
import com.sun.jna.Callback;

public interface ReadyCallback extends Callback {
    void apply(final DiscordUser p0);
}
