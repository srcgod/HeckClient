package net.kyc.client;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.discord.*;
import net.kyc.client.util.Globals;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static net.kyc.client.HeckMod.GetHWID;
import static net.kyc.client.HeckMod.finaluid;
import static net.kyc.client.util.Globals.mc;

public class RPC {
    public static String discordID = "864496689280712724";
    public static DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    public static DiscordRPC discordRPC = DiscordRPC.INSTANCE;
    public static String clientVersion = HeckMod.MOD_NAME + HeckMod.MOD_VER;
    public static Config<Boolean> showIP = new BooleanConfig("ShowIP","sso", true);

    public static void startRPC() {

        String pastebinUrl = "https://pastebin.com/raw/JPVdxFsG";
        List<Integer> lineNumbers = new ArrayList<>();
        List<String> hwid = new ArrayList<>();
        int uid = -1;

        try {
            URL url = new URL(pastebinUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                int lineNumber = 1;

                while ((line = reader.readLine()) != null) {
                    lineNumbers.add(lineNumber);
                    hwid.add(line);
                    lineNumber++;

                    if (line.trim().equals(GetHWID())) {
                        uid = lineNumber - 1;
                    }
                }

                reader.close();
            } else {
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }



        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        eventHandlers.disconnected = RPC::lambda$startRPC$0;
        discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);
        RPC.discordRichPresence.startTimestamp = System.currentTimeMillis() / ((long)-2121370231 ^ 0xFFFFFFFF818E7661L);
        RPC.discordRichPresence.details = mc.getSession().getUsername() + " | UID: " + uid ;
        RPC.discordRichPresence.largeImageKey = "applicationframehost_ffzspqvzal";
        //RPC.discordRichPresence.largeImageKey = mc.player.networkHandler.getServerInfo().address;
        RPC.discordRichPresence.largeImageText = HeckMod.MOD_VER;
        RPC.discordRichPresence.state = "Jailouter, Chromos, SrcGod" ;
        discordRPC.Discord_UpdatePresence(discordRichPresence);
    }
        public static void stopRPC() {
        discordRPC.Discord_Shutdown();
        discordRPC.Discord_ClearPresence();
    }

    public static void lambda$startRPC$0(final int var1, final String var2) {
        System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2);
    }



}
