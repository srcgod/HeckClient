package net.kyc.client.api.font;

/**
 * @author xgraza
 * @since 1.0
 */
@FunctionalInterface
public interface GlyphVisitor
{
    int draw(final FontRenderer fontRenderer, final char c, final double x, final double y, final int color, final boolean shadow);
}