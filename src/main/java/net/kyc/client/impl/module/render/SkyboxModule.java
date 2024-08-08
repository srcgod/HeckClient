package net.kyc.client.impl.module.render;

import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.config.setting.ColorConfig;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.EventStage;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.TickEvent;
import net.kyc.client.impl.event.network.PacketEvent;
import net.kyc.client.impl.event.world.SkyboxEvent;

import java.awt.*;

/**
 * @author linus
 * @since 1.0
 */
public class SkyboxModule extends ToggleModule {

    Config<Integer> dayTimeConfig = new NumberConfig<>("WorldTime", "The world time of day", 0, 6000, 24000);
    Config<Boolean> skyConfig = new BooleanConfig("Sky", "Changes the world skybox color", true);
    Config<Color> skyColorConfig = new ColorConfig("SkyColor", "The color for the world skybox", new Color(255, 0, 0), false, true, () -> skyConfig.getValue());
    Config<Boolean> cloudConfig = new BooleanConfig("Cloud", "Changes the world cloud color", false);
    Config<Color> cloudColorConfig = new ColorConfig("CloudColor", "The color for the world clouds", new Color(255, 0, 0), false, true, () -> cloudConfig.getValue());
    Config<Boolean> fogConfig = new BooleanConfig("Fog", "Changes the world fog color", false);
    Config<Color> fogColorConfig = new ColorConfig("FogColor", "The color for the world fog", new Color(255, 0, 0), false, true, () -> fogConfig.getValue());

    public SkyboxModule() {
        super("Skybox", "Changes the rendering of the world skybox", ModuleCategory.RENDER);
    }

//    @Override
//    public void onEnable()
//    {
//        if (mc.player == null || mc.worldRenderer == null)
//        {
//            return;
//        }
//        if (ambientConfig.getValue())
//        {
//            ((AccessorLightmapTextureManager) mc.gameRenderer.getLightmapTextureManager()).setUpdateLightmap(true);
//            RenderUtil.reloadRenders(true);
//        }
//    }

//    @Override
//    public void onDisable()
//    {
//        if (mc.player == null || mc.worldRenderer == null)
//        {
//            return;
//        }
//        if (ambientConfig.getValue())
//        {
//            RenderUtil.reloadRenders(true);
//        }
//    }

//    @EventListener
//    public void onConfigUpdate(ConfigUpdateEvent event)
//    {
//        if (event.getStage() != EventStage.POST || mc.player == null
//                || mc.worldRenderer == null)
//        {
//            return;
//        }
//        if (event.getConfig() == ambientConfig)
//        {
//            if (ambientConfig.getValue())
//            {
//                ((AccessorLightmapTextureManager) mc.gameRenderer.getLightmapTextureManager()).setUpdateLightmap(true);
//                RenderUtil.reloadRenders(true);
//            }
//            else
//            {
//                RenderUtil.reloadRenders(true);
//            }
//        }
//        else if (event.getConfig() == ambientColorConfig)
//        {
//            RenderUtil.reloadRenders(true);
//        }
//    }

    @EventListener
    public void onTick(TickEvent event) {
        if (event.getStage() == EventStage.POST) {
            mc.world.setTimeOfDay(dayTimeConfig.getValue());
        }
    }

    @EventListener
    public void onPacketInbound(PacketEvent.Inbound event) {
        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket) {
            event.cancel();
        }
    }

    @EventListener
    public void onSkyboxSky(SkyboxEvent.Sky event) {
        if (skyConfig.getValue()) {
            event.cancel();
            event.setColor(skyColorConfig.getValue());
        }
    }

    @EventListener
    public void onSkyboxCloud(SkyboxEvent.Cloud event) {
        if (cloudConfig.getValue()) {
            event.cancel();
            event.setColor(cloudColorConfig.getValue());
        }
    }

    @EventListener
    public void onSkyboxFog(SkyboxEvent.Fog event) {
        if (fogConfig.getValue()) {
            event.cancel();
            event.setColor(fogColorConfig.getValue());
        }
    }

    @Override
    public void onUpdate() {

    }

//    @EventListener
//    public void onAmbientColor(AmbientColorEvent event)
//    {
//        if (ambientConfig.getValue())
//        {
//            event.cancel();
//            event.setColor(ambientColorConfig.getValue());
//        }
//    }
}
