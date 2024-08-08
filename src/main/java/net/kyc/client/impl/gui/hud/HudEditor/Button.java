package net.kyc.client.impl.gui.hud.HudEditor;

import net.kyc.client.util.Globals;
import net.minecraft.util.Identifier;

/**
 * Just button.
 *
 * @author Bebra_tyan(Ferra13671)
 */

public class Button implements Globals {

    private final int id;

    private int centerX;
    private int centerY;

    private int width;
    private int height;

    private Identifier texture = null;

    public String text;

    public boolean hovered;

    public float halfTextWidth;
    public float halfTextHeight;



    public Button(int id, int x, int y, int width, int height, String text) {
        this.id = id;

        this.centerX = x;
        this.centerY = y;

        this.width = width;
        this.height = height;

        this.text = text;
    }

    public void updateButton(int mouseX, int mouseY) {
        this.hovered = isMouseOnButton(mouseX, mouseY);
    }

    public void mouseClicked(int mouseX, int mouseY) {}


    public void renderButton() {
    }



    public boolean isMouseOnButton(int mouseX, int mouseY) {
        return centerX - width <= mouseX && mouseX <= centerX + width && centerY - height <= mouseY && mouseY <= centerY + height;
    }

    public void keyTyped(int key, char charKey) {}


    public int getId() {
        return this.id;
    }

    public int getCenterX() {
        return this.centerX;
    }

    public int getCenterY() {
        return this.centerY;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String getText() {
        return this.text;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setText(String text) {
        this.text = text;
    }
}
