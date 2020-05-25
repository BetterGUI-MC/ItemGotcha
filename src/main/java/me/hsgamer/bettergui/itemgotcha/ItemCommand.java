package me.hsgamer.bettergui.itemgotcha;

import static me.hsgamer.bettergui.BetterGUI.getInstance;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import me.hsgamer.bettergui.config.impl.MessageConfig;
import me.hsgamer.bettergui.lib.taskchain.TaskChain;
import me.hsgamer.bettergui.lib.xseries.XMaterial;
import me.hsgamer.bettergui.object.Command;
import me.hsgamer.bettergui.object.icon.DummyIcon;
import me.hsgamer.bettergui.util.CommonUtils;
import me.hsgamer.bettergui.util.Validate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCommand extends Command {

  private final Map<String, DummyIcon> icons = Main.getItemManager().getMenu().getIcons();

  public ItemCommand(String command) {
    super(command);
  }

  private ItemStack getItemStack(Player player, String input) {
    int amount = 0;
    String[] split = input.split(",", 2);
    String item = split[0].trim();

    if (split.length > 1) {
      Optional<BigDecimal> optional = Validate.getNumber(split[1].trim());
      if (optional.isPresent()) {
        amount = optional.get().intValue();
      } else {
        CommonUtils.sendMessage(player,
            MessageConfig.INVALID_NUMBER.getValue().replace("{input}", split[1]));
      }
    }

    if (icons.containsKey(item)) {
      ItemStack itemStack = icons.get(item).createClickableItem(player).get().getItem();
      if (amount > 0) {
        itemStack.setAmount(amount);
      }
      return itemStack;
    } else {
      Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(item);
      if (xMaterial.isPresent()) {
        ItemStack itemStack = xMaterial.get().parseItem();
        amount = amount > 0 ? amount : 1;
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
      CommonUtils.sendMessage(player, Main.INVALID_ITEM.getValue());
    }
  }
}
