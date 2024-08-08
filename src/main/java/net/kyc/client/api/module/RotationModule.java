package net.kyc.client.api.module;

import net.kyc.client.impl.manager.player.rotation.Rotation;
import net.kyc.client.init.Managers;

/**
 * @author linus
 * @see net.kyc.client.impl.manager.player.rotation.RotationManager
 * @since 1.0
 */
public class RotationModule extends ToggleModule {

    private final int rotationPriority;

    /**
     * @param name     The module unique identifier
     * @param desc     The module description
     * @param category The module category
     */
    public RotationModule(String name, String desc, ModuleCategory category) {
        super(name, desc, category);
        this.rotationPriority = 100;
    }

    @Override
    public void onUpdate() {

    }

    /**
     * @param name     The module unique identifier
     * @param desc     The module description
     * @param category The module category
     */
    public RotationModule(String name, String desc, ModuleCategory category, int rotationPriority) {
        super(name, desc, category);
        this.rotationPriority = rotationPriority;
    }

    protected void setRotation(float yaw, float pitch)
    {
        Managers.ROTATION.setRotation(new Rotation(getRotationPriority(), yaw, pitch));
    }

    protected void setRotationSilent(float yaw, float pitch) {
        Managers.ROTATION.setRotationSilent(yaw, pitch, true);
    }

    /**
     * Sets client look yaw and pitch
     * @param yaw
     * @param pitch
     */
    protected void setRotationClient(float yaw, float pitch) {
        Managers.ROTATION.setRotationClient(yaw, pitch);
    }

    protected boolean isRotationBlocked() {
        return Managers.ROTATION.isRotationBlocked(getRotationPriority());
    }

    protected int getRotationPriority() {
        return rotationPriority;
    }
}
