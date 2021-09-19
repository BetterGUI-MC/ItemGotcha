package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.builder.ItemModifierBuilder;
import me.hsgamer.bettergui.lib.core.bukkit.item.ItemBuilder;
import me.hsgamer.bettergui.lib.core.bukkit.item.ItemModifier;
import me.hsgamer.bettergui.lib.xseries.XMaterial;
import me.hsgamer.bettergui.modifier.AmountModifier;
import me.hsgamer.bettergui.modifier.XMaterialModifier;
import me.hsgamer.bettergui.utils.CommonStringReplacers;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class RequiredItem {
    private final ItemBuilder itemBuilder = new ItemBuilder()
            .addStringReplacer("variable", CommonStringReplacers.VARIABLE)
            .addStringReplacer("colorize", CommonStringReplacers.COLORIZE)
            .addStringReplacer("expression", CommonStringReplacers.EXPRESSION);

    public RequiredItem(Map<String, Object> section) {
        ItemModifierBuilder.INSTANCE.getItemModifiers(section).forEach(this.itemBuilder::addItemModifier);
    }

    public RequiredItem(XMaterial material) {
        XMaterialModifier materialModifier = new XMaterialModifier();
        materialModifier.loadFromObject(material.name());
        itemBuilder.addItemModifier(materialModifier);
    }

    public ItemStack getItemStack(Player player, Integer amount) {
        ItemStack itemStack = itemBuilder.build(player);
        itemStack.setAmount(amount != null ? amount : itemStack.getAmount());
        return itemStack;
    }

    public boolean check(Player player, boolean oldCheck, Integer amount) {
        int amountNeeded = amount != null ? amount : itemBuilder.build(player).getAmount();
        int amountFound = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (compare(player, item, oldCheck)) {
                amountFound += item.getAmount();
            }
        }
        return amountFound >= amountNeeded;
    }

    private boolean compare(Player player, ItemStack itemStack, boolean oldCheck) {
        if (itemStack == null) {
            return false;
        }
        if (oldCheck) {
            return itemStack.isSimilar(itemBuilder.build(player));
        } else {
            for (ItemModifier itemModifier : itemBuilder.getItemModifiers()) {
                if (itemModifier instanceof AmountModifier) {
                    continue;
                }
                if (!itemModifier.compareWithItemStack(itemStack, player.getUniqueId(), itemBuilder.getStringReplacerMap())) {
                    return false;
                }
            }
            return true;
        }
    }

    public void take(Player player, boolean oldCheck, Integer amount) {
        int amountNeeded = amount != null ? amount : itemBuilder.build(player).getAmount();
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (compare(player, item, oldCheck)) {
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
}
