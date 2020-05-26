package me.hsgamer.bettergui.itemgotcha;

import java.util.ArrayList;
import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.Permissions;
import me.hsgamer.bettergui.builder.CommandBuilder;
import me.hsgamer.bettergui.builder.IconBuilder;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.config.ConfigPath;
import me.hsgamer.bettergui.config.impl.MessageConfig;
import me.hsgamer.bettergui.object.addon.Addon;
import me.hsgamer.bettergui.object.icon.DummyIcon;
import me.hsgamer.bettergui.util.CommonUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public final class Main extends Addon {

  public static final ConfigPath<String> INVALID_ITEM = new ConfigPath<>(String.class,
      "invalid-item", "&cUnable to get required item. Inform the staff");
  public static final ConfigPath<String> ITEM_REQUIRED = new ConfigPath<>(String.class,
      "item-required", "&cYou should specify an item");

  public static final Permission PERMISSION = Permissions
      .createPermission(BetterGUI.getInstance().getName().toLowerCase() + ".items", null,
          PermissionDefault.OP);
  private static ItemManager itemManager;

  public static ItemManager getItemManager() {
    return itemManager;
  }

  @Override
  public boolean onLoad() {
    setupConfig();
    INVALID_ITEM.setConfig(getPlugin().getMessageConfig());
    ITEM_REQUIRED.setConfig(getPlugin().getMessageConfig());
    getPlugin().getMessageConfig().saveConfig();
    return true;
  }

  @Override
  public void onEnable() {
    itemManager = new ItemManager(this);

    RequirementBuilder.register("item", ItemRequirement.class);
    CommandBuilder.register("give:", ItemCommand.class);
    IconBuilder.register("itemgotcha", IconType.class);

    registerCommand(new BukkitCommand("items",
        "Open the inventory that contains all items from items.yml", "/items",
        new ArrayList<>()) {
      @Override
      public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
          CommonUtils.sendMessage(commandSender, MessageConfig.PLAYER_ONLY.getValue());
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
          CommonUtils.sendMessage(commandSender, MessageConfig.PLAYER_ONLY.getValue());
          return false;
        }
        if (!commandSender.hasPermission(PERMISSION)) {
          CommonUtils.sendMessage(commandSender, MessageConfig.NO_PERMISSION.getValue());
          return false;
        }
        if (strings.length <= 0) {
          CommonUtils.sendMessage(commandSender, ITEM_REQUIRED.getValue());
          return false;
        }
        DummyIcon icon = itemManager.getMenu().getIcons().get(strings[0]);
        if (icon != null) {
          ((Player) commandSender).getInventory().addItem(icon.createClickableItem(
              (Player) commandSender).get().getItem());
          CommonUtils.sendMessage(commandSender, MessageConfig.SUCCESS.getValue());
        } else {
          CommonUtils.sendMessage(commandSender, INVALID_ITEM.getValue());
          return false;
        }
        return true;
      }
    });
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
    itemManager.reload();
  }
}
