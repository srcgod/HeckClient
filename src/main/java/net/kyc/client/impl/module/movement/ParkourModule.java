package net.kyc.client.impl.module.movement;

import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.network.PlayerTickEvent;

/**
 * @author linus
 * @since 1.0
 */
public class ParkourModule extends ToggleModule {

    private boolean override;

    /**
     *
     */
    public ParkourModule() {
        super("Parkour", "Automatically jumps at the edge of blocks", ModuleCategory.MOVEMENT);
    }

    @Override
    protected void onDisable() {
        super.onDisable();

        if (override)
        {
            override = false;
            mc.options.jumpKey.setPressed(false);
        }
    }

    @Override
    public void onUpdate() {

    }

    @EventListener
    public void onPlayerTick(PlayerTickEvent event) {
        if (mc.player.isOnGround() && !mc.player.isSneaking()
                && mc.world.isSpaceEmpty(mc.player.getBoundingBox().offset(0.0, -0.5, 0.0).expand(-0.001, 0.0, -0.001))) {
            mc.options.jumpKey.setPressed(true);
            override = true;
        }
        else
        {
            if (override)
            {
                override = false;
                mc.options.jumpKey.setPressed(false);
            }

        }
    }
}
