package net.kyc.client.mixin.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.kyc.client.Heckk;
import net.minecraft.network.handler.DecoderHandler;
import net.kyc.client.impl.event.network.DecodePacketEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * @see DecoderHandler
 */
@Mixin(DecoderHandler.class)
public class MixinDecoderHandler {
    /**
     * @param ctx
     * @param buf
     * @param objects
     * @param ci
     */
    @Inject(method = "decode", at = @At(value = "INVOKE", target = "Lnet/minecraft/" +
            "network/NetworkState;getId()Ljava/lang/String;", shift = At.Shift.AFTER), cancellable = true)
    private void hookDecode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> objects, CallbackInfo ci) {
        DecodePacketEvent decodePacketEvent = new DecodePacketEvent();
        Heckk.EVENT_HANDLER.dispatch(decodePacketEvent);
        if (decodePacketEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
