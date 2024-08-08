package net.kyc.client.impl.module.movement;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.LlamaEntity;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.event.EventStage;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.TickEvent;
import net.kyc.client.impl.event.entity.LookDirectionEvent;

/**
 * @author linus
 * @since 1.0
 */
public class YawModule extends ToggleModule {

    Config<Boolean> lockConfig = new BooleanConfig("Lock", "Locks the yaw in cardinal direction", false);

    /**
     *
     */
    public YawModule() {
        super("Yaw", "Locks player yaw to a cardinal axis",
                ModuleCategory.MOVEMENT);
    }

    @EventListener
    public void onTick(TickEvent event) {
        if (event.getStage() == EventStage.PRE) {
            float yaw = Math.round(mc.player.getYaw() / 45.0f) * 45.0f;
            Entity vehicle = mc.player.getVehicle();
            if (vehicle != null) {
                vehicle.setYaw(yaw);
                if (vehicle instanceof LlamaEntity llama) {
                    llama.setHeadYaw(yaw);
                }
                return;
            }
            mc.player.setYaw(yaw);
            mc.player.setHeadYaw(yaw);
        }
    }

    @EventListener
    public void onLookDirection(LookDirectionEvent event) {
        if (lockConfig.getValue()) {
            event.cancel();
        }
    }

    @Override
    public void onUpdate() {

    }
}
