package net.kyc.client.impl.gui.click.impl.config.component;

import net.minecraft.client.gui.DrawContext;

/**
 * @author linus
 * @since 1.0
 */
public interface Drawable {
    /**
     * @param context
     * @param mouseX
     * @param mouseY
     * @param delta
     */
    void render(DrawContext context, float mouseX, float mouseY, float delta);
}
