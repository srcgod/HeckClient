package net.kyc.client.impl.module.misc;

import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.EventStage;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.TickEvent;

/**
 * @author linus
 * @since 1.0
 */
public class AutoMapartModule extends ToggleModule {
    //
    Config<Float> rangeConfig = new NumberConfig<>("Range", "The range to place maps around the player", 0.1f, 6.0f, 10.0f);
    Config<Boolean> rotateConfig = new BooleanConfig("Rotate", "Rotates before placing maps", false);

    /**
     *
     */
    public AutoMapartModule() {
        super("AutoMapart", "Automatically places maparts on walls",
                ModuleCategory.MISC);
    }

    @EventListener
    public void onTick(TickEvent event) {
        if (event.getStage() == EventStage.PRE) {

        }
    }

    @Override
    public void onUpdate() {

    }
}
