package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.Permissions;
import me.hsgamer.bettergui.config.MessageConfig;
import me.hsgamer.bettergui.lib.core.bukkit.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GiveItemCommand extends Command {
    private static final Permission PERMISSION = new Permission(Permissions.PREFIX + ".items", null, PermissionDefault.OP);

    public GiveItemCommand() {
        super("giveitem", "Give an item to the player", "/giveitem <item_name>", Collections.emptyList());
        setPermission(PERMISSION.getName());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!testPermission(commandSender)) {
            return false;
        }
        if (!(commandSender instanceof Player)) {
            MessageUtils.sendMessage(commandSender, MessageConfig.PLAYER_ONLY.getValue());
            return false;
        }
        if (strings.length <= 0) {
            MessageUtils.sendMessage(commandSender, Main.ITEM_REQUIRED.getValue());
            return false;
        }
        Optional<RequiredItemExecute> optional = RequiredItemExecute.get(String.join(" ", strings));
        if (optional.isPresent()) {
            ((Player) commandSender).getInventory().addItem(optional.get().getItemStack((Player) commandSender));
            MessageUtils.sendMessage(commandSender, MessageConfig.SUCCESS.getValue());
        } else {
            MessageUtils.sendMessage(commandSender, Main.INVALID_ITEM.getValue());
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
}
