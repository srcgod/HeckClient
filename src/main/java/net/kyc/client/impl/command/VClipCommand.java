package net.kyc.client.impl.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.kyc.client.api.command.Command;
import net.kyc.client.init.Managers;
import net.kyc.client.util.chat.ChatUtil;

/**
 * @author linus
 * @since 1.0
 */
public class VClipCommand extends Command {
    /**
     *
     */
    public VClipCommand() {
        super("VClip", "Vertically clips the player", literal("vclip"));
    }

    @Override
    public void buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("distance", DoubleArgumentType.doubleArg()).executes(c -> {
            double dist = DoubleArgumentType.getDouble(c, "distance");
            double y = Managers.POSITION.getY();
            if (Math.abs(y) != 256) {
                Managers.POSITION.setPositionY(y + dist);
                ChatUtil.clientSendMessage("Vertically clipped §s" + dist + "§f blocks");
            }
            return 1;
        })).executes(c -> {
            ChatUtil.error("Must provide distance!");
            return 1;
        });
    }
}
