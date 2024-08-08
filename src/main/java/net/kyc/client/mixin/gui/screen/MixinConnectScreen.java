package net.kyc.client.mixin.gui.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.kyc.client.init.Managers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author linus
 * @since 1.0
 */
@Mixin(ConnectScreen.class)
public class MixinConnectScreen {
    /**
     * @param client
     * @param address
     * @param info
     * @param ci
     */
    @Inject(method = "connect(Lnet/minecraft/client/MinecraftClient;" +
            "Lnet/minecraft/client/network/ServerAddress;Lnet/" +
            "minecraft/client/network/ServerInfo;)V", at = @At(value = "HEAD"))
    private void onConnect(MinecraftClient client, ServerAddress address,
                           ServerInfo info, CallbackInfo ci) {
        Managers.NETWORK.setAddress(address);
        Managers.NETWORK.setInfo(info);
    }
}
