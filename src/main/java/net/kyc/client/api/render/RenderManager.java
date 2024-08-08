package net.kyc.client.api.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.*;
import net.kyc.client.init.Fonts;
import net.kyc.client.init.Modules;
import net.kyc.client.mixin.accessor.AccessorWorldRenderer;
import net.kyc.client.util.Globals;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import static net.kyc.client.api.render.RenderBuffers.*;

/**
 * @author linus
 * @since 1.0
 */
public class RenderManager implements Globals {
    //
    public static final Tessellator TESSELLATOR = RenderSystem.renderThreadTesselator();
    public static final BufferBuilder BUFFER = TESSELLATOR.getBuffer();

    /**
     * When rendering using vanilla methods, you should call this method in order to ensure the GL state does not get
     * leaked. This means you need to manually set the required GL state during the callback.
     */
    public static void post(Runnable callback) {
        RenderBuffers.post(callback);
    }

    /**
     * @param matrices
     * @param p
     * @param color
     */
    public static void renderBox(MatrixStack matrices, BlockPos p, int color) {
        renderBox(matrices, new Box(p), color);
    }

    /**
     * @param matrices
     * @param box
     * @param color
     */
    public static void renderBox(MatrixStack matrices, Box box, int color) {
        if (!isFrustumVisible(box)) {
            return;
        }
        matrices.push();
        drawBox(matrices, box, color);
        matrices.pop();
    }

    /**
     * @param matrices
     * @param box
     */
    public static void drawBox(MatrixStack matrices, Box box, int color) {
        drawBox(matrices, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, color);
    }

    /**
     * Draws a box spanning from [x1, y1, z1] to [x2, y2, z2].
     * The 3 axes centered at [x1, y1, z1] may be colored differently using
     * xAxisRed, yAxisGreen, and zAxisBlue.
     *
     * <p> Note the coordinates the box spans are relative to current
     * translation of the matrices.
     *
     * @param matrices
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     */
    public static void drawBox(MatrixStack matrices, double x1, double y1,
                               double z1, double x2, double y2, double z2, int color) {
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        QUADS.begin(matrix4f);
        QUADS.color(color);

        QUADS.vertex(x1, y1, z1).vertex(x2, y1, z1).vertex(x2, y1, z2).vertex(x1, y1, z2);
        QUADS.vertex(x1, y2, z1).vertex(x1, y2, z2).vertex(x2, y2, z2).vertex(x2, y2, z1);
        QUADS.vertex(x1, y1, z1).vertex(x1, y2, z1).vertex(x2, y2, z1).vertex(x2, y1, z1);
        QUADS.vertex(x2, y1, z1).vertex(x2, y2, z1).vertex(x2, y2, z2).vertex(x2, y1, z2);
        QUADS.vertex(x1, y1, z2).vertex(x2, y1, z2).vertex(x2, y2, z2).vertex(x1, y2, z2);
        QUADS.vertex(x1, y1, z1).vertex(x1, y1, z2).vertex(x1, y2, z2).vertex(x1, y2, z1);

        QUADS.end();
    }

    /**
     * @param p
     * @param width
     * @param color
     */
    public static void renderBoundingBox(MatrixStack matrices, BlockPos p,
                                         float width, int color) {
        renderBoundingBox(matrices, new Box(p), width, color);
    }

    /**
     * @param box
     * @param width
     * @param color
     */
    public static void renderBoundingBox(MatrixStack matrices, Box box,
                                         float width, int color) {
        if (!isFrustumVisible(box)) {
            return;
        }
        matrices.push();
        RenderSystem.lineWidth(width);
        drawBoundingBox(matrices, box, color);
        matrices.pop();
    }

    /**
     * @param matrices
     * @param box
     */
    public static void drawBoundingBox(MatrixStack matrices, Box box, int color) {
        drawBoundingBox(matrices, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, color);
    }

    /**
     * @param matrices
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     */
    public static void drawBoundingBox(MatrixStack matrices, double x1, double y1,
                                       double z1, double x2, double y2, double z2, int color) {
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        LINES.begin(matrix4f);
        LINES.color(color);

        LINES.vertex(x1, y1, z1).vertex(x2, y1, z1);
        LINES.vertex(x2, y1, z1).vertex(x2, y1, z2);
        LINES.vertex(x2, y1, z2).vertex(x1, y1, z2);
        LINES.vertex(x1, y1, z2).vertex(x1, y1, z1);

        LINES.vertex(x1, y1, z1).vertex(x1, y2, z1);
        LINES.vertex(x2, y1, z1).vertex(x2, y2, z1);
        LINES.vertex(x2, y1, z2).vertex(x2, y2, z2);
        LINES.vertex(x1, y1, z2).vertex(x1, y2, z2);

        LINES.vertex(x1, y2, z1).vertex(x2, y2, z1);
        LINES.vertex(x2, y2, z1).vertex(x2, y2, z2);
        LINES.vertex(x2, y2, z2).vertex(x1, y2, z2);
        LINES.vertex(x1, y2, z2).vertex(x1, y2, z1);

        LINES.end();
    }

    /**
     * @param matrices
     * @param s
     * @param d
     * @param width
     */
    public static void renderLine(MatrixStack matrices, Vec3d s,
                                  Vec3d d, float width, int color) {
        renderLine(matrices, s.x, s.y, s.z, d.x, d.y, d.z, width, color);
    }

    /**
     * @param matrices
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     * @param width
     */
    public static void renderLine(MatrixStack matrices, double x1, double y1,
                                  double z1, double x2, double y2, double z2,
                                  float width, int color) {
        matrices.push();
        RenderSystem.lineWidth(width);
        drawLine(matrices, x1, y1, z1, x2, y2, z2, color);
        matrices.pop();
    }

    /**
     * @param matrices
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     */
    public static void drawLine(MatrixStack matrices, double x1, double y1,
                                double z1, double x2, double y2, double z2, int color) {
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        LINES.begin(matrix4f);
        LINES.color(color);
        LINES.vertex(x1, y1, z1);
        LINES.vertex(x2, y2, z2);
        LINES.end();
    }

    /**
     * @param matrices
     * @param text
     * @param pos
     */
    public static void renderSign(MatrixStack matrices, String text, Vec3d pos) {
        renderSign(matrices, text, pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * @param matrices
     * @param text
     * @param x1
     * @param x2
     * @param x3
     */
    public static void renderSign(MatrixStack matrices, String text,
                                  double x1, double x2, double x3) {
        double dist = Math.sqrt(mc.player.squaredDistanceTo(x1, x2, x3));
        float scaling = 0.0018f + Modules.NAMETAGS.getScaling() * (float) dist;
        if (dist <= 8.0) {
            scaling = 0.0245f;
        }
        Camera camera = mc.gameRenderer.getCamera();
        final Vec3d pos = camera.getPos();
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0f));
        matrixStack.translate(x1 - pos.getX(), x2 - pos.getY(), x3 - pos.getZ());
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        matrixStack.scale(-scaling, -scaling, -1.0f);
        GL11.glDepthFunc(GL11.GL_ALWAYS);
        VertexConsumerProvider.Immediate vertexConsumers =
                VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        float hwidth = mc.textRenderer.getWidth(text) / 2.0f;
        //Fonts.VANILLA.drawWithShadow(matrixStack, text, -hwidth, 0.0f, -1);
        vertexConsumers.draw();
        RenderSystem.disableBlend();
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        matrixStack.pop();
    }

    /**
     * @param box
     * @return
     */
    public static boolean isFrustumVisible(Box box) {
        return ((AccessorWorldRenderer) mc.worldRenderer).getFrustum().isVisible(box);
    }

    /**
     * @param matrices
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param color
     */
    public static void rect(MatrixStack matrices, double x1, double y1,
                            double x2, double y2, int color) {
        rect(matrices, x1, y1, x2, y2, 0.0, color);
    }

    /**
     * @param matrices
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param z
     * @param color
     */
    public static void rect(MatrixStack matrices, double x1, double y1,
                            double x2, double y2, double z, int color) {
        x2 += x1;
        y2 += y1;
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
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
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        BUFFER.begin(VertexFormat.DrawMode.QUADS,
                VertexFormats.POSITION_COLOR);
        BUFFER.vertex(matrix4f, (float) x1, (float) y1, (float) z)
                .color(g, h, j, f).next();
        BUFFER.vertex(matrix4f, (float) x1, (float) y2, (float) z)
                .color(g, h, j, f).next();
        BUFFER.vertex(matrix4f, (float) x2, (float) y2, (float) z)
                .color(g, h, j, f).next();
        BUFFER.vertex(matrix4f, (float) x2, (float) y1, (float) z)
                .color(g, h, j, f).next();
        BufferRenderer.drawWithGlobalProgram(BUFFER.end());
        RenderSystem.disableBlend();
    }

    /**
     * @param context
     * @param text
     * @param x
     * @param y
     * @param color
     */
    public static void renderText(DrawContext context, String text, float x, float y, int color) {
        context.drawText(mc.textRenderer, text, (int) x, (int) y, color, true);
    }

    /**
     * @param text
     * @return
     */
    public static int textWidth(String text) {
        return mc.textRenderer.getWidth(text);
    }
}