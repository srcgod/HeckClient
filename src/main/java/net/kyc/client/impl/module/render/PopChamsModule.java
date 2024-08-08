package net.kyc.client.impl.module.render;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import jdk.jshell.execution.Util;
import net.kyc.client.Heckk;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.NumberDisplay;
import net.kyc.client.api.config.setting.ColorConfig;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.PacketEvent;
import net.kyc.client.impl.event.network.PlayerTickEvent;
import net.kyc.client.impl.event.render.RenderWorldEvent;
import net.kyc.client.impl.event.world.TotemPopEvent;
import net.kyc.client.mixin.accessor.AccessorEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PopChamsModule extends ToggleModule {
    public Config<Color> color = new ColorConfig("handItemsColor", "The color of the chams", new Color(255, 0, 0, 60));
    public Config<Integer> ySpeed = new NumberConfig<>("ySpeed", "Maximum yaw rotation in degrees for one tick", -10, 0, 10, NumberDisplay.DEGREES);
    public Config<Integer> aSpeed = new NumberConfig<>("aSpeed", "Maximum yaw rotation in degrees for one tick", 1, 5, 100, NumberDisplay.DEGREES);

    public PopChamsModule() {
        super("PopChams", "Renders entity models through walls", ModuleCategory.RENDER);
    }

    private final CopyOnWriteArrayList<Person> popList = new CopyOnWriteArrayList<>();

    @Override
    public void onUpdate() {
    }

    @EventListener
    public void onPlayerUpdate(PlayerTickEvent event) {
        popList.forEach(person -> person.update(popList));
    }

    public void onRender3D(MatrixStack stack) {
        //System.out.println("Render 1");
        RenderSystem.depthMask(false);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 0, 1);
        //System.out.println("Render 2");
        popList.forEach(person -> {
            person.modelPlayer.leftPants.visible = false;
            person.modelPlayer.rightPants.visible = false;
            person.modelPlayer.leftSleeve.visible = false;
            person.modelPlayer.rightSleeve.visible = false;
            person.modelPlayer.jacket.visible = false;
            person.modelPlayer.hat.visible = false;
            renderEntity(stack, person.player, person.modelPlayer, person.getAlpha());
        });
        //System.out.println("Render 3");
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        //System.out.println("Render 4");
    }

    @EventListener
    public void onPacketReceive(PacketEvent.@NotNull Receive event) {
        if (event.getPacket() instanceof EntityStatusS2CPacket pac) {
            if (pac.getStatus() == EntityStatuses.USE_TOTEM_OF_UNDYING) {
                Entity ent = pac.getEntity(mc.world);
                if (!(ent instanceof PlayerEntity playerEntity)) return;
                PlayerEntity entity = new PlayerEntity(mc.world, BlockPos.ORIGIN, playerEntity.bodyYaw, new GameProfile(playerEntity.getUuid(), playerEntity.getName().getString())) {
                    @Override
                    public boolean isSpectator() {
                        return false;
                    }

                    @Override
                    public boolean isCreative() {
                        return false;
                    }
                };
                entity.copyPositionAndRotation(playerEntity);
                entity.bodyYaw = playerEntity.bodyYaw;
                entity.headYaw = playerEntity.headYaw;
                entity.handSwingProgress = playerEntity.handSwingProgress;
                entity.handSwingTicks = playerEntity.handSwingTicks;
                entity.setSneaking(playerEntity.isSneaking());
                entity.limbAnimator.setSpeed(playerEntity.limbAnimator.getSpeed());
                entity.limbAnimator.pos = playerEntity.limbAnimator.getPos();
                popList.add(new Person(entity));
            }
        }
    }

    public static float rad(float angle) {
        return (float) (angle * Math.PI / 180);
    }

    private void renderEntity(@NotNull MatrixStack matrices, @NotNull LivingEntity entity, @NotNull BipedEntityModel<PlayerEntity> modelBase, int alpha) {
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        ((AccessorEntity) entity).setPos(entity.getPos().add(0, (double) ySpeed.getValue() / 50, 0));

        matrices.push();
        matrices.translate((float) x, (float) y, (float) z);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(rad(180 - entity.bodyYaw)));
        prepareScale(matrices);

        modelBase.animateModel((PlayerEntity) entity, entity.limbAnimator.getPos(), entity.limbAnimator.getSpeed(), mc.getTickDelta());
        modelBase.setAngles((PlayerEntity) entity, entity.limbAnimator.getPos(), entity.limbAnimator.getSpeed(), entity.age, entity.headYaw - entity.bodyYaw, entity.getPitch());

        RenderSystem.enableBlend();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        modelBase.render(matrices, buffer, 10, 0, color.getValue().getRed() / 255f, color.getValue().getGreen() / 255f, color.getValue().getBlue() / 255f, alpha / 255f);
        tessellator.draw();
        RenderSystem.disableBlend();
        matrices.pop();
    }

    private static void prepareScale(@NotNull MatrixStack matrixStack) {
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.scale(1.6f, 1.8f, 1.6f);
        matrixStack.translate(0.0F, -1.501F, 0.0F);
    }

    private class Person {
        private final PlayerEntity player;
        private final PlayerEntityModel<PlayerEntity> modelPlayer;
        private int alpha;

        public Person(PlayerEntity player) {
            this.player = player;
            modelPlayer = new PlayerEntityModel<>(new EntityRendererFactory.Context(mc.getEntityRenderDispatcher(), mc.getItemRenderer(), mc.getBlockRenderManager(), mc.getEntityRenderDispatcher().getHeldItemRenderer(), mc.getResourceManager(), mc.getEntityModelLoader(), mc.textRenderer).getPart(EntityModelLayers.PLAYER), false);
            modelPlayer.getHead().scale(new Vector3f(-0.3f, -0.3f, -0.3f));
            alpha = color.getValue().getAlpha();
        }

        public void update(CopyOnWriteArrayList<Person> arrayList) {
            if (alpha <= 0) {
                arrayList.remove(this);
                player.kill();
                player.remove(Entity.RemovalReason.KILLED);
                player.onRemoved();
                return;
            }
            alpha -= aSpeed.getValue();
        }

        public int getAlpha() {
            return clamp(alpha, 0, 255);
        }

        public static int clamp(int num, int min, int max) {
            return num < min ? min : Math.min(num, max);
        }
    }
}
