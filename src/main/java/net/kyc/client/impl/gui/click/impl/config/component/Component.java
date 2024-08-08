package net.kyc.client.impl.gui.click.impl.config.component;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.kyc.client.api.render.shader.Shader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.Window;
import net.minecraft.util.math.ColorHelper;
import net.kyc.client.api.render.RenderManager;
import net.kyc.client.impl.gui.click.ClickGuiScreen;
import net.kyc.client.util.Globals;
import org.joml.Matrix4f;

import java.util.function.BiConsumer;

/**
 * @author linus
 * @see Drawable
 * @see Interactable
 * @since 1.0
 */
public abstract class Component implements Drawable, Globals {
    //
    protected float x, y, width, height;

    /**
     * @param context
     * @param mouseX
     * @param mouseY
     * @param delta
     */
    @Override
    public abstract void render(DrawContext context, float mouseX, float mouseY, float delta);

    /**
     * @param context
     * @param color
     */
    protected void rect(DrawContext context, int color) {
        fill(context, x, y, width, height, color);
    }

    /**
     * @param context
     * @param color1
     */
    protected void rectGradient(DrawContext context, int color1, int color2) {
        fillGradient(context, x, y, x + width, y + height, color1, color2);
    }

    protected void scale(DrawContext context, float scale) {

    }

    protected void drawRoundedRect(DrawContext context, double x1, double y1,
                                   double x2, double y2, int color) {
        drawRoundedRect(context, x1, y1, x2, y2, 0, color);
    }

    protected void drawRoundedRect(DrawContext context, double x1, double y1,
                                   double x2, double y2, double z, int color) {
        fill(context, x1, y1, x2, y2, z, color);

       /* Matrix4f matrix4f = context.peek().getPositionMatrix();
        Shader shader = new Shader("rect.vert", "roundedrect.frag");
        shader.bind();
        float f = (float) ColorHelper.Argb.getAlpha(color) / 255.0f;
        float g = (float) ColorHelper.Argb.getRed(color) / 255.0f;
        float h = (float) ColorHelper.Argb.getGreen(color) / 255.0f;
        float j = (float) ColorHelper.Argb.getBlue(color) / 255.0f;
        BufferBuilder buffer = RenderManager.BUFFER;
        RenderSystem.enableBlend();
        // RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        buffer.begin(VertexFormat.DrawMode.QUADS,
                VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix4f, (float) x1, (float) y1, (float) z)
                .color(g, h, j, f).next();
        buffer.vertex(matrix4f, (float) x1, (float) y2, (float) z)
                .color(g, h, j, f).next();
        buffer.vertex(matrix4f, (float) x2, (float) y2, (float) z)
                .color(g, h, j, f).next();
        buffer.vertex(matrix4f, (float) x2, (float) y1, (float) z)
                .color(g, h, j, f).next();
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.disableBlend();
        shader.unbind();
         */
    }

    protected void drawCircle(DrawContext context, double x, double y,
                              double radius, int color) {
        drawCircle(context, x, y, 0, radius, color);
    }

    protected void drawCircle(DrawContext context, double x, double y,
                              double z, double radius, int color) {
        /*
        Matrix4f matrix4f = context.peek().getPositionMatrix();
        Shader shader = new Shader("rect.vert", "circle.frag");
        shader.bind();
        float f = (float) ColorHelper.Argb.getAlpha(color) / 255.0f;
        float g = (float) ColorHelper.Argb.getRed(color) / 255.0f;
        float h = (float) ColorHelper.Argb.getGreen(color) / 255.0f;
        float j = (float) ColorHelper.Argb.getBlue(color) / 255.0f;
        BufferBuilder buffer = RenderManager.BUFFER;
        RenderSystem.enableBlend();
        // RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        buffer.begin(VertexFormat.DrawMode.QUADS,
                VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix4f, (float) (x - radius), (float) (y - radius),
                (float) z).color(g, h, j, f).next();
        buffer.vertex(matrix4f, (float) (x - radius), (float) (y + radius),
                        (float) z).color(g, h, j, f).next();
        buffer.vertex(matrix4f, (float) (x + radius), (float) (y + radius),
                        (float) z).color(g, h, j, f).next();
        buffer.vertex(matrix4f, (float) (x + radius), (float) (y - radius),
                (float) z).color(g, h, j, f).next();
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.disableBlend();
        shader.unbind();
         */
    }

    protected void drawHorizontalLine(DrawContext context, double x1, double x2,
                                      double y, int color) {
        if (x2 < x1) {
            double i = x1;
            x1 = x2;
            x2 = i;
        }
        fill(context, x1, y, x1 + x2 + 1, y + 1, color);
    }

    protected void drawVerticalLine(DrawContext context, double x, double y1,
                                    double y2, int color) {
        if (y2 < y1) {
            double i = y1;
            y1 = y2;
            y2 = i;
        }
        fill(context, x, y1 + 1, x + 1, y1 + y2, color);
    }

    public void fill(DrawContext context, double x1, double y1, double x2,
                     double y2, int color) {
        fill(context, x1, y1, x2, y2, 0.0, color);
    }

    public void fill(DrawContext context, double x1, double y1, double x2,
                     double y2, double z, int color) {
        x2 += x1;
        y2 += y1;
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        double i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }
        float f = (float) ColorHelper.Argb.getAlpha(color) / 255.0f;
        float g = (float) ColorHelper.Argb.getRed(color) / 255.0f;
        float h = (float) ColorHelper.Argb.getGreen(color) / 255.0f;
        float j = (float) ColorHelper.Argb.getBlue(color) / 255.0f;
        BufferBuilder buffer = RenderManager.BUFFER;
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        buffer.begin(VertexFormat.DrawMode.QUADS,
                VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix4f, (float) x1, (float) y1, (float) z)
                .color(g, h, j, f).next();
        buffer.vertex(matrix4f, (float) x1, (float) y2, (float) z)
                .color(g, h, j, f).next();
        buffer.vertex(matrix4f, (float) x2, (float) y2, (float) z)
                .color(g, h, j, f).next();
        buffer.vertex(matrix4f, (float) x2, (float) y1, (float) z)
                .color(g, h, j, f).next();
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.disableBlend();
    }

    protected void fillGradient(DrawContext context, double startX,
                                double startY, double endX, double endY,
                                int colorStart, int colorEnd) {
        fillGradient(context, startX, startY, endX, endY, colorStart, colorEnd, 0);
    }

    protected void fillGradient(DrawContext context, double startX,
                                double startY, double endX, double endY,
                                int colorStart, int colorEnd, int z) {
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        fillGradient(context.getMatrices().peek().getPositionMatrix(), buffer,
                startX, startY, endX, endY, z, colorStart, colorEnd);
        tessellator.draw();
        RenderSystem.disableBlend();
    }

    protected void fillGradient(Matrix4f matrix, BufferBuilder builder,
                                double startX, double startY, double endX,
                                double endY, double z, int colorStart,
                                int colorEnd) {
        float f = (float) ColorHelper.Argb.getAlpha(colorStart) / 255.0f;
        float g = (float) ColorHelper.Argb.getRed(colorStart) / 255.0f;
        float h = (float) ColorHelper.Argb.getGreen(colorStart) / 255.0f;
        float i = (float) ColorHelper.Argb.getBlue(colorStart) / 255.0f;
        float j = (float) ColorHelper.Argb.getAlpha(colorEnd) / 255.0f;
        float k = (float) ColorHelper.Argb.getRed(colorEnd) / 255.0f;
        float l = (float) ColorHelper.Argb.getGreen(colorEnd) / 255.0f;
        float m = (float) ColorHelper.Argb.getBlue(colorEnd) / 255.0f;
        builder.vertex(matrix, (float) startX, (float) startY, (float) z)
                .color(k, l, m, j).next();
        builder.vertex(matrix, (float) startX, (float) endY, (float) z)
                .color(k, l, m, j).next();
        builder.vertex(matrix, (float) endX, (float) endY, (float) z)
                .color(g, h, i, f).next();
        builder.vertex(matrix, (float) endX, (float) startY, (float) z)
                .color(g, h, i, f).next();
    }

    protected void fillGradientQuad(DrawContext context, float x1, float y1, float x2, float y2, 
                                    int startColor, int endColor, boolean sideways) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        Matrix4f posMatrix = context.getMatrices().peek().getPositionMatrix();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        if (sideways) {
            bufferBuilder.vertex(posMatrix, x1, y1, 0.0F).color(f1, f2, f3, f).next();
            bufferBuilder.vertex(posMatrix, x1, y2, 0.0F).color(f1, f2, f3, f).next();
            bufferBuilder.vertex(posMatrix, x2, y2, 0.0F).color(f5, f6, f7, f4).next();
            bufferBuilder.vertex(posMatrix, x2, y1, 0.0F).color(f5, f6, f7, f4).next();
        } else {
            bufferBuilder.vertex(posMatrix, x2, y1, 0.0F).color(f1, f2, f3, f).next();
            bufferBuilder.vertex(posMatrix, x1, y1, 0.0F).color(f1, f2, f3, f).next();
            bufferBuilder.vertex(posMatrix, x1, y2, 0.0F).color(f5, f6, f7, f4).next();
            bufferBuilder.vertex(posMatrix, x2, y2, 0.0F).color(f5, f6, f7, f4).next();
        }
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
    }


    public void drawWithOutline(int x, int y, BiConsumer<Integer, Integer> renderAction) {
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ZERO,
                GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SrcFactor.SRC_ALPHA,
                GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        renderAction.accept(x + 1, y);
        renderAction.accept(x - 1, y);
        renderAction.accept(x, y + 1);
        renderAction.accept(x, y - 1);
        RenderSystem.defaultBlendFunc();
        renderAction.accept(x, y);
    }

    public void drawSprite(DrawContext context, int x, int y, int z, int width,
                           int height, Sprite sprite) {
        drawTexturedQuad(context.getMatrices().peek().getPositionMatrix(), x, x + width,
                y, y + height, z, sprite.getMinU(), sprite.getMaxU(),
                sprite.getMinV(), sprite.getMaxV());
    }

    public void drawSprite(DrawContext context, int x, int y, int z, int width,
                           int height, Sprite sprite, float red, float green,
                           float blue, float alpha) {
        drawTexturedQuad(context.getMatrices().peek().getPositionMatrix(), x, x + width,
                y, y + height, z, sprite.getMinU(), sprite.getMaxU(),
                sprite.getMinV(), sprite.getMaxV(), red, green, blue, alpha);
    }

    public void drawBorder(DrawContext context, int x, int y, int width,
                           int height, int color) {
        fill(context, x, y, x + width, y + 1, color);
        fill(context, x, y + height - 1, x + width, y + height, color);
        fill(context, x, y + 1, x + 1, y + height - 1, color);
        fill(context, x + width - 1, y + 1, x + width,
                y + height - 1, color);
    }

    public void drawTexture(DrawContext context, int x, int y, int u,
                            int v, int width, int height) {
        drawTexture(context, x, y, 0, (float) u, (float) v, width, height, 256, 256);
    }                                                                       //256

    public void drawTexture(DrawContext context, int x, int y, int z,
                            float u, float v, int width, int height,
                            int textureWidth, int textureHeight) {
        drawTexture(context, x, x + width, y, y + height, z, width,
                height, u, v, textureWidth, textureHeight);
    }

    public void drawTexture(DrawContext context, int x, int y, int width,
                            int height, float u, float v, int regionWidth,
                            int regionHeight, int textureWidth,
                            int textureHeight) {
        drawTexture(context, x, x + width, y, y + height, 0,
                regionWidth, regionHeight, u, v, textureWidth, textureHeight);
    }

    public void drawTexture(DrawContext context, int x, int y, float u,
                            float v, int width, int height,
                            int textureWidth, int textureHeight) {
        drawTexture(context, x, y, width, height, u, v, width, height,
                textureWidth, textureHeight);
    }

    private void drawTexture(DrawContext context, int x0, int x1,
                             int y0, int y1, int z, int regionWidth,
                             int regionHeight, float u, float v,
                             int textureWidth, int textureHeight) {
        drawTexturedQuad(context.getMatrices().peek().getPositionMatrix(), x0, x1, y0, y1, z,
                (u + 0.0F) / (float) textureWidth,
                (u + (float) regionWidth) / (float) textureWidth,
                (v + 0.0F) / (float) textureHeight,
                (v + (float) regionHeight) / (float) textureHeight);
    }

    private void drawTexturedQuad(Matrix4f matrix, int x0, int x1, int y0,
                                  int y1, int z, float u0, float u1,
                                  float v0, float v1) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(matrix, (float) x0, (float) y0, (float) z)
                .texture(u0, v0).next();
        buffer.vertex(matrix, (float) x0, (float) y1, (float) z)
                .texture(u0, v1).next();
        buffer.vertex(matrix, (float) x1, (float) y1, (float) z)
                .texture(u1, v1).next();
        buffer.vertex(matrix, (float) x1, (float) y0, (float) z)
                .texture(u1, v0).next();
        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

    private void drawTexturedQuad(Matrix4f matrix, int x0, int x1,
                                  int y0, int y1, int z, float u0,
                                  float u1, float v0, float v1,
                                  float red, float green, float blue, float alpha) {
        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
        RenderSystem.enableBlend();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        buffer.vertex(matrix, (float) x0, (float) y0, (float) z)
                .color(red, green, blue, alpha).texture(u0, v0).next();
        buffer.vertex(matrix, (float) x0, (float) y1, (float) z)
                .color(red, green, blue, alpha).texture(u0, v1).next();
        buffer.vertex(matrix, (float) x1, (float) y1, (float) z)
                .color(red, green, blue, alpha).texture(u1, v1).next();
        buffer.vertex(matrix, (float) x1, (float) y0, (float) z)
                .color(red, green, blue, alpha).texture(u1, v0).next();
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.disableBlend();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * @param xval
     * @param yval
     * @return
     */
    public boolean isWithin(double xval, double yval) {
        return isWithin((float) xval, (float) yval);
    }

    /**
     * Checks if a given value is between the width and the height
     *
     * @param xval The x-position of the value
     * @param yval The y-position of the value
     * @return Whether the given value is between the width and the height
     */
    public boolean isWithin(float xval, float yval) {
        return isMouseOver(xval, yval, x, y, width, height);
    }

    public boolean isMouseOver(double mx, double my, double x1, double y1,
                               double x2, double y2) {
        return mx >= x1 && mx <= x1 + x2 && my >= y1 && my <= y1 + y2;
    }

    /**
     * @param x
     * @param y
     */
    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @param width
     * @param height
     */
    public void setDimensions(float width, float height) {
        setWidth(width);
        setHeight(height);
    }

    public void enableScissor(int x1, int y1, int x2, int y2) {
        setScissor(ClickGuiScreen.SCISSOR_STACK.push(new ScreenRect(x1, y1, x2 - x1, y2 - y1)));
    }

    public void disableScissor() {
        setScissor(ClickGuiScreen.SCISSOR_STACK.pop());
    }

    private void setScissor(ScreenRect rect) {
        if (rect != null) {
            Window window = mc.getWindow();
            int i = window.getFramebufferHeight();
            double d = window.getScaleFactor();
            double e = (double)rect.getLeft() * d;
            double f = (double)i - (double)rect.getBottom() * d;
            double g = (double)rect.width() * d;
            double h = (double)rect.height() * d;
            RenderSystem.enableScissor((int)e, (int)f, Math.max(0, (int)g), Math.max(0, (int)h));
        } else {
            RenderSystem.disableScissor();
        }
    }
}
