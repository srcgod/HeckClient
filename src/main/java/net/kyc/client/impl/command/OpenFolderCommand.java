package net.kyc.client.impl.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.kyc.client.Heckk;
import net.minecraft.command.CommandSource;
import net.kyc.client.api.command.Command;
import net.kyc.client.util.chat.ChatUtil;

import java.awt.*;
import java.io.IOException;

/**
 * @author linus
 * @since 1.0
 */
public class OpenFolderCommand extends Command {

    /**
     *
     */
    public OpenFolderCommand() {
        super("OpenFolder", "Opens the client configurations folder", literal("openfolder"));
    }

    @Override
    public void buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(c -> {
            try {
                Desktop.getDesktop().open(Heckk.CONFIG.getClientDirectory().toFile());
            } catch (IOException e) {
                e.printStackTrace();
                ChatUtil.error("Failed to open client folder!");
            }
            return 1;
        });
    }
}
