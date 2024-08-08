package net.kyc.client.impl.module.render;

import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.render.CameraClipEvent;

/**
 * @author linus
 * @since 1.0
 */
public class ViewClipModule extends ToggleModule {

    Config<Float> distanceConfig = new NumberConfig<>("Distance", "The third-person camera clip distance", 1.0f, 3.5f, 20.0f);

    public ViewClipModule() {
        super("ViewClip", "Clips your third-person camera through blocks", ModuleCategory.RENDER);
    }

    @EventListener
    public void onCameraClip(CameraClipEvent event) {
        event.cancel();
        event.setDistance(distanceConfig.getValue());
    }

    @Override
    public void onUpdate() {

    }
}
