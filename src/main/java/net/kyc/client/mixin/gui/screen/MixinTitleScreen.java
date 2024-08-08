package net.kyc.client.mixin.gui.screen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyc.client.HeckMod;
import net.kyc.client.init.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.AccessibilityOnboardingButtons;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.option.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screen.option.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.client.realms.gui.screen.RealmsNotificationsScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

import org.jetbrains.annotations.Nullable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xgraza
 * @since 03/28/24
 */
@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {

    @Shadow
    @Nullable
    private SplashTextRenderer splashText;

    @Shadow
    @Final
    public static Text COPYRIGHT;

    @Shadow
    protected abstract void initWidgetsDemo(int y, int spacingY);

    @Shadow
    protected abstract void initWidgetsNormal(int y, int spacingY);

    @Shadow
    @Nullable
    private RealmsNotificationsScreen realmsNotificationGui;

    @Shadow
    protected abstract boolean isRealmsNotificationsGuiDisplayed();

    @Shadow
    private long backgroundFadeStart;

    @Shadow
    @Final
    private boolean doBackgroundFade;

    private List<String> githubCommits = new ArrayList<>();

    public MixinTitleScreen(Text title) {
        super(title);
    }


    @Inject(method = "render", at = @At("TAIL"))
    public void hookRender(final DrawContext context, final int mouseX, final int mouseY, final float delta, final CallbackInfo info) {
        float f = this.doBackgroundFade ? (float) (Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0f : 1.0f;
        float g = this.doBackgroundFade ? MathHelper.clamp(f - 1.0f, 0.0f, 1.0f) : 1.0f;
        int i = MathHelper.ceil(g * 255.0f) << 24;
        if ((i & 0xFC000000) == 0) {
            return;
        }
        context.drawTextWithShadow(client.textRenderer, "Heckk " + HeckMod.MOD_VER + " by Jailouter and ChronosUser",
                2, (int) 7.5, Modules.COLORS.getRGB() | i);

        int yOffset = 20;
        for (String commit : githubCommits) {
            context.drawTextWithShadow(client.textRenderer, commit, 2, yOffset, Modules.COLORS.getRGB() | i);
            yOffset += client.textRenderer.fontHeight + 4;
        }
    }

    @Inject(method = "init", at = @At(value = "HEAD"), cancellable = true)
    private void hookInit(CallbackInfo ci) {
        ci.cancel();
        if (this.splashText == null) {
            this.splashText = this.client.getSplashTextLoader().get();
        }
        int i = this.textRenderer.getWidth(COPYRIGHT);
        int j = this.width - i - 2;
        int k = 24;
        int l = this.height / 4 + 48;
        if (this.client.isDemo()) {
            this.initWidgetsDemo(l, 24);
        } else {
            this.initWidgetsNormal(l, 24);
        }
        TextIconButtonWidget textIconButtonWidget = this.addDrawableChild(AccessibilityOnboardingButtons.createLanguageButton(20, button -> this.client.setScreen(new LanguageOptionsScreen((Screen) this, this.client.options, this.client.getLanguageManager())), true));
        textIconButtonWidget.setPosition(this.width / 2 - 124, l + 72 + 24);
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("menu.options"), button -> this.client.setScreen(new OptionsScreen(this, this.client.options))).dimensions(this.width / 2 - 100, l + 72 + 24, 98, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("menu.quit"), button -> this.client.scheduleStop()).dimensions(this.width / 2 + 2, l + 72 + 24, 98, 20).build());
        TextIconButtonWidget textIconButtonWidget2 = this.addDrawableChild(AccessibilityOnboardingButtons.createAccessibilityButton(20, button -> this.client.setScreen(new AccessibilityOptionsScreen(this, this.client.options)), true));
        textIconButtonWidget2.setPosition(this.width / 2 + 104, l + 72 + 24);
        this.addDrawableChild(new PressableTextWidget(j, this.height - 10, i, 10, COPYRIGHT, button -> this.client.setScreen(new CreditsAndAttributionScreen(this)), this.textRenderer));
        if (this.realmsNotificationGui == null) {
            this.realmsNotificationGui = new RealmsNotificationsScreen();
        }
        if (this.isRealmsNotificationsGuiDisplayed()) {
            this.realmsNotificationGui.init(this.client, this.width, this.height);
        }

        fetchGithubCommits();
    }

    private void fetchGithubCommits() {
        String url = "https://api.github.com/repos/JailOuter/HeckClient/commits?per_page=15";
        String token = "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";
        new Thread(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", "token " + token);
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                JsonArray commitsArray = JsonParser.parseReader(reader).getAsJsonArray();
                githubCommits.clear();
                for (JsonElement element : commitsArray) {
                    JsonObject commit = element.getAsJsonObject().getAsJsonObject("commit");
                    String message = commit.get("message").getAsString();
                    githubCommits.add("[+] " + message);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}