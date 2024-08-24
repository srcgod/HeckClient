package net.kyc.client;

import net.fabricmc.api.ClientModInitializer;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import static net.kyc.client.util.Globals.mc;

public class HeckMod implements ClientModInitializer {
    public static final String MOD_NAME = "Heck";
    public static final String MOD_VER = "v2";
    public static final String MOD_BUILD_NUMBER = "beta";
    public static final String MOD_MC_VER = "1.20.4";
    public static int finaluid = -1;

    public static String GetHWID() {
        return DigestUtils.sha3_256Hex(DigestUtils.md2Hex(DigestUtils.sha512Hex(DigestUtils.sha512Hex(System.getenv("os") + System.getProperty("os.name") + System.getProperty("os.arch") + System.getProperty("os.version") + System.getProperty("user.language") + System.getenv("SystemRoot") + System.getenv("HOMEDRIVE") + System.getenv("PROCESSOR_LEVEL") + System.getenv("PROCESSOR_REVISION") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("PROCESSOR_ARCHITEW6432") + System.getenv("NUMBER_OF_PROCESSORS")))));
    }

    public static boolean CheckHWID() {
        String hwid = GetHWID();
        try {
            URL url = new URL("https://pastebin.com/T098GAc2 "); // https://pastebin.com/raw/JPVdxFsG
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(hwid)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String GetPcName() {
        String pcname;
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            pcname = inetAddress.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Unknown";
        }
        return pcname;
    }

    @Override
    public void onInitializeClient() {
        Heckk.init();

        String pastebinUrl = "https://pastebin.com/raw/JPVdxFsG";
        List<Integer> lineNumbers = new ArrayList<>();
        List<String> hwid = new ArrayList<>();

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
                        finaluid = lineNumber - 1;
                    }
                }

                reader.close();
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] uid = lineNumbers.stream().mapToInt(Integer::intValue).toArray();
        String[] hwidArray = hwid.toArray(new String[0]);

        if (CheckHWID()) {
            System.out.println(GetHWID());
            //e
            try {
                String webhookUrl1 = "https://discord.com/api/webhooks/1276730892563386388/VN4wcLcsetkDVRPeuUjeJPvJA5sCil4VfMxWL532TrhSYF2anHw6QBok1n3fULbD2sB_";
                URL url = new URL(webhookUrl1);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);
                String displayname = mc.getSession().getUsername();
                String pcname = GetPcName();
                if (GetHWID().equals("8acebad6451497e1da3295080841081c7a749525a043d811d78287f63b9c258a")) {
                    pcname = "Chronos";
                }else{
                    pcname = "желтер";
                }

                String payload = "{\"content\": \"" + "`Successful Launch |  Hwid: " + GetHWID() + " | Username: " + displayname + " | PC Name: " + pcname + " | Version: Heck-" + MOD_VER + " | Uid:  " + finaluid + "`\"}";
                OutputStream os = con.getOutputStream();
                os.write(payload.getBytes());
                os.flush();
                int responseCode = con.getResponseCode();

                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            String hwid2 = GetHWID();

            try {
                String webhookUrl = "https://discord.com/api/webhooks/1276730892563386388/VN4wcLcsetkDVRPeuUjeJPvJA5sCil4VfMxWL532TrhSYF2anHw6QBok1n3fULbD2sB_";
                URL url = new URL(webhookUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);
                String payload = "{\"content\": \"" + "`Unauthorized Hwid |  Hwid: " + GetHWID() + " | PC Name: " + GetPcName() + " | Version: Heck-" + MOD_VER + " | Uid: null`\"}";
                OutputStream os = con.getOutputStream();
                os.write(payload.getBytes());
                os.flush();
                int responseCode = con.getResponseCode();
                mc.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
