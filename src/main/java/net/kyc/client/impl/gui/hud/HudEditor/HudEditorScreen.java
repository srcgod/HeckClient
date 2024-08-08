package net.kyc.client.impl.gui.hud.HudEditor;

import com.google.common.collect.Sets;
import net.kyc.client.impl.gui.hud.HudModule;
import net.kyc.client.impl.module.client.HUDModule;
import net.kyc.client.impl.module.client.HudEditorModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Set;

/**
 * HUD Editor. It can be used to change the position of HUD modules.
 *
 * @author Bebra_tyan(Ferra13671)
 */

public class HudEditorScreen extends Screen {


    public HudEditorScreen(Text title) {
        super(title);
    }

    private final Set<HudModuleButton> hudModuleButtons = Sets.newHashSet();

    @Override
    public void init() {
        for (HudModule component : HUDModule.hudModules) {
            hudModuleButtons.add(new HudModuleButton(0, component));
        }
    }

    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
        for (HudModuleButton button : hudModuleButtons) {
            button.updateButton(mouseX, mouseY);
            button.renderButton();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        for (HudModuleButton button : hudModuleButtons) {
            button.mouseClicked((int) mouseX, (int) mouseY);
        }
        return super.mouseClicked(mouseX,mouseY,mouseButton);
    }

    @Override
    public void removed() {
        HudEditorModule.hudEditorScreen = new HudEditorScreen(Text.of("HudEditor"));
    }
}
