package net.kyc.client.impl.module.world;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShapes;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.event.EventStage;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.TickEvent;
import net.kyc.client.impl.event.world.BlockCollisionEvent;
import net.kyc.client.init.Managers;
import net.kyc.client.util.world.BlockUtil;

/**
 * @author linus
 * @since 1.0
 */
public class AvoidModule extends ToggleModule {
    Config<Boolean> voidConfig = new BooleanConfig("Void", "Prevents player from falling into the void", true);
    Config<Boolean> fireConfig = new BooleanConfig("Fire", "Prevents player from walking into fire", false);
    Config<Boolean> berryBushConfig = new BooleanConfig("BerryBush", "Prevents player from walking into sweet berry bushes", false);
    Config<Boolean> cactiConfig = new BooleanConfig("Cactus", "Prevents player from walking into cacti", false);
    Config<Boolean> unloadedConfig = new BooleanConfig("Unloaded", "Prevents player from entering chunks that haven't been loaded", false);

    public AvoidModule() {
        super("Avoid", "Prevents player from entering harmful areas",
                ModuleCategory.WORLD);
    }

    @EventListener
    public void onTick(TickEvent event) {
        if (event.getStage() == EventStage.PRE && voidConfig.getValue()
                && !mc.player.isSpectator()
                && mc.player.getY() < mc.world.getBottomY()) {
            // sendModuleMessage(Formatting.RED + "Prevented player from " +
            //    "falling into void!");
            Managers.MOVEMENT.setMotionY(0.0);
        }
    }

    @EventListener
    public void onBlockCollision(BlockCollisionEvent event) {
        BlockPos pos = event.getPos();
        if (fireConfig.getValue() && event.getBlock() == Blocks.FIRE
                && mc.player.getY() < pos.getY() + 1.0
                || cactiConfig.getValue()
                && event.getBlock() == Blocks.CACTUS
                || berryBushConfig.getValue()
                && event.getBlock() == Blocks.SWEET_BERRY_BUSH
                || unloadedConfig.getValue()
                && !BlockUtil.isBlockLoaded(pos.getX(), pos.getZ())) {
            event.cancel();
            event.setVoxelShape(VoxelShapes.fullCube());
        }
    }

    @Override
    public void onUpdate() {

    }
}
