package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.object.icon.DummyIcon;
import org.bukkit.entity.Player;

public class RequiredItem {

  private final DummyIcon icon;
  private final boolean oldCheck;
  private int amount;

  public RequiredItem(Player player, DummyIcon icon, boolean oldCheck) {
    this.icon = icon;
    this.amount = icon.createClickableItem(player).get().getItem().getAmount();
    this.oldCheck = oldCheck;
  }

  public DummyIcon getIcon() {
    return icon;
  }

  public boolean isOldCheck() {
    return oldCheck;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }
}
