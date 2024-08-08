package net.kyc.client.util.world;

import net.kyc.client.mixin.IClientWorld;
import net.minecraft.advancement.criterion.EntityHurtPlayerCriterion;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.chunk.WorldChunk;
import net.kyc.client.util.Globals;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linus
 * @since 1.0
 */
public class BlockUtil implements Globals {
    /**
     * @return
     */
    public static List<BlockEntity> blockEntities() {
        List<BlockEntity> list = new ArrayList<>();
        for (WorldChunk chunk : loadedChunks()) {
            list.addAll(chunk.getBlockEntities().values());
        }
        return list;
    }

    /**
     * Credit https://github.com/BleachDev/BleachHack/blob/1.19.4/src/main/java/org/bleachhack/util/world/WorldUtils.java#L83
     *
     * @return
     */
    public static List<WorldChunk> loadedChunks() {
        List<WorldChunk> chunks = new ArrayList<>();
        int viewDist = mc.options.getViewDistance().getValue();
        for (int x = -viewDist; x <= viewDist; x++) {
            for (int z = -viewDist; z <= viewDist; z++) {
                WorldChunk chunk = mc.world.getChunkManager().getWorldChunk(
                        (int) mc.player.getX() / 16 + x, (int) mc.player.getZ() / 16 + z);
                if (chunk != null) {
                    chunks.add(chunk);
                }
            }
        }
        return chunks;
    }

    /**
     * @param pos
     * @return
     */
    public static boolean isBlockAccessible(BlockPos pos) {
        return mc.world.isAir(pos) && !mc.world.isAir(pos.add(0, -1, 0))
                && mc.world.isAir(pos.add(0, 1, 0)) && mc.world.isAir(pos.add(0, 2, 0));
    }


    /**
     * @param x
     * @param z
     * @return
     */
    public static boolean isBlockLoaded(double x, double z) {
        ChunkManager chunkManager = mc.world.getChunkManager();
        if (chunkManager != null) {
            return chunkManager.isChunkLoaded(ChunkSectionPos.getSectionCoord(x),
                    ChunkSectionPos.getSectionCoord(z));
        }
        return false;
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(mc.player.getX(), mc.player.getY() + (double) mc.player.getEyeHeight(mc.player.getPose()), mc.player.getZ());
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{mc.player.getYaw() + MathHelper.wrapDegrees(yaw - mc.player.getYaw()), mc.player.getPitch() + MathHelper.wrapDegrees(pitch - mc.player.getPitch())};
    }

    public static int getWorldActionId(ClientWorld world) {
        PendingUpdateManager pum = getUpdateManager(world);
        int p = pum.getSequence();
        pum.close();
        return p;
    }

    public static PendingUpdateManager getUpdateManager(ClientWorld world) {
        return ((IClientWorld) world).acquirePendingUpdateManager();
    }

    public static Vec3d[] convertVec3ds(Vec3d vec3d, Vec3d[] input) {
        Vec3d[] output = new Vec3d[input.length];
        for (int i = 0; i < input.length; ++i) {
            output[i] = vec3d.add(input[i]);
        }
        return output;
    }

    public static BlockPos vec3dToPos(Vec3d vec3d) {

        double x = vec3d.getZ();
        double y = vec3d.getY();
        double z = vec3d.getZ();

        return new BlockPos((int) x, (int) y, (int) z);
    }



    public static void clickBlock(BlockPos pos, Direction side, boolean rotate) {
        Vec3d directionVec = new Vec3d(pos.getX() + 0.5 + side.getVector().getX() * 0.5, pos.getY() + 0.5 + side.getVector().getY() * 0.5, pos.getZ() + 0.5 + side.getVector().getZ() * 0.5);
        if (rotate) {
            float[] angle = getLegitRotations(directionVec);
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(angle[0], angle[1], mc.player.isOnGround()));
        }
        BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
        mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, result, getWorldActionId(mc.world)));
    }
}
