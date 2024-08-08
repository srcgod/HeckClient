package net.kyc.client.impl.gui.click.impl.config;

import net.kyc.client.impl.gui.click.ClickGuiScreen;
import net.kyc.client.impl.gui.click.impl.config.component.Frame;
import net.kyc.client.impl.gui.click.impl.config.setting.ColorButton;
import net.kyc.client.impl.gui.click.impl.config.setting.ConfigButton;
import net.minecraft.client.gui.DrawContext;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.module.Module;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.render.RenderManager;
import net.kyc.client.api.render.anim.Animation;
import net.kyc.client.api.render.anim.Easing;
import net.kyc.client.init.Managers;
import net.kyc.client.init.Modules;
import net.kyc.client.util.string.EnumFormatter;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Configuration {@link Frame} (aka the "ClickGui" frames) which
 * allows the user to configure a {@link Module}'s {@link Config} values.
 *
 * @author linus
 * @see Frame
 * @see Module
 * @see Config
 * @since 1.0
 */
public class CategoryFrame extends Frame {
    //
    private final String name;
    private final ModuleCategory category;
    // private final Identifier categoryIcon;
    // module components
    private final List<ModuleButton> moduleButtons =
            new CopyOnWriteArrayList<>();
    // global module offset
    private float off, inner;
    private boolean open;
    private boolean drag;
    //
    private final Animation categoryAnimation = new Animation(Easing.CUBIC_IN_OUT, 5, false);

    /**
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public CategoryFrame(ModuleCategory category, float x, float y,
                         float width, float height) {
        super(x, y, width, height);
        this.category = category;
        this.name = EnumFormatter.formatEnum(category);
        for (Module module : Managers.MODULE.getModules()) {
            if (module.getCategory() == category) {
                moduleButtons.add(new ModuleButton(module, this, x, y));
            }
        }
        categoryAnimation.setState(true);
        open = true;
    }

    /**
     * @param category
     * @param x
     * @param y
     */
    public CategoryFrame(ModuleCategory category, float x, float y) {
        this(category, x, y, 105.0f, 15.0f);
    }

    /**
     * @param context
     * @param mouseX
     * @param mouseY
     * @param delta
     */
    @Override
    public void render(DrawContext context, float mouseX, float mouseY, float delta) {
        if (drag) {
            x += ClickGuiScreen.MOUSE_X - px;
            y += ClickGuiScreen.MOUSE_Y - py;
        }
        fheight = 2.0f;
        for (ModuleButton moduleButton : moduleButtons) {
            // account for button height
            fheight += moduleButton.getHeight() + 1.0f;
            if (moduleButton.getScaledTime() < 0.01f) {
                continue;
            }
            fheight += 3.0f * moduleButton.getScaledTime();
            for (ConfigButton<?> configButton : moduleButton.getConfigButtons()) {
                if (!configButton.getConfig().isVisible()) {
                    continue;
                }
                // config button height may vary
                fheight += configButton.getHeight() * moduleButton.getScaledTime();
                if (configButton instanceof ColorButton colorPicker && colorPicker.getScaledTime() > 0.01f) {
                    fheight += colorPicker.getPickerHeight() * colorPicker.getScaledTime() * moduleButton.getScaledTime();
                }
            }
        }


        if (y < -(fheight - 10)) {
            y = -(fheight - 10);
        }
        if (y > mc.getWindow().getHeight() - 10) {
            y = mc.getWindow().getHeight() - 10;
        }
        //alpha
        rect(context, Modules.CLICK_GUI.getColor(2f)); // 2f
        RenderManager.renderText(context, name, x + 3.0f, y + 4.0f, -1);
        if (categoryAnimation.getScaledTime() > 0.01f) {
            enableScissor((int) x, (int) (y + height), (int) (x + width), (int) (y + height + fheight * categoryAnimation.getScaledTime()));
            fill(context, x, y + height, width, fheight, 0x77000000); //0x77000000
            off = y + height + 1.0f;
            inner = off;
            for (ModuleButton moduleButton : moduleButtons) {
                moduleButton.render(context, x + 1.0f, inner + 1.0f, mouseX, mouseY, delta);
                off += (float) ((moduleButton.getHeight() + 1.0f) * categoryAnimation.getScaledTime());
                inner += moduleButton.getHeight() + 1.0f;
            }
            disableScissor();

        }
        // update previous position
        px = ClickGuiScreen.MOUSE_X;
        py = ClickGuiScreen.MOUSE_Y;
    }

    /**
     * @param mouseX
     * @param mouseY
     * @param mouseButton
     */
    @Override
    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_RIGHT && isWithin(mouseX, mouseY)) {
            open = !open;
            categoryAnimation.setState(open);
        }
        if (open) {
            for (ModuleButton button : moduleButtons) {
                button.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    /**
     * @param mouseX
     * @param mouseY
     * @param mouseButton
     */
    @Override
    public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        drag = false;
        if (open) {
            for (ModuleButton button : moduleButtons) {
                button.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }

    /**
     * @param keyCode
     * @param scanCode
     * @param modifiers
     */
    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
        if (open) {
            for (ModuleButton button : moduleButtons) {
                button.keyPressed(keyCode, scanCode, modifiers);
            }
        }
    }

    /**
     * @param mx
     * @param my
     * @return
     */
    public boolean isWithinTotal(float mx, float my) {
        return isMouseOver(mx, my, x, y, width, getTotalHeight());
    }

    /**
     * Update global offset
     *
     * @param in The offset
     */
    public void offset(float in) {
        off += in;
        inner += in;
    }

    /**
     * @return
     */
    public ModuleCategory getCategory() {
        return category;
    }

    /**
     * Gets the total height of the frame
     *
     * @return The total height
     */
    public float getTotalHeight() {
        return height + fheight;
    }

    /**
     * @return
     */
    public List<ModuleButton> getModuleButtons() {
        return moduleButtons;
    }

    public void setDragging(boolean drag) {
        this.drag = drag;
    }

    public boolean isDragging() {
        return drag;
    }
}
