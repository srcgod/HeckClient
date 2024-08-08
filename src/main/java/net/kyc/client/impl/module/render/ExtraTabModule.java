package net.kyc.client.impl.module.render;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.gui.hud.PlayerListEvent;
import net.kyc.client.impl.event.gui.hud.PlayerListNameEvent;
import net.kyc.client.init.Managers;

/**
 * @author linus
 * @since 1.0
 */
public class ExtraTabModule extends ToggleModule {

    Config<Integer> sizeConfig = new NumberConfig<>("Size", "The number of players to show", 80, 200, 1000);
    Config<Boolean> friendsConfig = new BooleanConfig("Friends", "Highlights friends in the tab list", true);

    public ExtraTabModule() {
        super("ExtraTab", "Expands the tab list size to allow for more players",
                ModuleCategory.RENDER);
    }

    @EventListener
    public void onPlayerListName(PlayerListNameEvent event) {
        if (friendsConfig.getValue() && Managers.SOCIAL.isFriend(event.getPlayerName())) {
            event.cancel();
            event.setPlayerName(Text.of(Formatting.AQUA + event.getPlayerName().getString()));
        }
    }

    @EventListener
    public void onPlayerList(PlayerListEvent event) {
        event.cancel();
        event.setSize(sizeConfig.getValue());
    }

    @Override
    public void onUpdate() {

    }
}
