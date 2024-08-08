package net.kyc.client.api.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.kyc.client.util.Globals;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class PlayerArgumentType implements ArgumentType<String>, Globals {

    public static PlayerArgumentType player() {
        return new PlayerArgumentType();
    }

    public static String getPlayer(final CommandContext<?> context, final String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Collection<PlayerListEntry> playerListEntries = mc.player.networkHandler.getPlayerList();
        for (PlayerListEntry playerListEntry : playerListEntries) {
            builder.suggest(playerListEntry.getProfile().getName());
        }
        return builder.buildFuture();
    }
}
