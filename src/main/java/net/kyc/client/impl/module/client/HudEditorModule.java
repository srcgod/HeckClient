package net.kyc.client.impl.module.client;

import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.gui.hud.HudEditor.HudEditorScreen;
import net.minecraft.text.Text;

/**
 * @author Bebra_tyan(Ferra13671)
 */

public class HudEditorModule extends ToggleModule {

    public static HudEditorScreen hudEditorScreen;

    public HudEditorModule() {
        super("HudEditor",
                "Opens the HudEditor screen.",
                ModuleCategory.CLIENT
        );

        hudEditorScreen = new HudEditorScreen(Text.of("HudEditor"));
    }

    @Override
    protected void onEnable() {
        mc.setScreen(hudEditorScreen);

        toggle(false);

        hudEditorScreen = new HudEditorScreen(Text.of("HudEditor"));
    }

    @Override
    public void onUpdate() {

    }
}
