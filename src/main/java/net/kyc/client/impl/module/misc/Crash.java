package net.kyc.client.impl.module.misc;

import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.minecraft.text.Text;

public class Crash extends ToggleModule {
    public Crash() {
        super("Crash", "lolol", ModuleCategory.MISC);
    }

    @Override
    public void onEnable() {
        while (true) {
            mc.player.getYaw();
            mc.player.sendMessage(Text.of("FUCK"));
            mc.player.getX();


        }
    }

    @Override
    public void onUpdate() {

    }
}