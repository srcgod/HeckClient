package net.kyc.client.impl.gui.click.impl.config.setting;

import net.kyc.client.impl.gui.click.impl.config.ModuleButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.ColorConfig;
import net.kyc.client.api.render.RenderManager;
import net.kyc.client.api.render.anim.Animation;
import net.kyc.client.api.render.anim.Easing;
import net.kyc.client.impl.gui.click.ClickGuiScreen;
import net.kyc.client.impl.gui.click.impl.config.CategoryFrame;
import net.kyc.client.init.Modules;

import java.awt.*;

/**
 * @author heck
 * @since 1.0
 */
public class ColorButton extends ConfigButton<Color> {

    private boolean open;
    private final Animation pickerAnimation = new Animation(Easing.CUBIC_IN_OUT, 500);
    private float[] selectedColor;

    /**
     * @param frame
     * @param config
     * @param x
     * @param y
     */
    public ColorButton(CategoryFrame frame, ModuleButton moduleButton, Config<Color> config, float x, float y) {
        super(frame, moduleButton, config, x, y);
        float[] hsb = ((ColorConfig) config).getHsb();
        selectedColor = new float[] {hsb[0], hsb[1], 1.0f - hsb[2], hsb[3]};
    }

    @Override
    public void render(DrawContext context, float ix, float iy, float mouseX,
                       float mouseY, float delta) {
        x = ix;
        y = iy;
        //
        fill(context, ix + width - 40.0f, iy + 2.0f, 10.0f, 10.0f, ((ColorConfig) config).getRgb());
        RenderManager.renderText(context, config.getName(), ix + 2.0f, iy + 4.0f, -1);
        if (pickerAnimation.getScaledTime() > 0.01f) {
            ColorConfig colorConfig = (ColorConfig) config;
            if (ClickGuiScreen.MOUSE_LEFT_HOLD) {
                if (isMouseOver(mouseX, mouseY, x + 1.0f, y + height + 2.0f, width - 2.0f, width) && !colorConfig.isGlobal()) {
                    selectedColor[1] = (mouseX - (x + 1.0f)) / (width - 1.0f);
                    selectedColor[2] = (mouseY - (y + height + 2.0f)) / width;
                }
                if (isMouseOver(mouseX, mouseY, x + 1.0f, y + height + 4.0f + width, width - 2.0f, 10.0f) && !colorConfig.isGlobal()) {
                    selectedColor[0] = (mouseX - (x + 1.0f)) / (width - 1.0f);
                }
                if (colorConfig.allowAlpha() && isMouseOver(mouseX, mouseY, x + 1.0f, y + height + 17.0f + width, width - 2.0f, 10.0f)) {
                    selectedColor[3] = (mouseX - (x + 1.0f)) / (width - 1.0f);
                }
                Color color = Color.getHSBColor(MathHelper.clamp(selectedColor[0], 0.001f, 0.999f), MathHelper.clamp(selectedColor[1], 0.001f, 0.999f), 1.0f - MathHelper.clamp(selectedColor[2], 0.001f, 0.999f));
                color = new Color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, MathHelper.clamp(selectedColor[3], 0.0f, 1.0f));
                colorConfig.setValue(color);
            }
            float[] hsb = colorConfig.getHsb();
            int color = Color.HSBtoRGB(hsb[0], 1.0f, 1.0f);
            enableScissor((int) x, (int) (y + height), (int) (x + width), (int) (y + height + (getPickerHeight() * getScaledTime())));
            for (float i = 0.0f; i < width - 2.0f; i += 1.0f) {
                float hue = i / (width - 2.0f);
            fill(context, x + 1.0f + i, y + height + 4.0f + width, 1.0f, 10.0f, Color.getHSBColor(hue, 1.0f, 1.0f).getRGB());
            }
            fill(context, x + 1.0f + ((width - 2.0f) * hsb[0]), y + height + 4.0f + width, 1.0f, 10.0f, -1);
            fillGradientQuad(context, x + 1.0f, y + height + 2.0f, x + width - 1.0f, y + height + 2.0f + width, 0xffffffff, color, true);
            fillGradientQuad(context, x + 1.0f, y + height + 2.0f, x + width - 1.0f, y + height + 2.0f + width, 0, 0xff000000, false);
            fill(context, x + (width * hsb[1]), y + height + 1.0f + (width * (1.0f - hsb[2])), 2.0f, 2.0f, -1);
            if (colorConfig.allowAlpha()) {
                fillGradient(context, x + 1.0f, y + height + 17.0f + width, x + width - 1.0f, y + height + 27.0f + width, color, 0x7b7b7b);
                fill(context, x + 1.0f + ((width - 2.0f) * hsb[3]), y + height + 17.0f + width, 1.0f, 10.0f, -1);
            }
            if (!config.getContainer().getName().equalsIgnoreCase("Colors")) {
                Animation globalAnimation = colorConfig.getAnimation();
                if (globalAnimation.getScaledTime() > 0.01) {
                    fill(context, x + 1.0f, y + height + (colorConfig.allowAlpha() ? 29.0f : 17.0f) + width, width - 2.0f, 13.0f, Modules.CLICK_GUI.getColor(globalAnimation.getScaledTime()));
                }
                RenderManager.renderText(context, "ClientColor", x + 3.0f, y + height + (colorConfig.allowAlpha() ? 131.0f : 21.0f) + width, -1);
            }
            moduleButton.offset(getPickerHeight() * pickerAnimation.getScaledTime());
            ((CategoryFrame) frame).offset(getPickerHeight() * pickerAnimation.getScaledTime() * moduleButton.getScaledTime());
            disableScissor();
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isWithin(mouseX, mouseY) && button == 1) {
            open = !open;
            pickerAnimation.setState(open);
        }
        if (!config.getContainer().getName().equalsIgnoreCase("Colors") && isMouseOver(mouseX, mouseY,
                x + 1.0f, y + height + (((ColorConfig) config).allowAlpha() ? 29.0f : 17.0f) + width, width - 2.0f, 13.0f) && button == 0) {
            ColorConfig colorConfig = (ColorConfig) config;
            boolean val = !colorConfig.isGlobal();
            colorConfig.setGlobal(val);
            float[] hsb = ((ColorConfig) config).getHsb();
            selectedColor = new float[] {hsb[0], hsb[1], 1.0f - hsb[2], hsb[3]};
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {

    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {

    }

    public float getPickerHeight() {
        float pickerHeight = 16.0f;
        if (((ColorConfig) config).allowAlpha()) {
            pickerHeight += 12.0f;
        }
        if (!config.getContainer().getName().equalsIgnoreCase("Colors")) {
            pickerHeight += 15.0f;
        }
        return pickerHeight + width;
    }

    public float getScaledTime() {
        return pickerAnimation.getScaledTime();
    }
}
