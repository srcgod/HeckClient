package net.kyc.client.impl.module.combat;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.RotationModule;
import net.kyc.client.api.render.RenderManager;
import net.kyc.client.impl.event.network.PacketEvent;
import net.kyc.client.impl.event.render.RenderWorldEvent;
import net.kyc.client.impl.manager.player.interaction.RotationCallback;
import net.kyc.client.impl.module.client.RotationsModule;
import net.kyc.client.impl.module.world.SpeedmineModule;
import net.kyc.client.init.Managers;
import net.kyc.client.init.Modules;
import net.kyc.client.util.player.InventoryUtil;
import net.kyc.client.util.render.animation.TimeAnimation;
import net.kyc.client.util.world.BlockUtil;
import net.minecraft.block.BlockState;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.Map;

public class HandBlockModule extends RotationModule {
    Config<Float> rangeConfig = new NumberConfig<>("Range", "The range to mine blocks", 0.1f, 4.0f, 5.0f);
    Config<Boolean> silentConfig = new BooleanConfig("Silent", "Silent Switch", false);
    Config<Boolean> strictDirectionConfig = new BooleanConfig("StrictDirection", "Strict Direction", true);
    Config<Boolean> swingConfig = new BooleanConfig("Swing", "Swing your Hand", false);
    Config<Boolean> rotateConfig = new BooleanConfig("Rotate", "Rotates when mining the block", true);
    Config<Boolean> grimConfig = new BooleanConfig("Grim", "Grim AC?", true);
    Config<Boolean> renderConfig = new BooleanConfig("Render", "Render Box", false);



    public HandBlockModule() {
        super("HandBlock", "Places Obsidian at your Crossair", ModuleCategory.COMBAT);
    }

    @Override
    public void onEnable() {

        int prev = 0;
        int slot = 0;

        findSlot(prev, slot);

        assert mc.crosshairTarget != null;
        Vec3d crossairTarget = mc.crosshairTarget.getPos();

        BlockPos blockTarget = BlockUtil.vec3dToPos(crossairTarget);

        if (!silentConfig.getValue()) {
            //Managers.INVENTORY.setSlot(slot);
        }

        Managers.INTERACT.placeBlock(blockTarget, slot, strictDirectionConfig.getValue(), swingConfig.getValue(), (state, angles) ->
        {
            if (rotateConfig.getValue())
            {
                if (state)
                {
                    Managers.ROTATION.setRotationSilent(angles[0], angles[1], grimConfig.getValue());
                }
                else
                {
                    Managers.ROTATION.setRotationSilentSync(grimConfig.getValue());
                }
            }
        });



        if (!silentConfig.getValue())
        {
           // Managers.INVENTORY.setSlot(prev);
        }
    }

    public void findSlot(int prev, int slot){
        prev = mc.player.getInventory().selectedSlot;
        slot = mc.player.getInventory().getSlotWithStack(Items.OBSIDIAN.getDefaultStack());
    }



}
