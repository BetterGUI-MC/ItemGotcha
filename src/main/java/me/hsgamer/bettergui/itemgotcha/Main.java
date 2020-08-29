package me.hsgamer.bettergui.itemgotcha;

import static me.hsgamer.bettergui.BetterGUI.getInstance;

import java.util.ArrayList;
import me.hsgamer.bettergui.builder.CommandBuilder;
import me.hsgamer.bettergui.builder.IconBuilder;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.config.MessageConfig;
import me.hsgamer.bettergui.object.addon.Addon;
import me.hsgamer.bettergui.object.icon.DummyIcon;
import me.hsgamer.bettergui.util.MessageUtils;
import me.hsgamer.bettergui.util.PermissionUtils;
import me.hsgamer.bettergui.util.config.path.StringConfigPath;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public final class Main extends Addon {

  public static final StringConfigPath INVALID_ITEM = new StringConfigPath(
      "invalid-item", "&cUnable to get required item. Inform the staff");
  public static final StringConfigPath ITEM_REQUIRED = new StringConfigPath(
      "item-required", "&cYou should specify an item");

  public static final Permission PERMISSION = PermissionUtils
      .createPermission(getInstance().getName().toLowerCase() + ".items", null,
          PermissionDefault.OP);
  private static ItemManager itemManager;

  public static ItemManager getItemManager() {
    return itemManager;
  }

  @Override
  public boolean onLoad() {
    setupConfig();
    INVALID_ITEM.setConfig(getInstance().getMessageConfig());
    ITEM_REQUIRED.setConfig(getInstance().getMessageConfig());
    getInstance().getMessageConfig().saveConfig();
    return true;
  }

  @Override
  public void onEnable() {
    itemManager = new ItemManager(this);

    RequirementBuilder.register(ItemRequirement::new, "item");
    CommandBuilder.register(ItemCommand::new, "give:");
    IconBuilder.register(IconType::new, "itemgotcha");

    registerCommand(new BukkitCommand("items",
        "Open the inventory that contains all items from items.yml", "/items",
        new ArrayList<>()) {
      @Override
      public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
          MessageUtils.sendMessage(commandSender, MessageConfig.PLAYER_ONLY.getValue());
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
        DummyIcon icon = itemManager.getMenu().getIcons().get(strings[0]);
        if (icon != null) {
          ((Player) commandSender).getInventory().addItem(icon.createClickableItem(
              (Player) commandSender).get().getItem());
          MessageUtils.sendMessage(commandSender, MessageConfig.SUCCESS.getValue());
        } else {
          MessageUtils.sendMessage(commandSender, INVALID_ITEM.getValue());
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
