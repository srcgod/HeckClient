package net.kyc.client.impl.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.item.Item;
import net.kyc.client.api.command.Command;
import net.kyc.client.api.command.ConfigArgumentType;
import net.kyc.client.api.command.ItemArgumentType;
import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.ItemListConfig;
import net.kyc.client.api.macro.Macro;
import net.kyc.client.api.module.Module;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.util.KeyboardUtil;
import net.kyc.client.util.chat.ChatUtil;

import java.util.Arrays;
import java.util.List;

/**
 * @author linus
 * @since 1.0
 */
public class ModuleCommand extends Command {
    //
    private final Module module;

    /**
     * @param module
     */
    public ModuleCommand(Module module) {
        super(module.getName(), module.getDescription(), literal(module.getName().toLowerCase()));
        this.module = module;
    }

    @Override
    public void buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("setting", ConfigArgumentType.config(module))
                .then(argument("value", StringArgumentType.string())
                        .executes(c -> {
                            Config<?> config = ConfigArgumentType.getConfig(c, "setting");
                            String value = StringArgumentType.getString(c, "value");
                            if (value.equalsIgnoreCase("list")) {
                                return listItems(config, value);
                            } else if (value.equalsIgnoreCase("reset")) {
                                config.resetValue();
                                ChatUtil.clientSendMessage("§7%s§f was reset to default value", config.getName());
                                return 1;
                            }
                            return updateValue(config, value);
                        }).then(argument("item", ItemArgumentType.item())
                                .executes(c -> {
                                    Config<?> config = ConfigArgumentType.getConfig(c, "setting");
                                    String action = StringArgumentType.getString(c, "value");
                                    Item value = ItemArgumentType.getItem(c, "item");
                                    return addDeleteItem(config, action, value);
                                }))).executes(c -> {
                                    ChatUtil.error("Must provide a value!");
                                    return 1;
                                })).executes(c -> {
                                    if (module instanceof ToggleModule m) { // Can use the module command to toggle
                                        m.toggle(true);
                                        ChatUtil.clientSendMessage("%s is now %s", "§7" + m.getName() + "§f", m.isEnabled() ? "§senabled§f" : "§cdisabled§f");
                                    }
                                    return 1;
                                });
    }

    private int addDeleteItem(Config<?> config, String action, Item value) {
        if (config instanceof ItemListConfig) {
            List<Item> list = ((List<Item>) config.getValue());
            if (action.equalsIgnoreCase("add")) {
                list.add(value);
                ChatUtil.clientSendMessage("Added §s" + value.getName().getString() + "§f to §7" + config.getName());
            } else if (action.equalsIgnoreCase("del") || action.equalsIgnoreCase("remove")) {
                list.remove(value);
                ChatUtil.clientSendMessage("Removed §c" + value.getName().getString() + "§f from §7" + config.getName());
            }
        }
        return 1;
    }

    private int listItems(Config<?> config, String action) {
        if (config instanceof ItemListConfig) {
            List<Item> list = ((List<Item>) config.getValue());
            if (action.equalsIgnoreCase("list")) {
                if (list.isEmpty()) {
                    ChatUtil.error("There are no items in the list!");
                    return 1;
                }
                StringBuilder listString = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    Item item = list.get(i);
                    listString.append(item.getName().getString());
                    if (i <= list.size() - 1) {
                        listString.append(", ");
                    }
                }
                ChatUtil.clientSendMessage("§7" + config.getName() + "§f: " + listString);
            }
        }
        return 1;
    }

    private int updateValue(Config<?> config, String value) {
        if (config == null || value == null) {
            return 0;
        }
        // parse value
        try {
            if (config.getValue() instanceof Integer) {
                Integer val = Integer.parseInt(value);
                ((Config<Integer>) config).setValue(val);
                ChatUtil.clientSendMessage("§7%s§f was set to §s%s", config.getName(), val.toString());
            } else if (config.getValue() instanceof Float) {
                Float val = Float.parseFloat(value);
                ((Config<Float>) config).setValue(val);
                ChatUtil.clientSendMessage("§7%s§f was set to §s%s", config.getName(), val.toString());
            } else if (config.getValue() instanceof Double) {
                Double val = Double.parseDouble(value);
                ((Config<Double>) config).setValue(val);
                ChatUtil.clientSendMessage("§7%s§f was set to §s%s", config.getName(), val.toString());
            }
        } catch (NumberFormatException e) {
            ChatUtil.error("Not a number!");
            // e.printStackTrace();
        }
        if (config.getValue() instanceof Boolean) {
            Boolean val = Boolean.parseBoolean(value);
            ((Config<Boolean>) config).setValue(val);
            ChatUtil.clientSendMessage("§7%s§f was set to §s%s", config.getName(), val ? "True" : "False");
        } else if (config.getValue() instanceof Enum<?>) {
            String[] values = Arrays.stream(((Enum<?>) config.getValue()).getClass()
                    .getEnumConstants()).map(Enum::name).toArray(String[]::new);
            // TODO: FIX THIS!
            int ix = -1;
            for (int i = 0; i < values.length; i++) {
                if (values[i].equalsIgnoreCase(value)) {
                    ix = i;
                    break;
                }
            }
            if (ix == -1) {
                ChatUtil.error("Not a valid mode!");
                return 0;
            }
            Enum<?> val = Enum.valueOf(((Enum<?>) config.getValue()).getClass(), values[ix]);
            ((Config<Enum<?>>) config).setValue(val);
            ChatUtil.clientSendMessage("§7%s§f was set to mode §s%s", config.getName(), value);
        } else if (config.getValue() instanceof Macro macro) {
            if (config.getName().equalsIgnoreCase("Keybind")) {
                ChatUtil.error("Use the 'bind' command to keybind modules!");
                return 0;
            }
            ((Config<Macro>) config).setValue(new Macro(config.getId(), KeyboardUtil.getKeyCode(value), macro.getRunnable()));
            ChatUtil.clientSendMessage("§7%s§f was set to key §s%s", config.getName(), value);
        } else if (config.getValue() instanceof String) {
            ((Config<String>) config).setValue(value);
        }
        return 1;
    }
}
