package net.kyc.client.impl.module.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.kyc.client.Heckk;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.config.setting.ItemListConfig;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.EventStage;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.file.ConfigFile;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.TickEvent;
import net.kyc.client.util.math.timer.CacheTimer;
import net.kyc.client.util.math.timer.Timer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author linus
 * @since 1.0
 */
public class InvCleanerModule extends ToggleModule {

    //
    Config<List<Item>> blacklistConfig = new ItemListConfig<>("Blacklist", "The items to throw");
    Config<Float> delayConfig = new NumberConfig<>("Delay", "The delay between removing items from the inventory", 0.05f, 0.0f, 1.0f);
    Config<Boolean> hotbarConfig = new BooleanConfig("Hotbar", "Cleans the hotbar inventory slots", true);
    //
    private final Timer invCleanTimer = new CacheTimer();

    /**
     *
     */
    public InvCleanerModule() {
        super("InvCleaner", "Automatically cleans the player inventory",
                ModuleCategory.MISC);
    }

    @EventListener
    public void onTick(TickEvent event) {
        if (event.getStage() != EventStage.PRE) {
            return;
        }
        for (Item item : blacklistConfig.getValue()) {
            for (int i = 35; i >= (hotbarConfig.getValue() ? 0 : 9); i--) {
                ItemStack stack = mc.player.getInventory().getStack(i);
                if (stack.isEmpty()) {
                    continue;
                }
                if (stack.getItem() == item && invCleanTimer.passed(delayConfig.getValue() * 1000)) {
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, ScreenHandler.EMPTY_SPACE_SLOT_INDEX, 0, SlotActionType.PICKUP, mc.player);
                    invCleanTimer.reset();
                    break;
                }
            }
        }
    }

    public ConfigFile getBlacklistFile(Path clientDir) {
        return new InvCleanerFile(clientDir);
    }

    @Override
    public void onUpdate() {

    }

    /**
     * @see ConfigFile
     */
    public class InvCleanerFile extends ConfigFile {

        public InvCleanerFile(Path clientDir) {
            super(clientDir, "inv-cleaner");
        }

        @Override
        public void save() {
            try {
                Path filepath = getFilepath();
                if (!Files.exists(filepath)) {
                    Files.createFile(filepath);
                }
                JsonObject json = new JsonObject();
                //
                JsonArray itemArray = new JsonArray();
                for (Item item : blacklistConfig.getValue()) {
                    itemArray.add(item.getTranslationKey());
                }
                json.add("items", itemArray);
                write(filepath, serialize(json));
            }
            // error writing file
            catch (IOException e) {
                Heckk.error("Could not save file for inv cleaner!");
                e.printStackTrace();
            }
        }

        @Override
        public void load() {
            try {
                Path filepath = getFilepath();
                if (Files.exists(filepath)) {
                    String content = read(filepath);
                    JsonObject object = parseObject(content);
                    if (object != null && object.has("items")) {
                        JsonArray jsonArray = object.getAsJsonArray("items");
                        for (JsonElement element : jsonArray) {
                            // blacklist.add(Registries.ITEM.get(new Identifier(element.getAsString())));
                        }
                    }
                }
            }
            // error writing file
            catch (IOException e) {
                Heckk.error("Could not read file for inv cleaner!");
                e.printStackTrace();
            }
        }
    }
}
