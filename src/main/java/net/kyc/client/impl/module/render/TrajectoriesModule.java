package net.kyc.client.impl.module.render;

import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;

/**
 * @author linus
 * @since 1.0
 */
public class TrajectoriesModule extends ToggleModule {

    public TrajectoriesModule() {
        super("Trajectories", "Renders the trajectory path of projectiles", ModuleCategory.RENDER);
    }

    @Override
    public void onUpdate() {

    }
}
