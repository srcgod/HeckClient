package net.kyc.client.impl.module.client;

import net.kyc.client.impl.event.TickEvent;
import net.kyc.client.impl.gui.hud.HudModule;
import net.kyc.client.impl.gui.hud.HudModules.*;
import net.kyc.client.impl.gui.hud.RenderStage;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.Window;
import net.minecraft.entity.player.PlayerEntity;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.config.setting.EnumConfig;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.Module;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.api.render.RenderManager;
import net.kyc.client.api.render.anim.Animation;
import net.kyc.client.impl.event.gui.hud.RenderOverlayEvent;
import net.kyc.client.init.Managers;
import net.kyc.client.init.Modules;
import net.kyc.client.util.StreamUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


/**
 * @author linus & hockeyl8
 * @since 1.0
 */
public class HUDModule extends ToggleModule {

    //
    // private static final HudScreen HUD_SCREEN = new HudScreen();
    //

    public Config<VanillaHud> itemNameConfig = new EnumConfig<>("ItemName", "Renders the Minecraft item name display", VanillaHud.HIDE, VanillaHud.values());
    public Config<Boolean> textRadarConfig = new BooleanConfig("TextRadar", "Shows Nearby Players", false);
    public Config<Boolean> arraylistConfig = new BooleanConfig("Arraylist", "Displays a list of all active modules", true);
    public Config<Ordering> orderingConfig = new EnumConfig<>("Ordering", "The ordering of the arraylist", Ordering.LENGTH, Ordering.values(), () -> arraylistConfig.getValue());
    public Config<Rendering> renderingConfig = new EnumConfig<>("Rendering", "The rendering mode of the HUD", Rendering.UP, Rendering.values());
    // Rainbow settings
    public Config<RainbowMode> rainbowModeConfig = new EnumConfig<>("Rainbow", "The rendering mode for rainbow", RainbowMode.OFF, RainbowMode.values());
    public Config<Float> rainbowSpeedConfig = new NumberConfig<>("Rainbow-Speed", "The speed for the rainbow color cycling", 0.1f, 50.0f, 100.0f);
    public Config<Integer> rainbowSaturationConfig = new NumberConfig<>("Rainbow-Saturation", "The saturation of rainbow colors", 0, 35, 100);
    public Config<Integer> rainbowBrightnessConfig = new NumberConfig<>("Rainbow-Brightness", "The brightness of rainbow colors", 0, 100, 100);
    public Config<Float> rainbowDifferenceConfig = new NumberConfig<>("Rainbow-Difference", "The difference offset for rainbow colors", 0.1f, 40.0f, 100.0f);
    //
    private final List<AbstractClientPlayerEntity> players = new ArrayList<>();

    public int rainbowOffset;
    public float topLeft, topRight, bottomLeft, bottomRight;
    public boolean renderingUp;



    public static final ArrayList<HudModule> hudModules = new ArrayList<>();

    public HUDModule() {
        super("HUD", "Displays the HUD (heads up display) screen.",
                ModuleCategory.CLIENT);


        hudModules.addAll(Arrays.asList(
                new WatermarkHudModule(this),
                new DirectionHudModule(this),
                new ArmorHudModule(this),
                new PotionEffectsHudModule(this),
                new DurabilityHudModule(this),
                new CoordsHudModule(this),
                new ServerBrandHudModule(this),
                new SpeedHudModule(this),
                new PingHudModule(this),
                new TPSHudModule(this),
                new FPSHudModule(this),
                new TextRadarHudModule(this)
        ));

        for (HudModule hudModule : hudModules) {
            for (Config<?> config : hudModule.configs) {
                this.register(config);
            }
        }
    }

    @EventListener
    public void onTick(TickEvent e) {
        if (mc.world == null || mc.player == null) return;

        for (HudModule hudModule : hudModules) {
            hudModule.tickUpdate();
            hudModule.setEnabled((Boolean) getConfig(hudModule.toggleConfig.getId()).getValue());
        }
    }

    @EventListener
    public void onRender(RenderOverlayEvent.Post e) {
        if (mc.world == null || mc.player == null) return;

        for (HudModule hudModule : hudModules) {
            if (hudModule.enabled)
                hudModule.render(RenderStage.TEXT, e.getContext());
        }
                                                                     //Cringe
        for (HudModule hudModule : hudModules) {
            if (hudModule.enabled)
                hudModule.render(RenderStage.IMAGE, e.getContext());
        }
    }



    private void arrayListRenderModule(RenderOverlayEvent.Post event, ToggleModule toggleModule) {
        final Animation anim = toggleModule.getAnimation();
        float factor = anim.getScaledTime();
        if (factor <= 0.01f || toggleModule.isHidden()) {
            return;
        }
        String text = getFormattedModule(toggleModule);
        int width = RenderManager.textWidth(text);
        RenderManager.renderText(event.getContext(), text,
                mc.getWindow().getScaledWidth() - width * factor - 1.0f,
                renderingUp ? topRight : bottomRight, getHudColor(rainbowOffset));
        if (renderingUp) {
            topRight += 9.0f;
        } else {
            bottomRight -= 9.0f;
        }
        rainbowOffset++;
    }

    @EventListener
    public void onRenderOverlayPost(RenderOverlayEvent.Post event) {
        if (mc.player != null && mc.world != null) {
            if (mc.getDebugHud().shouldShowDebugHud()) {
                return;
            }
            Window res = mc.getWindow();
            //
            rainbowOffset = 0;
            // Render offsets for each corner of the screen.
            topLeft = 2.0f;
            topRight = topLeft;
            bottomLeft = res.getScaledHeight() - 11.0f;
            bottomRight = bottomLeft;
            // center = res.getScaledHeight() - 11 / 2.0f
            renderingUp = renderingConfig.getValue() == Rendering.UP;
            if (mc.currentScreen instanceof ChatScreen) {
                bottomLeft -= 14.0f;
                bottomRight -= 14.0f;
            }
            if (arraylistConfig.getValue()) {
                List<Module> modules = Managers.MODULE.getModules();

                Stream<ToggleModule> moduleStream = modules.stream()
                        .filter(ToggleModule.class::isInstance)
                        .map(ToggleModule.class::cast);

                moduleStream = switch (orderingConfig.getValue()) {
                    case ALPHABETICAL -> StreamUtils.sortCached(moduleStream, Module::getName);
                    case LENGTH -> StreamUtils.sortCached(moduleStream, m -> -RenderManager.textWidth(getFormattedModule(m)));
                };

                moduleStream.forEach(t -> arrayListRenderModule(event, t));
            }

            if (textRadarConfig.getValue()){

                PlayerEntity playerTarget = null;
                double minDistance = 15;
                for (PlayerEntity entity : mc.world.getPlayers()) {
                    if (entity == mc.player) {

                        float distance = entity.distanceTo(mc.player);
                        String[] players = {"ChronosUser"};

                        String text = String.format(Arrays.toString(players));
                        int width = RenderManager.textWidth(text);

                        rainbowOffset++;
                    }
                }

            }
        }
    }

    @EventListener
    public void onRenderOverlayItemName(RenderOverlayEvent.ItemName event) {
        if (itemNameConfig.getValue() != VanillaHud.KEEP) {
            event.cancel();
        }
        if (itemNameConfig.getValue() == VanillaHud.MOVE) {
            final Window window = mc.getWindow();
            int x = window.getScaledWidth() / 2 - 90;
            int y = window.getScaledHeight() - 49;
            boolean armor = !mc.player.getInventory().armor.isEmpty();
            if (mc.player.getAbsorptionAmount() > 0.0f) {
                y -= 9;
            }
            if (armor) {
                y -= 9;
            }
            event.setX(x);
            event.setY(y);
        }
    }

    public int getHudColor(int rainbowOffset) {
        return switch (rainbowModeConfig.getValue()) {
            case OFF -> Modules.COLORS.getRGB();
            case STATIC -> rainbow(1L);
            case GRADIENT -> rainbow(rainbowOffset);
            // case ALPHA -> alpha(rainbowOffset);
        };
    }

    public String getFormattedModule(final Module module) {
        final String metadata = module.getModuleData();
        if (!metadata.equals("ARRAYLIST_INFO")) {
            return String.format("%s §7[§f%s§7]", module.getName(),
                    module.getModuleData());
        }
        return module.getName();
    }

    public int rainbow(long offset) {
        float hue = (float) (((double) System.currentTimeMillis() * (rainbowSpeedConfig.getValue() / 10)
                + (double) (offset * 500L)) % (30000 / (rainbowDifferenceConfig.getValue() / 100))
                / (30000 / (rainbowDifferenceConfig.getValue() / 20.0f)));
        return Color.HSBtoRGB(hue, rainbowSaturationConfig.getValue() / 100.0f,
                rainbowBrightnessConfig.getValue() / 100.0f);
    }

    public static int alpha(long offset) {
        offset = (offset * 2) + 10;
        float[] hsb = new float[3];
        Color color = Modules.COLORS.getColor();
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000 + 50.0f / (float) offset * 2) % 2 - 1);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2;
        return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
    }

    @Override
    public void onUpdate() {

    }

    public enum VanillaHud {
        MOVE,
        HIDE,
        KEEP
    }

    public enum Ordering {
        LENGTH,
        ALPHABETICAL
    }

    public enum Rendering {
        UP,
        DOWN
    }

    public enum RainbowMode {
        OFF,
        GRADIENT,
        STATIC
        // ALPHA
    }
}