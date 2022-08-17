package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.hscore.bukkit.utils.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RequiredItemExecute {
    private final RequiredItem requiredItem;
    private final boolean oldCheck;
    private final Integer amount;

    private RequiredItemExecute(RequiredItem requiredItem, boolean oldCheck, Integer amount) {
        this.requiredItem = requiredItem;
        this.oldCheck = oldCheck;
        this.amount = amount;
    }

    public static RequiredItemExecute get(String input) {
        boolean oldCheck = true;
        String[] split = input.split(":", 2);
        String item = split[0].trim();
        if (split.length > 1) {
            oldCheck = Boolean.parseBoolean(split[1].trim());
        }

        String[] split1 = item.split(",", 2);
        Integer amount = null;
        if (split1.length > 1) {
            try {
                amount = Integer.parseInt(split1[1].trim());
            } catch (Exception e) {
                // IGNORED
            }
        }
        return new RequiredItemExecute(Manager.getRequiredItem(split1[0].trim()), oldCheck, amount);
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

    public ItemUtils.ItemCheckSession createSession(Player player) {
        return requiredItem.createSession(player, oldCheck, amount);
    }

    public ItemStack getItemStack(Player player) {
        return requiredItem.getItemStack(player, amount);
    }

    public void giveItem(Player player) {
        ItemUtils.giveItem(player, getItemStack(player));
    }
}
