package net.kyc.client.impl.gui.hud;

import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.impl.module.client.HUDModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * The class to extends all HUD modules, yeah.
 *
 * @author Bebra_tyan(Ferra13671)
 */
public class HudModule {

    public final String name;

    private int x;
    private int y;

    //Needed for the HUDEditor
    public int width = 0;
    public int height = 0;
    //////

    //To add settings
    public ArrayList<Config<?>> configs = new ArrayList<>();


    //Not working, lol
    public final boolean autoEnabled;

    public HUDModule hudModule;

    public boolean enabled = false;

    public final Config<Boolean> toggleConfig;

    public MinecraftClient mc = MinecraftClient.getInstance();

    public final DecimalFormat decimal = new DecimalFormat("0.0");


    public HudModule(String name, int x, int y, boolean autoEnabled, HUDModule hudModule) {
        this.name = name;
        setX(x);
        setY(y);

        this.autoEnabled = autoEnabled;

        this.hudModule = hudModule;

        toggleConfig = new BooleanConfig(this.name, "", autoEnabled);
        configs.add(toggleConfig);
    }


    public void init() {}

    public void render(RenderStage renderStage, DrawContext drawContext) {}

    public void tickUpdate() {}

    public void toggle() {
        enabled = !enabled;
    }

    public void setEnabled(boolean value) {
        this.enabled = value;
    }


    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int value) {
        this.x = value;
    }

    public void setY(int value) {
        this.y = value;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setWidth(int value) {
        this.width = value;
    }

    public void setHeight(int value) {
        this.height = value;
    }

    public Config<?> getConfig(String name) {
        for (Config<?> config : configs) {
            if (config.getName().equals(name))
                return config;
        }
        return null;
    }
}
