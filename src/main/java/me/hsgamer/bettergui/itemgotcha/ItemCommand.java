package me.hsgamer.bettergui.itemgotcha;

import static me.hsgamer.bettergui.BetterGUI.getInstance;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.logging.Level;
import me.hsgamer.bettergui.config.MessageConfig;
import me.hsgamer.bettergui.lib.taskchain.TaskChain;
import me.hsgamer.bettergui.lib.xseries.XMaterial;
import me.hsgamer.bettergui.object.Command;
import me.hsgamer.bettergui.util.MessageUtils;
import me.hsgamer.bettergui.util.common.Validate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCommand extends Command {

  public ItemCommand(String command) {
    super(command);
  }

  private ItemStack getItemStack(Player player, String input) {
    Optional<RequiredItem> optionalRequiredItem = RequiredItem.getRequiredItem(input, player);
    if (optionalRequiredItem.isPresent()) {
      RequiredItem requiredItem = optionalRequiredItem.get();
      ItemStack itemStack = requiredItem.getIcon().createClickableItem(player).get().getItem();
      itemStack.setAmount(requiredItem.getAmount());
      return itemStack;
    }

    // Material
    int amount = 0;
    String[] split = input.split(",", 2);
    String mat = split[0].trim();

    if (split.length > 1) {
      Optional<BigDecimal> optional = Validate.getNumber(split[1].trim());
      if (optional.isPresent()) {
        amount = optional.get().intValue();
      } else {
        MessageUtils.sendMessage(player,
            MessageConfig.INVALID_NUMBER.getValue().replace("{input}", split[1]));
      }
    }

    Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(mat);
    if (xMaterial.isPresent()) {
      ItemStack itemStack = xMaterial.get().parseItem();
      amount = amount > 0 ? amount : 1;
      if (itemStack != null) {
        itemStack.setAmount(amount);
        return itemStack;
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
      MessageUtils.sendMessage(player, Main.INVALID_ITEM.getValue());
    }
  }
}
