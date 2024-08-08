package net.kyc.client.impl.module.render;

import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.StringConfig;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.text.TextVisitEvent;

/**
 * @author linus
 * @since 1.0
 */
public class NameProtectModule extends ToggleModule {

    Config<String> placeholderConfig = new StringConfig("Placeholder", "The placeholder name for the player", "Player");

    public NameProtectModule() {
        super("NameProtect", "Hides the player name in chat and tablist",
                ModuleCategory.RENDER);
    }

    @EventListener
    public void onTextVisit(TextVisitEvent event) {
        if (mc.player == null) {
            return;
        }
        final String username = mc.getSession().getUsername();
        final String text = event.getText();
        if (text.contains(username)) {
            event.cancel();
            event.setText(text.replace(username, placeholderConfig.getValue()));
        }
    }

    @Override
    public void onUpdate() {

    }
}
