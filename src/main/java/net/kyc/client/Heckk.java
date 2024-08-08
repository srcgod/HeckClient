package net.kyc.client;

import net.kyc.client.api.Identifiable;
import net.kyc.client.api.event.handler.EventBus;
import net.kyc.client.api.event.handler.EventHandler;
import net.kyc.client.api.file.ClientConfiguration;
import net.kyc.client.init.Managers;
import net.kyc.client.init.Modules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Heckk {
    // Client logger.
    public static Logger LOGGER;
    // Client Event handler (aka Event bus) which handles event dispatching
    // and listening for client events.
    public static EventHandler EVENT_HANDLER;
    // Client configuration handler. This master saves/loads the client
    // configuration files which have been saved locally.
    public static ClientConfiguration CONFIG;
    // Client shutdown hooks which will run once when the MinecraftClient
    // game instance is shutdown.
    public static ShutdownHook SHUTDOWN;


    //
    public static Executor EXECUTOR;

    /**
     * Called during {@link HeckMod#onInitializeClient()}
     */
    public static void init() {
        LOGGER = LogManager.getLogger("Heckk");
        // Debug information - required when submitting a crash / bug report
        //info("This build of Heckk is on Git hash {} and was compiled on {}", net.kyc.client.BuildConfig.HASH, net.kyc.client.BuildConfig.BUILD_TIME);
        info("Starting preInit ...");
        EXECUTOR = Executors.newFixedThreadPool(1);
        EVENT_HANDLER = new EventBus();
        info("Starting init ...");
        Managers.init();
        Modules.init();
        info("Starting postInit ...");
        CONFIG = new ClientConfiguration();
        Managers.postInit();
        SHUTDOWN = new ShutdownHook();
        Runtime.getRuntime().addShutdownHook(SHUTDOWN);
        // load configs AFTER everything has been initialized
        // this is to prevent configs loading before certain aspects of managers are available
        CONFIG.loadClient();
    }

    public static void info(String message) {
        LOGGER.info(String.format("[Heckk] %s", message));
    }

    /**
     * @param message
     * @param params
     */
    public static void info(String message, Object... params) {
        LOGGER.info(String.format("[Heckk] %s", message), params);
    }

    /**
     * Wrapper method for {@link Logger#info(String)} which logs a message to
     * the client {@link Logger}.
     *
     * @param feature
     * @param message The log message
     * @see Logger#info(String)
     */
    public static void info(Identifiable feature, String message) {
        LOGGER.info(String.format("[%s] %s", feature.getId(), message));
    }

    /**
     * @param feature
     * @param message
     * @param params
     */
    public static void info(Identifiable feature, String message,
                            Object... params) {
        LOGGER.info(String.format("[%s] %s", feature.getId(), message), params);
    }

    /**
     * Wrapper method for {@link Logger#error(String)} which logs an error to
     * the client {@link Logger}.
     *
     * @param message The log message
     * @see Logger#error(String)
     */
    public static void error(String message) {
        LOGGER.error(message);
    }

    /**
     * @param message
     */
    public static void error(String message, Object... params) {
        LOGGER.error(message, params);
    }

    /**
     * Wrapper method for {@link Logger#error(String)} which logs an error to
     * the client {@link Logger}.
     *
     * @param feature
     * @param message The log message
     * @see Logger#error(String)
     */
    public static void error(Identifiable feature, String message) {
        LOGGER.error(String.format("[%s] %s", feature.getId(), message));
    }

    /**
     * @param feature
     * @param message
     * @param params
     */
    public static void error(Identifiable feature, String message,
                             Object... params) {
        LOGGER.error(String.format("[%s] %s", feature.getId(), message), params);
    }
}
