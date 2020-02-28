package me.hsgamer.bettergui.itemgotcha;

import static me.hsgamer.bettergui.BetterGUI.getInstance;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import me.hsgamer.bettergui.config.impl.MessageConfig.DefaultMessage;
import me.hsgamer.bettergui.lib.taskchain.TaskChain;
import me.hsgamer.bettergui.lib.xseries.XMaterial;
import me.hsgamer.bettergui.object.Command;
import me.hsgamer.bettergui.object.icon.DummyIcon;
import me.hsgamer.bettergui.util.CommonUtils;
import me.hsgamer.bettergui.util.Validate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCommand extends Command {

  public ItemCommand(String command) {
    super(command);
  }

  private ItemStack getItemStack(Player player, String input) {
    Map<String, DummyIcon> icons = Main.getItemManager().getMenu().getIcons();
    if (icons.containsKey(input)) {
      return icons.get(input).createClickableItem(player).get().getItem();
    } else {
      String[] split = input.split(",", 2);
      Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(split[0].trim());
      int amount = 1;
      if (xMaterial.isPresent()) {
        if (split.length >= 2) {
          String rawInt = split[1].trim();
          if (Validate.isValidInteger(rawInt)) {
            amount = Integer.parseInt(rawInt);
          } else {
            CommonUtils.sendMessage(player,
                getInstance().getMessageConfig().get(DefaultMessage.INVALID_NUMBER)
                    .replace("{input}", rawInt));
          }
        }
        ItemStack itemStack = xMaterial.get().parseItem();
        if (itemStack != null) {
          itemStack.setAmount(amount);
          return itemStack;
        }
      }
    }
    return null;
  }

  @Override
  public void addToTaskChain(Player player, TaskChain<?> taskChain) {
    String parsed = getParsedCommand(player);
    ItemStack itemStack = getItemStack(player, parsed);
    if (itemStack != null) {
      taskChain.sync(() -> player.getInventory().addItem(itemStack));
    } else {
      getInstance().getLogger().log(Level.WARNING, "Invalid item on {0}", parsed);
      CommonUtils
          .sendMessage(player, Main.getFailMessage());
    }
  }
}
