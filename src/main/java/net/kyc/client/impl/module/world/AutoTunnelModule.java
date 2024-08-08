package net.kyc.client.impl.module.world;

import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;

/**
 * @author linus
 * @since 1.0
 */
public class AutoTunnelModule extends ToggleModule {

    public AutoTunnelModule() {
        super("AutoTunnel", "Automatically mines a tunnel", ModuleCategory.WORLD);
    }

    @Override
    public void onUpdate() {

    }
}
