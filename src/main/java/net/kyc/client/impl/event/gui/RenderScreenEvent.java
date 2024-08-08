package net.kyc.client.impl.event.gui;

import net.minecraft.client.util.math.MatrixStack;
import net.kyc.client.api.event.Event;

public class RenderScreenEvent extends Event {
    public final MatrixStack matrixStack;

    public RenderScreenEvent(MatrixStack matrixStack) {
        this.matrixStack = matrixStack;
    }
}
