package net.kyc.client.impl.module.movement;

import net.minecraft.block.Blocks;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.mixin.accessor.AccessorAbstractBlock;

/**
 * @author linus
 * @since 1.0
 */
public class IceSpeedModule extends ToggleModule {

    /**
     *
     */
    public IceSpeedModule() {
        super("IceSpeed", "Modifies the walking speed on ice",
                ModuleCategory.MOVEMENT);
    }

    @Override
    public void onEnable() {
        if (mc.world == null) {
            return;
        }
        ((AccessorAbstractBlock) Blocks.ICE).setSlipperiness(0.4f);
        ((AccessorAbstractBlock) Blocks.PACKED_ICE).setSlipperiness(0.4f);
        ((AccessorAbstractBlock) Blocks.BLUE_ICE).setSlipperiness(0.4f);
        ((AccessorAbstractBlock) Blocks.FROSTED_ICE).setSlipperiness(0.4f);
    }

    @Override
    public void onDisable() {
        if (mc.world == null) {
            return;
        }
        ((AccessorAbstractBlock) Blocks.ICE).setSlipperiness(0.98f);
        ((AccessorAbstractBlock) Blocks.PACKED_ICE).setSlipperiness(0.98f);
        ((AccessorAbstractBlock) Blocks.BLUE_ICE).setSlipperiness(0.98f);
        ((AccessorAbstractBlock) Blocks.FROSTED_ICE).setSlipperiness(0.98f);
    }

    @Override
    public void onUpdate() {

    }
}
