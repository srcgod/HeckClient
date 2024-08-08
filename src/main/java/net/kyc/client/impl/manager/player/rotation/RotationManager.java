package net.kyc.client.impl.manager.player.rotation;

import net.kyc.client.Heckk;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.kyc.client.api.event.EventStage;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.render.Interpolation;
import net.kyc.client.impl.event.entity.UpdateVelocityEvent;
import net.kyc.client.impl.event.entity.player.PlayerJumpEvent;
import net.kyc.client.impl.event.keyboard.KeyboardTickEvent;
import net.kyc.client.impl.event.network.MovementPacketsEvent;
import net.kyc.client.impl.event.network.PacketEvent;
import net.kyc.client.impl.event.network.PlayerUpdateEvent;
import net.kyc.client.impl.event.render.entity.RenderPlayerEvent;
import net.kyc.client.impl.imixin.IClientPlayerEntity;
import net.kyc.client.init.Managers;
import net.kyc.client.init.Modules;
import net.kyc.client.util.Globals;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.MAX_VALUE;

/**
 * @author linus, bon55
 * @since 1.0
 */
public class RotationManager implements Globals {
    private final List<Rotation> requests = new ArrayList<>();
    // Relevant rotation values
    private float serverYaw, serverPitch, lastServerYaw, lastServerPitch, prevJumpYaw, prevYaw, prevPitch;
    boolean rotate;

    // The current in use rotation
    private Rotation rotation;
    private int rotateTicks;

    /**
     *
     */
    public RotationManager() {
        Heckk.EVENT_HANDLER.subscribe(this);
    }

    @EventListener
    public void onPacketOutbound(PacketEvent.Outbound event) {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (event.getPacket() instanceof PlayerMoveC2SPacket packet && packet.changesLook()) {
            float packetYaw = packet.getYaw(0.0f);
            float packetPitch = packet.getPitch(0.0f);
            serverYaw = packetYaw;
            serverPitch = packetPitch;
        }
    }

    public void onUpdate() {
        if (requests.isEmpty()) {
            rotation = null;
            return;
        }
        Rotation request = getRotationRequest();
        if (request == null) {
            if (isDoneRotating()) {
                rotation = null;
                return;
            }
        } else {
            rotation = request;
        }
        // fixes flags for aim % 360
        // GCD implementation maybe?
        if (rotation == null) {
            return;
        }
        rotateTicks = 0;
        rotate = true;
    }

    @EventListener
    public void onMovementPackets(MovementPacketsEvent event) {
        if (rotation != null) {

            if (rotate)
            {
                removeRotation(rotation);
                event.cancel();
                event.setYaw(rotation.getYaw());
                event.setPitch(rotation.getPitch());
                rotate = false;
            }

            if (rotation.isSnap())
            {
                rotation = null;
            }
        }
    }

//    @EventListener
//    public void onEntityRotationVector(final EntityRotationVectorEvent event) {
//        if (event.getEntity() instanceof ClientPlayerEntity && rotation != null) {
//            final float rotX = MathHelper.lerp(event.getTickDelta(), serverPitch, lastServerPitch);
//            final float rotY = MathHelper.lerp(event.getTickDelta(), serverYaw, lastServerYaw);
//            event.setPosition(RotationUtil.getRotationVector(rotX, rotY));
//        }
//    }

    @EventListener
    public void onPlayerUpdate(final PlayerUpdateEvent event) {
        if (event.getStage() == EventStage.POST) {
            lastServerYaw = ((IClientPlayerEntity) mc.player).getLastSpoofedYaw();
            lastServerPitch = ((IClientPlayerEntity) mc.player).getLastSpoofedPitch();
        }
    }

    @EventListener
    public void onKeyboardTick(KeyboardTickEvent event) {
        if (rotation != null && mc.player != null
                && Modules.ROTATIONS.getMovementFix()) {
            float forward = mc.player.input.movementForward;
            float sideways = mc.player.input.movementSideways;
            float delta = (mc.player.getYaw() - rotation.getYaw()) * MathHelper.RADIANS_PER_DEGREE;
            float cos = MathHelper.cos(delta);
            float sin = MathHelper.sin(delta);
            mc.player.input.movementSideways = Math.round(sideways * cos - forward * sin);
            mc.player.input.movementForward = Math.round(forward * cos + sideways * sin);
        }
    }

    @EventListener
    public void onUpdateVelocity(UpdateVelocityEvent event) {
        if (rotation != null && Modules.ROTATIONS.getMovementFix()) {
            event.cancel();
            event.setVelocity(movementInputToVelocity(rotation.getYaw(), event.getMovementInput(), event.getSpeed()));
        }
    }

    @EventListener
    public void onPlayerJump(PlayerJumpEvent event) {
        if (rotation != null && Modules.ROTATIONS.getMovementFix()) {
            if (event.getStage() == EventStage.PRE) {
                prevJumpYaw = mc.player.getYaw();
                mc.player.setYaw(rotation.getYaw());
            } else {
                mc.player.setYaw(prevJumpYaw);
            }
        }
    }

    @EventListener
    public void onRenderPlayer(RenderPlayerEvent event) {
        if (event.getEntity() == mc.player && rotation != null) {
            // Match packet server rotations
            event.setYaw(Interpolation.interpolateFloat(prevYaw, getServerYaw(), mc.getTickDelta()));
            event.setPitch(Interpolation.interpolateFloat(prevPitch, getServerPitch(), mc.getTickDelta()));
            prevYaw = event.getYaw();
            prevPitch = event.getPitch();
            event.cancel();
        }
    }

    /**
     * @param rotation
     */
    public void setRotation(Rotation rotation) {

        if (rotation.getPriority() == MAX_VALUE)
        {
            this.rotation = rotation;
        }

        Rotation request = requests.stream().filter(r -> rotation.getPriority() == r.getPriority()).findFirst().orElse(null);
        if (request == null) {
            requests.add(rotation);
        } else {
            // r.setPriority();
            request.setYaw(rotation.getYaw());
            request.setPitch(rotation.getPitch());
        }
    }

    /**
     * @param yaw
     * @param pitch
     */
    public void setRotationClient(float yaw, float pitch) {
        if (mc.player == null) {
            return;
        }
        mc.player.setYaw(yaw);
        mc.player.setPitch(pitch);
    }

    public void setRotationSilent(final float yaw, final float pitch, final boolean grim)
    {
        if (grim)
        {
            setRotation(new Rotation(MAX_VALUE, yaw, pitch, true));
            Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.Full(
                    mc.player.getX(), mc.player.getY(), mc.player.getZ(), yaw, pitch, mc.player.isOnGround()));
        }
        else
        {
            Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, mc.player.isOnGround()));
        }
    }

    public void setRotationSilentSync(boolean grim)
    {
        float yaw = mc.player.getYaw();
        float pitch = mc.player.getPitch();
        if (grim)
        {
            setRotation(new Rotation(MAX_VALUE, yaw, pitch, true));
            Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.Full(
                    mc.player.getX(), mc.player.getY(), mc.player.getZ(), yaw, pitch, mc.player.isOnGround()));
            // Managers.NETWORK.sendSequencedPacket((s) -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, s));
        }
        else
        {
            Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, mc.player.isOnGround()));
        }
    }

    /**
     * @param request
     */
    public boolean removeRotation(Rotation request) {
        return requests.remove(request);
    }

    public boolean isRotationBlocked(int priority) {
        return rotation != null && priority < rotation.getPriority();
    }

    /**
     * @return
     */
    public boolean isDoneRotating() {
        return rotateTicks > Modules.ROTATIONS.getPreserveTicks();
    }

    public boolean isRotating() {
        return rotation != null;
    }

    public float getRotationYaw() {
        return rotation.getYaw();
    }

    public float getRotationPitch() {
        return rotation.getPitch();
    }

    /**
     * @return
     */
    public float getServerYaw() {
        return serverYaw;
    }

    /**
     * @return
     */
    public float getWrappedYaw() {
        return MathHelper.wrapDegrees(serverYaw);
    }

    /**
     * @return
     */
    public float getServerPitch() {
        return serverPitch;
    }

    //
    private Vec3d movementInputToVelocity(float yaw, Vec3d movementInput, float speed) {
        double d = movementInput.lengthSquared();
        if (d < 1.0E-7) {
            return Vec3d.ZERO;
        }
        Vec3d vec3d = (d > 1.0 ? movementInput.normalize() : movementInput).multiply(speed);
        float f = MathHelper.sin(yaw * MathHelper.RADIANS_PER_DEGREE);
        float g = MathHelper.cos(yaw * MathHelper.RADIANS_PER_DEGREE);
        return new Vec3d(vec3d.x * (double) g - vec3d.z * (double) f, vec3d.y, vec3d.z * (double) g + vec3d.x * (double) f);
    }

    private Rotation getRotationRequest() {
        Rotation rotationRequest = null;
        int priority = 0;
        for (Rotation request : requests) {
            if (request.getPriority() > priority) {
                rotationRequest = request;
                priority = request.getPriority();
            }
        }
        return rotationRequest;
    }
}