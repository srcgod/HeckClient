package net.kyc.client.impl.module.combat;

import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;

/**
 * @author linus
 * @since 1.0
 */
public class BedAuraModule extends ToggleModule {
    /**
     *
     */
    public BedAuraModule() {
        super("BedAura", "Automatically places and explodes beds",
                ModuleCategory.COMBAT);
    }

    @Override
    public void onUpdate() {

    }
}
