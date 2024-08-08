package net.kyc.client.impl.module.client;

import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.config.setting.ColorConfig;
import net.kyc.client.api.module.ConcurrentModule;
import net.kyc.client.api.module.ModuleCategory;

import java.awt.*;

/**
 * @author linus
 * @since 1.0
 */
public class ColorsModule extends ConcurrentModule {
    //
    Config<Color> colorConfig = new ColorConfig("Color", "The primary client color", new Color(0, 225, 255), false, false);
    Config<Color> color1Config = new ColorConfig("Accent-Color", "The accent client color", new Color(0,225,5));
    Config<Color> color2Config = new ColorConfig("LOpat", "The accent client color", new Color(100,100,200));
    Config<Boolean> rainbowConfig = new BooleanConfig("Rainbow", "Renders rainbow colors for modules", false);

    /**
     *
     */
    public ColorsModule() {
        super("Colors", "Client color scheme", ModuleCategory.CLIENT);
    }

    public Color getColor() {
        return colorConfig.getValue();
    }

    public Color getColor(float alpha) {
        ColorConfig config = (ColorConfig) colorConfig;
        return new Color(config.getRed() / 255.0f, config.getGreen() / 255.0f, config.getBlue() / 255.0f, alpha);
    }

    public Color getColor(int alpha) {
        ColorConfig config = (ColorConfig) colorConfig;
        return new Color(config.getRed(), config.getGreen(), config.getBlue(), alpha);
    }

    public Integer getRGB() {
        return getColor().getRGB();
    }

    public int getRGB(int a) {
        return getColor(a).getRGB();
    }
}
