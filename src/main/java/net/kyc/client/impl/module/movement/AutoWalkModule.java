package net.kyc.client.impl.module.movement;

import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.event.EventStage;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.TickEvent;

/**
 * @author linus
 * @since 1.0
 */
public class AutoWalkModule extends ToggleModule {
    //
    Config<Boolean> lockConfig = new BooleanConfig("Lock", "Stops movement when sneaking or jumping", false);

    /**
     *
     */
    public AutoWalkModule() {
        super("AutoWalk", "Automatically moves forward", ModuleCategory.MOVEMENT);
    }

    @Override
    public void onDisable() {
        mc.options.forwardKey.setPressed(false);
    }

    @Override
    public void onUpdate() {

    }

    @EventListener
    public void onTick(TickEvent event) {
        if (event.getStage() == EventStage.PRE) {
            mc.options.forwardKey.setPressed(!mc.options.sneakKey.isPressed()
                    && (!lockConfig.getValue() || (!mc.options.jumpKey.isPressed() && mc.player.isOnGround())));
        }
    }
}
