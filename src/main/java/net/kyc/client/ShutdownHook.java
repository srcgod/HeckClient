package net.kyc.client;

import net.kyc.client.api.file.ClientConfiguration;

/**
 * @author linus
 * @since 1.0
 */
public class ShutdownHook extends Thread {
    /**
     *
     */
    public ShutdownHook() {
        setName("Heckk-ShutdownHook");
    }

    /**
     * This runs when the game is shutdown and saves the
     * {@link ClientConfiguration} files.
     *
     * @see ClientConfiguration#saveClient()
     */
    @Override
    public void run() {
        Heckk.info("Saving configurations and shutting down!");
        Heckk.CONFIG.saveClient();
    }
}
