package net.kyc.client.impl.manager;

import net.kyc.client.api.module.Module;
import net.kyc.client.impl.module.client.*;
import net.kyc.client.impl.module.combat.*;
import net.kyc.client.impl.module.exploit.*;
import net.kyc.client.impl.module.misc.*;
import net.kyc.client.impl.module.movement.*;
import net.kyc.client.impl.module.render.*;
import net.kyc.client.impl.module.world.*;

import java.util.*;

/**
 * @author linus
 * @since 1.0
 */
public class ModuleManager {
    // The client module register. Keeps a list of modules and their ids for
    // easy retrieval by id.
    private final Map<String, Module> modules =
            Collections.synchronizedMap(new LinkedHashMap<>());

    /**
     * Initializes the module register.
     */
    public ModuleManager() {
        // MAINTAIN ALPHABETICAL ORDER
        register(
                // Client
                new ServerModule(),
                new AntiCrashModule(),
                new CapesModule(),
                new ClickGuiModule(),
                new DRPC(),
                new ColorsModule(),
                new HudEditorModule(),
                new HUDModule(),
                new RotationsModule(),
                // Combat
                new AuraModule(),
                new AutoArmorModule(),
                new AutoBowReleaseModule(),
                new AutoCrystalModule(),
                new AutoLogModule(),
                new AutoTotemModule(),
                new AutoTrapModule(),
                new AutoXPModule(),
                // new BackTrackModule(),
                new BurrowModule(),
                new BowAimModule(),
                new CevBreaker(),
                new CriticalsModule(),
                new HandBlockModule(),
                new HoleFillModule(),
                new NoHitDelayModule(),
                new ReplenishModule(),
                new SelfBowModule(),
                new SelfTrapModule(),
                new SurroundModule(),
                new TriggerModule(),
                // Exploit
                new AntiHungerModule(),
                new ChorusControlModule(),
                new CrasherModule(),
                new DisablerModule(),
                new ExtendedFireworkModule(),
                new FakeLatencyModule(),
                new FastProjectileModule(),
                new PacketCancelerModule(),
                new PacketFlyModule(),
                new PhaseModule(),
                new PortalGodModeModule(),
                new ReachModule(),
                // Misc
                // new AntiAFKModule(),
                new AntiAimModule(),
                // new AntiBookBanModule(),
                new AntiSpamModule(),
                new AutoAcceptModule(),
                new AutoEatModule(),
                new AutoFishModule(),
                new AutoReconnectModule(),
                new AutoRespawnModule(),
                new BeaconSelectorModule(),
                // new BetterChatModule(),
                new ChatNotifierModule(),
                new ChestSwapModule(),
                // new ChestStealerModule(),

                new InvCleanerModule(),
                new MiddleClickModule(),
                new NoPacketKickModule(),
                new NoSoundLagModule(),
                new TimerModule(),
                new TrueDurabilityModule(),
                new FakePlayerModule(),

                new GhostMeowModule(),
                new UnfocusedFPSModule(),
                new XCarryModule(),
                // Movement
                new AntiLevitationModule(),
                new AutoWalkModule(),
                new ElytraFlyModule(),
                new EntityControlModule(),
                new EntitySpeedModule(),
                new FakeLagModule(),
                new FastFallModule(),
                new FlightModule(),
                new IceSpeedModule(),
                new JesusModule(),
                new LongJumpModule(),
                new NoFallModule(),
                new NoJumpDelayModule(),
                new NoSlowModule(),
                new ParkourModule(),
                new SpeedModule(),
                new SprintModule(),
                new SaveWalk(),
                new StepModule(),
                new ReverseStep(),
                new HoleSnapModule(),
                new Crash(),
                new TickShiftModule(),
                new TridentFlyModule(),
                new VelocityModule(),
                new YawModule(),
                // Render
                new Animations(),
                new BlockHighlightModule(),
                new BreakHighlightModule(),
                new ChamsModule(),
                new PopChamsModule(),
                new ESPModule(),
                new ExtraTabModule(),
                new FreecamModule(),
                new FullbrightModule(),
                new HoleESPModule(),
                new KillEffectModule(),
                new NameProtectModule(),
                new NametagsModule(),
                new NoRenderModule(),
                new NoRotateModule(),
                new NoWeatherModule(),
                new ParticlesModule(),
                new SkeletonModule(),
                new SkyboxModule(),
                new TooltipsModule(),
                new TracersModule(),
                new TrueSightModule(),
                new ViewClipModule(),
                new ViewModelModule(),
                // new WaypointsModule(),
                // World
                new AntiInteractModule(),
                new AutoMineModule(),
                //new AutoMineRewriteModule(),
                new AutoToolModule(),
                new AvoidModule(),
                new BlockInteractModule(),
                new FastDropModule(),
                new FastPlaceModule(),
                new MultitaskModule(),
                new NoGlitchBlocksModule(),
                new PearlMacroModule(),
                new ScaffoldModule(),
                new SpeedmineModule()
                // new WallhackModule()
        );
        //if (HeckMod.isBaritonePresent()) {
            //register(new BaritoneModule());
       // }
        //Heckk.info("Registered {} modules!", modules.size());
    }

    /**
     *
     */
    public void postInit() {
        // TODO
    }

    /**
     * @param modules
     * @see #register(Module)
     */
    private void register(Module... modules) {
        for (Module module : modules) {
            register(module);
        }
    }

    /**
     * @param module
     */
    private void register(Module module) {
        modules.put(module.getId(), module);
    }

    /**
     * @param id
     * @return
     */
    public Module getModule(String id) {
        return modules.get(id);
    }

    /**
     * @return
     */
    public List<Module> getModules() {
        return new ArrayList<>(modules.values());
    }
}
