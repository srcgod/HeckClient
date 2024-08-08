package net.kyc.client.init;

import net.kyc.client.Heckk;
import net.kyc.client.impl.manager.ModuleManager;
import net.kyc.client.impl.manager.anticheat.AntiCheatManager;
import net.kyc.client.impl.manager.client.*;
import net.kyc.client.impl.manager.combat.TotemManager;
import net.kyc.client.impl.manager.combat.hole.HoleManager;
import net.kyc.client.impl.manager.network.NetworkManager;
import net.kyc.client.impl.manager.player.InventoryManager;
import net.kyc.client.impl.manager.player.MovementManager;
import net.kyc.client.impl.manager.player.PositionManager;
import net.kyc.client.impl.manager.player.interaction.InteractionManager;
import net.kyc.client.impl.manager.player.rotation.RotationManager;
import net.kyc.client.impl.manager.world.WaypointManager;
import net.kyc.client.impl.manager.world.sound.SoundManager;
import net.kyc.client.impl.manager.world.tick.TickManager;

/**
 * @author linus
 * @since 1.0
 */
public class Managers {
    // Manager instances. Managers can be statically referenced after
    // initialized. Managers will be initialized in this order.
    public static NetworkManager NETWORK;
    public static ModuleManager MODULE;
    public static MacroManager MACRO;
    public static CommandManager COMMAND;
    public static SocialManager SOCIAL;
    public static WaypointManager WAYPOINT;
    public static AccountManager ACCOUNT;
    public static TickManager TICK;
    public static InventoryManager INVENTORY;
    public static PositionManager POSITION;
    public static RotationManager ROTATION;
    //public static NCPManager NCP;
    public static AntiCheatManager ANTICHEAT;
    public static MovementManager MOVEMENT;
    public static HoleManager HOLE;
    public static TotemManager TOTEM;
    public static InteractionManager INTERACT;
    public static SoundManager SOUND;
    public static CapeManager CAPES;
    // The initialized state of the managers. If this is true, all managers
    // have been initialized and the init process is complete. As a general
    // rule, it is good practice to check this state before accessing instances.
    public static boolean initialized;

    /**
     * Initializes the manager instances. Should not be used if the
     * managers are already initialized.
     *
     * @see #isInitialized()
     */
    public static void init() {
        if (!isInitialized()) {
            NETWORK = new NetworkManager();
            MODULE = new ModuleManager();
            MACRO = new MacroManager();
            SOCIAL = new SocialManager();
            WAYPOINT = new WaypointManager();
            ACCOUNT = new AccountManager();
            TICK = new TickManager();
            INVENTORY = new InventoryManager();
            POSITION = new PositionManager();
            ROTATION = new RotationManager();
            ANTICHEAT = new AntiCheatManager();
            MOVEMENT = new MovementManager();
            HOLE = new HoleManager();
            TOTEM = new TotemManager();
            INTERACT = new InteractionManager();
            COMMAND = new CommandManager();
            SOUND = new SoundManager();
            initialized = true;
        }
    }

    /**
     * Initializes final manager properties. Only runs if the Manager
     * instances have been initialized.
     *
     * @see #init()
     * @see #isInitialized()
     */
    public static void postInit() {
        if (isInitialized()) {
            MODULE.postInit();
            MACRO.postInit();
            ACCOUNT.postInit();
            CAPES = new CapeManager();
        }
    }

    /**
     * Returns <tt>true</tt> if the Manager instances have been initialized.
     * This should always return <tt>true</tt> if  has
     * finished running.
     *
     * @return <tt>true</tt> if the Manager instances have been initialized
     * @see #init()
     * @see #initialized
     */
    public static boolean isInitialized() {
        return initialized;
    }
}
