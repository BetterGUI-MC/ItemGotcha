package me.hsgamer.bettergui.itemgotcha;

import java.util.ArrayList;
import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.Permissions;
import me.hsgamer.bettergui.builder.CommandBuilder;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.config.impl.MessageConfig.DefaultMessage;
import me.hsgamer.bettergui.object.addon.Addon;
import me.hsgamer.bettergui.util.CommonUtils;
import me.hsgamer.bettergui.util.TestCase;
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
    getPlugin().getMessageConfig().saveConfig();
    return true;
  }

  @Override
  public void onEnable() {
    itemManager = new ItemManager(this);
    setFailMessage();

    RequirementBuilder.register("item", ItemRequirement.class);
    CommandBuilder.register("give:", ItemCommand.class);

    registerCommand(new BukkitCommand("items",
        "Open the inventory that contains all items from items.yml", "/items",
        new ArrayList<>()) {
      private TestCase<CommandSender> testCase = new TestCase<CommandSender>()
          .setPredicate(sender -> sender instanceof Player)
          .setSuccessConsumer(
              sender -> itemManager.createMenu((Player) sender))
          .setFailConsumer(sender -> CommonUtils.sendMessage(sender,
              getPlugin().getMessageConfig().get(DefaultMessage.PLAYER_ONLY)));

      @Override
      public boolean execute(CommandSender commandSender, String s, String[] strings) {
        return testCase.setTestObject(commandSender).test();
      }
    });
  }

  private void setFailMessage() {
    failMessage = getPlugin().getMessageConfig().get(String.class, "invalid-item",
        "&cUnable to get required item. Inform the staff");
  }

  @Override
  public void onPostEnable() {
    itemManager.initializeMenu();
  }

  @Override
  public void onReload() {
    reloadConfig();
    setFailMessage();
    itemManager.reload();
  }
}
