package net.kyc.client.mixin.item;

import net.kyc.client.Heckk;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.kyc.client.impl.event.item.FireworkUseEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(FireworkRocketItem.class)
public class MixinFireworkRocketItem {
    /**
     * @param world
     * @param user
     * @param hand
     * @param cir
     */
    @Inject(method = "use", at = @At(value = "HEAD"))
    private void hookUse(World world, PlayerEntity user, Hand hand,
                         CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        FireworkUseEvent fireworkUseEvent = new FireworkUseEvent();
        Heckk.EVENT_HANDLER.dispatch(fireworkUseEvent);
    }
}
