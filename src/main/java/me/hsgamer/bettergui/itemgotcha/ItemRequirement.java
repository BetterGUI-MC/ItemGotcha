package me.hsgamer.bettergui.itemgotcha;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import me.hsgamer.bettergui.lib.xseries.XMaterial;
import me.hsgamer.bettergui.object.Requirement;
import me.hsgamer.bettergui.object.icon.DummyIcon;
import me.hsgamer.bettergui.object.property.item.ItemProperty;
import me.hsgamer.bettergui.object.property.item.impl.Amount;
import me.hsgamer.bettergui.util.CommonUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemRequirement extends Requirement<Object, List<RequiredItem>> {

  public ItemRequirement() {
    super(true);
  }

  @Override
  public List<RequiredItem> getParsedValue(Player player) {
    List<RequiredItem> list = new ArrayList<>();
    for (String s : CommonUtils.createStringListFromObject(value, true)) {
      Optional<RequiredItem> optional = RequiredItem.getRequiredItem(s, player);
      if (optional.isPresent()) {
        list.add(optional.get());
      } else {
        CommonUtils.sendMessage(player, Main.INVALID_ITEM.getValue());
      }
    }
    return list;
  }


  @Override
  public boolean check(Player player) {
    for (RequiredItem requiredItem : getParsedValue(player)) {
      DummyIcon dummyIcon = requiredItem.getIcon();
      int amountNeeded = requiredItem.getAmount();
      int amountFound = 0;
      for (ItemStack item : player.getInventory().getContents()) {
        if (checkItem(player, dummyIcon, item, requiredItem.isOldCheck())) {
          amountFound += item.getAmount();
        }
      }
      if (amountFound < amountNeeded) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void take(Player player) {
    getParsedValue(player).forEach(requiredItem -> takeSingle(player, requiredItem));
  }

  private void takeSingle(Player player, RequiredItem requiredItem) {
    DummyIcon dummyIcon = requiredItem.getIcon();
    ItemStack[] contents = player.getInventory().getContents();
    int amountNeeded = requiredItem.getAmount();
    for (int i = 0; i < contents.length; i++) {
      ItemStack item = contents[i];
      if (checkItem(player, dummyIcon, item, requiredItem.isOldCheck())) {
        if (item.getAmount() > amountNeeded) {
          item.setAmount(item.getAmount() - amountNeeded);
          return;
        } else {
          amountNeeded -= item.getAmount();
          player.getInventory().setItem(i, XMaterial.AIR.parseItem());
        }
      }
      if (amountNeeded <= 0) {
        return;
      }
    }
  }

  private boolean checkItem(Player player, DummyIcon dummyIcon, ItemStack item, boolean oldCheck) {
    if (item == null) {
      return false;
    }
    if (oldCheck) {
      return item.isSimilar(dummyIcon.createClickableItem(player).get().getItem());
    } else {
      for (ItemProperty<?, ?> property : dummyIcon.getItemProperties().values()) {
        if (property instanceof Amount) {
          continue;
        }
        if (!property.compareWithItemStack(player, item)) {
          return false;
        }
      }
      return true;
    }
  }
}