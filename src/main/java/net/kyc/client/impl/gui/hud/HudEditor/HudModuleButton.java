package net.kyc.client.impl.gui.hud.HudEditor;


import net.kyc.client.impl.gui.hud.HudModule;
import net.kyc.client.impl.gui.hud.RenderStage;
import net.minecraft.client.gui.DrawContext;

/**
 * A button that binds to the HUD module. It is necessary to be able to change the module position via HUD Editor.
 *
 * @author Bebra_tyan(Ferra13671)
 */

public class HudModuleButton extends Button {
    public int x;
    public int y;

    private boolean selected = false;
    public final HudModule hudComponent;


    public HudModuleButton(int id, HudModule hudComponent) {
        super(id ,(hudComponent.getX() + (hudComponent.width / 2)),(hudComponent.getY() + (hudComponent.height / 2)),(hudComponent.width / 2),(hudComponent.height / 2), "");
        this.hudComponent = hudComponent;
    }


    public void renderButton(DrawContext drawContext) {
        this.hudComponent.render(RenderStage.TEXT, drawContext);
        this.hudComponent.render(RenderStage.IMAGE, drawContext);
    }

    @Override
    public void updateButton(int mouseX, int mouseY) {
        super.updateButton(mouseX, mouseY);

        if (this.selected) {
            this.x = mouseX;
            this.y = mouseY;
        } else {
            this.x = this.hudComponent.getX();
            this.y = this.hudComponent.getY();
        }

        this.hudComponent.setX(this.x);
        this.hudComponent.setY(this.y);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        if (this.isMouseOnButton(mouseX, mouseY)) {
            this.selected = !this.selected;
        }
    }

    @Override
    public boolean isMouseOnButton(int mouseX, int mouseY) {
        if (this.hudComponent.width < 0) {
            return mouseX <= this.x && mouseX >= this.x + this.hudComponent.width && mouseY >= this.y && mouseY <= this.y + this.hudComponent.height;
        } else {
            return this.x <= mouseX && mouseX <= this.x + this.hudComponent.width && mouseY >= this.y && mouseY <= this.y + this.hudComponent.height;
        }
    }

    public boolean isSelected() {
        return this.selected;
    }
}
