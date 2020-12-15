package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.api.addon.BetterGUIAddon;
import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.config.MessageConfig;
import me.hsgamer.bettergui.lib.core.bukkit.utils.MessageUtils;
import me.hsgamer.bettergui.lib.core.bukkit.utils.PermissionUtils;
import me.hsgamer.bettergui.lib.core.config.path.StringConfigPath;
import me.hsgamer.bettergui.lib.core.variable.VariableManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static me.hsgamer.bettergui.BetterGUI.getInstance;

public final class Main extends BetterGUIAddon {

    public static final StringConfigPath INVALID_ITEM = new StringConfigPath("invalid-item", "&cUnable to get required item. Inform the staff");
    public static final StringConfigPath ITEM_REQUIRED = new StringConfigPath("item-required", "&cYou should specify an item");
    public static final StringConfigPath CHECK_ITEM_SUCCESS = new StringConfigPath("check-item-success", "&a✓");
    public static final StringConfigPath CHECK_ITEM_FAILED = new StringConfigPath("check-item-failed", "&c✗");
    public static final Permission PERMISSION = PermissionUtils.createPermission(getInstance().getName().toLowerCase() + ".items", null, PermissionDefault.OP);
    private Command giveCommand;

    @Override
    public boolean onLoad() {
        setupConfig();
        Manager.setConfig(getAddonConfig());

        INVALID_ITEM.setConfig(getInstance().getMessageConfig());
        ITEM_REQUIRED.setConfig(getInstance().getMessageConfig());
        CHECK_ITEM_SUCCESS.setConfig(getInstance().getMessageConfig());
        CHECK_ITEM_FAILED.setConfig(getInstance().getMessageConfig());
        getInstance().getMessageConfig().saveConfig();
        return true;
    }

    @Override
    public void onEnable() {
        Manager.load();

        ActionBuilder.INSTANCE.register(GiveItemAction::new, "give-item", "give");
        RequirementBuilder.INSTANCE.register(ItemRequirement::new, "item");

        VariableManager.register("check_item_", (original, uuid) -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                return "";
            }
            return RequiredItemExecute.get(original).map(requiredItemExecute -> requiredItemExecute.check(player) ? CHECK_ITEM_SUCCESS.getValue() : CHECK_ITEM_FAILED.getValue()).orElse(null);
        });

        giveCommand = new BukkitCommand("giveitem", "Give an item to the player", "/giveitem <item_name>", Collections.emptyList()) {
            @Override
            public boolean execute(CommandSender commandSender, String s, String[] strings) {
                if (!(commandSender instanceof Player)) {
                    MessageUtils.sendMessage(commandSender, MessageConfig.PLAYER_ONLY.getValue());
                    return false;
                }
                if (!commandSender.hasPermission(PERMISSION)) {
                    MessageUtils.sendMessage(commandSender, MessageConfig.NO_PERMISSION.getValue());
                    return false;
                }
                if (strings.length <= 0) {
                    MessageUtils.sendMessage(commandSender, ITEM_REQUIRED.getValue());
                    return false;
                }
                Optional<RequiredItem> optional = Manager.getRequiredItem(strings[0]);
                if (optional.isPresent()) {
                    ((Player) commandSender).getInventory().addItem(optional.get().getItemStack((Player) commandSender, null));
                    MessageUtils.sendMessage(commandSender, MessageConfig.SUCCESS.getValue());
                } else {
                    MessageUtils.sendMessage(commandSender, INVALID_ITEM.getValue());
                    return false;
                }
                return true;
            }

            @Override
            public List<String> tabComplete(CommandSender commandSender, String s, String[] strings) {
                if (strings.length == 1) {
                    return new ArrayList<>(Manager.getRequiredItemMap().keySet());
                }
                return super.tabComplete(commandSender, s, strings);
            }
        };
        registerCommand(giveCommand);
    }

    @Override
    public void onReload() {
        Manager.clear();
        reloadConfig();
        Manager.load();
    }

    @Override
    public void onDisable() {
        Manager.clear();
        unregisterCommand(giveCommand);
    }
}
