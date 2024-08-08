package net.kyc.client.util.chat;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.kyc.client.util.Globals.mc;

public class ChatUtils {

    public static void sendMessage(String message) {
        mc.player.sendMessage(Text.of(Formatting.AQUA + "[Heck] " + Formatting.WHITE + message));
    }

    public static void warningMessage(String message) {
        mc.player.sendMessage(Text.of(Formatting.AQUA + "[Heck] " + Formatting.YELLOW + message));
    }

    public static void errorMessage(String message) {
        mc.player.sendMessage(Text.of(Formatting.AQUA + "[Heck] " + Formatting.RED + message));
    }
}
