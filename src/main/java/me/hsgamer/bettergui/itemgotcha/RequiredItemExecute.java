package me.hsgamer.bettergui.itemgotcha;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class RequiredItemExecute {
    private final RequiredItem requiredItem;
    private final boolean oldCheck;
    private final Integer amount;

    private RequiredItemExecute(RequiredItem requiredItem, boolean oldCheck, Integer amount) {
        this.requiredItem = requiredItem;
        this.oldCheck = oldCheck;
        this.amount = amount;
    }

    public static Optional<RequiredItemExecute> get(String input) {
        boolean oldCheck = true;
        String[] split = input.split(":", 2);
        String item = split[0].trim();
        if (split.length > 1) {
            oldCheck = Boolean.parseBoolean(split[1].trim());
        }

        String[] split1 = item.split(",", 2);
        Optional<RequiredItem> optional = Manager.getRequiredItem(split1[0].trim());
        if (optional.isPresent()) {
            Integer amount = null;
            if (split1.length > 1) {
                try {
                    amount = Integer.parseInt(split1[1].trim());
                } catch (Exception e) {
                    // IGNORED
                }
            }
            return Optional.of(new RequiredItemExecute(optional.get(), oldCheck, amount));
        } else {
            return Optional.empty();
        }
    }

    public boolean isOldCheck() {
        return oldCheck;
    }

    public Integer getAmount() {
        return amount;
    }

    public RequiredItem getRequiredItem() {
        return requiredItem;
    }

    public boolean check(Player player) {
        return requiredItem.check(player, oldCheck, amount);
    }

    public void take(Player player) {
        requiredItem.take(player, oldCheck, amount);
    }

    public ItemStack getItemStack(Player player) {
        return requiredItem.getItemStack(player, amount);
    }
}
