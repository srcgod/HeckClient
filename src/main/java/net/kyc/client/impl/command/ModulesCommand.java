package net.kyc.client.impl.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;
import net.kyc.client.HeckMod;
import net.kyc.client.api.command.Command;
import net.kyc.client.api.module.Module;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.init.Managers;
import net.kyc.client.util.chat.ChatUtil;

public class ModulesCommand extends Command {
    public ModulesCommand() {
        super("Modules", "Displays all client modules", literal("modules"));
    }

    @Override
    public void buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(c -> {
            StringBuilder modulesList = new StringBuilder();
            for (Module module : Managers.MODULE.getModules()) {
                String formatting = module instanceof ToggleModule t && t.isEnabled() ? "§s" : "§f";
                modulesList.append(formatting);
                modulesList.append(module.getName());
                modulesList.append(Formatting.RESET);
                // LOL

            }
            ChatUtil.clientSendMessageRaw(" §7Modules:§f " + modulesList);
            return 1;
        });
    }
}
