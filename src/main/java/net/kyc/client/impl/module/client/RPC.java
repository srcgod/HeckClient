package net.kyc.client.impl.module.client;
/*
import net.kyc.client.HeckMod;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.EnumConfig;
import net.kyc.client.api.config.setting.StringConfig;
import net.kyc.client.api.discord.DiscordEventHandlers;
import net.kyc.client.api.discord.DiscordRPC;
import net.kyc.client.api.discord.DiscordRichPresence;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.AddServerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;

import java.io.*;
import java.util.Objects;


public final class RPC extends ToggleModule {
    private static final DiscordRPC rpc = DiscordRPC.INSTANCE;
    Config<Mode> mode = new EnumConfig<>("Picture","LOL", Mode.Recode, Mode.values() );
    Config<sMode> smode = new EnumConfig<> ("StateMode","LOL", sMode.Stats, sMode.values());
    Config<String> state = new StringConfig("State", "lol","player");


    public static DiscordRichPresence presence = new DiscordRichPresence();
    public static boolean started;
    static String String1 = "none";
    private static Thread thread;

    public RPC() {
        super("DiscordRPC","lol", ModuleCategory.CLIENT);
    }

    public static void readFile() {
        try {
            File file = new File("ThunderHackRecode/misc/RPC.txt");
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    while (reader.ready()) {
                        String1 = reader.readLine();
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    public static void WriteFile(String url1, String url2) {
        File file = new File("ThunderHackRecode/misc/RPC.txt");
        try {
            file.createNewFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(url1 + "SEPARATOR" + url2 + '\n');
            } catch (Exception ignored) {
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onUpdate() {
        started = false;
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }
        rpc.Discord_Shutdown();
    }

    @Override
    public void onEnable() {
        startRpc();
    }

    public void startRpc() {

        if (!started) {
            started = true;
            DiscordEventHandlers handlers = new DiscordEventHandlers();
            rpc.Discord_Initialize( "1260131483142717442", handlers, true, "");
            presence.startTimestamp = (System.currentTimeMillis() / 1000L);
            presence.largeImageText = "v" + HeckMod.MOD_VER + " [" + "meow" + "]";
            rpc.Discord_UpdatePresence(presence);

            thread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    rpc.Discord_RunCallbacks();

                    presence.details = getDetails();

                    switch (smode.getValue()) {
                        case Stats ->
                                presence.state = "Hacks: " + HeckMod.MOD_NAME + " / " ;
                        case Custom -> presence.state = state.getValue();
                        case Version -> presence.state = "v" + HeckMod.MOD_VER +" for mc 1.20.6";
                    }





                    switch (mode.getValue()) {
                        case MegaCute ->
                                presence.largeImageKey = "https://media1.tenor.com/images/6bcbfcc0be97d029613b54f97845bc59/tenor.gif?itemid=26823781";
                        case Custom -> {
                            readFile();
                            presence.largeImageKey = String1.split("SEPARATOR")[0];
                            if (!Objects.equals(String1.split("SEPARATOR")[1], "none")) {
                                presence.smallImageKey = String1.split("SEPARATOR")[1];
                            }
                        }
                    }
                    rpc.Discord_UpdatePresence(presence);
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException ignored) {
                    }
                }
            }, "TH-RPC-Handler");
            thread.start();
        }
    }

    private String getDetails() {
        String result = "";

        if (mc.currentScreen instanceof TitleScreen) {
            result =  "In Main menu";
        } else if (mc.currentScreen instanceof MultiplayerScreen || mc.currentScreen instanceof AddServerScreen) {
            result =  "Picks a server";
        } else if (mc.getCurrentServerEntry() != null) {
            result = "Playing on %sPlaying on server%s".formatted(mc.getCurrentServerEntry().address, mc.getCurrentServerEntry());
        } else if (mc.isInSingleplayer()) {
            result =  "SinglePlayer hacker";
        }
        return result;
    }

    public enum Mode {Custom, MegaCute, Recode}

    public enum sMode {Custom, Stats, Version}
}
*/
