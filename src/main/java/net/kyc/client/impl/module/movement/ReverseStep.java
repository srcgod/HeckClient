package net.kyc.client.impl.module.movement;

import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class ReverseStep extends ToggleModule {
    public ReverseStep() {
        super("ReverseStep", "lolol", ModuleCategory.MISC);
    }
    @Override
    public void onEnable() {
        try {
            Desktop.getDesktop().browse(URI.create("https://www.ayo"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        toggle(true);
    }

    @Override
    public void onUpdate() {

    }
}


