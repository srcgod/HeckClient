package net.kyc.client.impl.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.kyc.client.api.command.Command;
import net.kyc.client.api.module.Module;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.init.Managers;
import net.kyc.client.util.chat.ChatUtil;

/**
 * @author Heckk
 * @since 1.0
 */
public class DisableAllCommand extends Command {
    /**
     *
     */
    public DisableAllCommand() {
        super("DisableAll", "Disables all enabled modules", literal("disableall"));
    }

    @Override
    public void buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(c -> {
            for (Module module : Managers.MODULE.getModules()) {
                if (module instanceof ToggleModule toggleModule && toggleModule.isEnabled()) {
                    toggleModule.disable();
                }
            }
            ChatUtil.clientSendMessage("All modules are disabled");
            return 1;
        });
    }
}
