package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.Permissions;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
            MessageUtils.sendMessage(commandSender, BetterGUI.getInstance().getMessageConfig().getPlayerOnly());
            return false;
        }
        if (strings.length == 0) {
            MessageUtils.sendMessage(commandSender, getUsage());
            return false;
        }
        RequiredItemExecute.get(String.join(" ", strings)).giveItem((Player) commandSender);
        MessageUtils.sendMessage(commandSender, BetterGUI.getInstance().getMessageConfig().getSuccess());
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
