package net.kyc.client.impl.gui.click.impl.config.setting;

import net.kyc.client.impl.gui.click.impl.config.ModuleButton;
import net.minecraft.client.gui.DrawContext;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.render.RenderManager;
import net.kyc.client.api.render.anim.Animation;
import net.kyc.client.impl.gui.click.impl.config.CategoryFrame;
import net.kyc.client.init.Modules;

/**
 * @author linus
 * @see Config
 * @since 1.0
 */
public class CheckboxButton extends ConfigButton<Boolean> {

    /**
     * @param frame
     * @param config
     */
    public CheckboxButton(CategoryFrame frame, ModuleButton moduleButton, Config<Boolean> config, float x, float y) {
        super(frame, moduleButton, config, x, y);
        config.getAnimation().setStateHard(config.getValue());
    }

    /**
     * @param context
     * @param ix
     * @param iy
     * @param mouseX
     * @param mouseY
     * @param delta
     */
    @Override
    public void render(DrawContext context, float ix, float iy, float mouseX,
                       float mouseY, float delta) {
        x = ix;
        y = iy;
        Animation checkboxAnimation = config.getAnimation();
        rectGradient(context, checkboxAnimation.getScaledTime() > 0.01f ?  Modules.CLICK_GUI.getColor(checkboxAnimation.getScaledTime()) : 0x000000,
                checkboxAnimation.getScaledTime() > 0.01f ?  Modules.CLICK_GUI.getColor1(checkboxAnimation.getScaledTime()) : 0x000000);
        RenderManager.renderText(context, config.getName(), ix + 2.0f, iy + 4.0f, -1); // -1
                                                                                                        // 0x000000
    }                                                           // ix + 2.0f     iy + 4.0f   col -1

    /**
     * @param mouseX
     * @param mouseY
     * @param button
     */
    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isWithin(mouseX, mouseY)) {
            if (button == 0) {
                boolean val = config.getValue();
                config.setValue(!val);
            }
        }
    }

    /**
     * @param mouseX
     * @param mouseY
     * @param button
     */
    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {

    }

    /**
     * @param keyCode
     * @param scanCode
     * @param modifiers
     */
    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {

    }

}
