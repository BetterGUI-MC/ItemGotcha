package me.hsgamer.bettergui.itemgotcha;

import java.util.ArrayList;
import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.Permissions;
import me.hsgamer.bettergui.builder.CommandBuilder;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.config.impl.MessageConfig.DefaultMessage;
import me.hsgamer.bettergui.object.addon.Addon;
import me.hsgamer.bettergui.object.icon.DummyIcon;
import me.hsgamer.bettergui.util.CommonUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public final class Main extends Addon {

  public static final Permission PERMISSION = Permissions
      .createPermission(BetterGUI.getInstance().getName().toLowerCase() + ".items", null,
          PermissionDefault.OP);
  private static ItemManager itemManager;
  private static String failMessage;
  private static String requiredMessage;

  public static ItemManager getItemManager() {
    return itemManager;
  }

  public static String getFailMessage() {
    return failMessage;
  }

  @Override
  public boolean onLoad() {
    setupConfig();
    getPlugin().getMessageConfig().getConfig().addDefault("invalid-item",
        "&cUnable to get required item. Inform the staff");
    getPlugin().getMessageConfig().getConfig().addDefault("item-required",
        "&cYou should specify an item");
    getPlugin().getMessageConfig().saveConfig();
    return true;
  }

  @Override
  public void onEnable() {
    itemManager = new ItemManager(this);
    setMessage();

    RequirementBuilder.register("item", ItemRequirement.class);
    CommandBuilder.register("give:", ItemCommand.class);

    registerCommand(new BukkitCommand("items",
        "Open the inventory that contains all items from items.yml", "/items",
        new ArrayList<>()) {
      @Override
      public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
          CommonUtils.sendMessage(commandSender,
              getPlugin().getMessageConfig().get(DefaultMessage.PLAYER_ONLY));
          return false;
        }
        itemManager.createMenu((Player) commandSender);
        return true;
      }
    });
    registerCommand(new BukkitCommand("giveitem",
        "Give an item to the player", "/giveitem <item_name>",
        new ArrayList<>()) {
      @Override
      public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
          CommonUtils.sendMessage(commandSender,
              getPlugin().getMessageConfig().get(DefaultMessage.PLAYER_ONLY));
          return false;
        }
        if (!commandSender.hasPermission(PERMISSION)) {
          CommonUtils.sendMessage(commandSender,
              getPlugin().getMessageConfig().get(DefaultMessage.NO_PERMISSION));
          return false;
        }
        if (strings.length <= 0) {
          CommonUtils.sendMessage(commandSender, requiredMessage);
          return false;
        }
        DummyIcon icon = itemManager.getMenu().getIcons().get(strings[0]);
        if (icon != null) {
          ((Player) commandSender).getInventory().addItem(icon.createClickableItem(
              (Player) commandSender).get().getItem());
          CommonUtils.sendMessage(commandSender,
              getPlugin().getMessageConfig().get(DefaultMessage.SUCCESS));
        } else {
          CommonUtils.sendMessage(commandSender, failMessage);
          return false;
        }
        return true;
      }
    });
  }

  private void setMessage() {
    failMessage = getPlugin().getMessageConfig().get(String.class, "invalid-item",
        "&cUnable to get required item. Inform the staff");
    requiredMessage = getPlugin().getMessageConfig().get(String.class, "item-required",
        "&cYou should specify an item");
  }

  @Override
  public void onPostEnable() {
    itemManager.initializeMenu();
  }

  @Override
  public void onDisable() {
    itemManager.closeMenu();
  }

  @Override
  public void onReload() {
    reloadConfig();
    setMessage();
    itemManager.reload();
  }
}
