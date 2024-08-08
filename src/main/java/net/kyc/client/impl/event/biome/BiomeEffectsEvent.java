package net.kyc.client.impl.event.biome;

import net.minecraft.world.biome.BiomeParticleConfig;
import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;

@Cancelable
public class BiomeEffectsEvent extends Event {

    private BiomeParticleConfig particleConfig;

    public BiomeParticleConfig getParticleConfig() {
        return particleConfig;
    }

    public void setParticleConfig(BiomeParticleConfig particleConfig) {
        this.particleConfig = particleConfig;
    }
}
