package net.kyc.client.impl.module.render;

import net.minecraft.util.math.Vec3d;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.EventStage;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.network.PlayerUpdateEvent;
import net.kyc.client.impl.event.render.RenderWorldEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author linus
 * @since 1.0
 */
public class BreadcrumbsModule extends ToggleModule {

    private final Map<Vec3d, Long> positions = new ConcurrentHashMap<>();
    Config<Boolean> infiniteConfig = new BooleanConfig("Infinite", "Renders breadcrumbs for all positions since toggle", true);
    Config<Float> maxTimeConfig = new NumberConfig<>("MaxPosition", "The maximum time for a given position", 1.0f, 2.0f, 20.0f);

    public BreadcrumbsModule() {
        super("Breadcrumbs", "Renders a line connecting all previous positions", ModuleCategory.RENDER);
    }

    @Override
    public void onDisable() {
        positions.clear();
    }

    @Override
    public void onUpdate() {

    }

    @EventListener
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (event.getStage() != EventStage.PRE) {
            return;
        }
        positions.put(new Vec3d(mc.player.getX(), mc.player.getBoundingBox().minY, mc.player.getZ()), System.currentTimeMillis());
        if (!infiniteConfig.getValue()) {
            positions.forEach((p, t) ->
            {
                if (System.currentTimeMillis() - t >= maxTimeConfig.getValue() * 1000) {
                    positions.remove(p);
                }
            });
        }
    }

    @EventListener
    public void onRenderWorld(RenderWorldEvent event) {

    }
}
