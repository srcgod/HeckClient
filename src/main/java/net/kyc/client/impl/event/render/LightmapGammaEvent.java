package net.kyc.client.impl.event.render;

import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;
import net.kyc.client.mixin.render.MixinLightmapTextureManager;

/**
 * @author linus
 * @see MixinLightmapTextureManager
 * @since 1.0
 */
@Cancelable
public class LightmapGammaEvent extends Event {
    //
    private int gamma;

    /**
     * @param gamma
     */
    public LightmapGammaEvent(int gamma) {
        this.gamma = gamma;
    }

    public int getGamma() {
        return gamma;
    }

    public void setGamma(int gamma) {
        this.gamma = gamma;
    }
}
