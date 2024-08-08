package net.kyc.client.api.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.kyc.client.init.Managers;
import net.kyc.client.util.Globals;

/**
 * @author linus
 * @since 1.0
 */
public abstract class Command implements Globals {
    //
    private final String name;
    private final String desc;
    private final LiteralArgumentBuilder<CommandSource> builder;

    /**
     * @param name
     * @param desc
     * @param builder
     */
    public Command(String name, String desc, LiteralArgumentBuilder<CommandSource> builder) {
        this.name = name;
        this.desc = desc;
        this.builder = builder;
    }

    public abstract void buildCommand(LiteralArgumentBuilder<CommandSource> builder);

    protected static LiteralArgumentBuilder<CommandSource> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }
    protected static <T> RequiredArgumentBuilder<CommandSource, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    protected static SuggestionProvider<CommandSource> suggest(String... suggestions) {
        return (context, builder) -> CommandSource.suggestMatching(Lists.newArrayList(suggestions), builder);
    }

    /**
     * @return
     */
    public LiteralArgumentBuilder<CommandSource> getCommandBuilder() {
        return builder;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return desc;
    }

    /**
     * Returns the unique command identifier, used to identify the command in
     * the chat. Ex: help, prefix, openfolder, etc.
     *
     * @return
     */
    public String getUsage() {
        return Managers.COMMAND.getDispatcher().getAllUsage(builder.build(), Managers.COMMAND.getSource(), false)[0];
    }
}
