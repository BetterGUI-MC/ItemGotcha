package me.hsgamer.bettergui.itemgotcha;

import java.util.Map;
import java.util.Optional;
import me.hsgamer.bettergui.object.icon.DummyIcon;
import me.hsgamer.bettergui.util.common.Validate;
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

  public static Optional<RequiredItem> getRequiredItem(String input, Player player) {
    Map<String, DummyIcon> icons = Main.getItemManager().getMenu().getIcons();
    boolean oldCheck = true;
    String[] split = input.split(":", 2);
    String item = split[0].trim();
    if (split.length > 1) {
      oldCheck = Boolean.parseBoolean(split[1].trim());
    }

    String[] split1 = item.split(",", 2);
    split1[0] = split1[0].trim();
    if (icons.containsKey(split1[0])) {
      RequiredItem requiredItem = new RequiredItem(player, icons.get(split1[0]), oldCheck);
      if (split1.length > 1) {
        Validate.getNumber(split1[1].trim())
            .ifPresent(number -> requiredItem.setAmount(number.intValue()));
      }
      return Optional.of(requiredItem);
    } else {
      return Optional.empty();
    }
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
