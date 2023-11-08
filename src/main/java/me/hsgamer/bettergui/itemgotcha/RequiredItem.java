package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.builder.ItemModifierBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.bukkit.item.BukkitItemBuilder;
import me.hsgamer.hscore.bukkit.item.modifier.AmountModifier;
import me.hsgamer.hscore.bukkit.item.modifier.MaterialModifier;
import me.hsgamer.hscore.bukkit.utils.ItemUtils;
import me.hsgamer.hscore.minecraft.item.ItemComparator;
import me.hsgamer.hscore.minecraft.item.ItemModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class RequiredItem {
    private final BukkitItemBuilder itemBuilder;

    public RequiredItem(Map<String, Object> section) {
        itemBuilder = new BukkitItemBuilder();
        ItemModifierBuilder.INSTANCE.build(section).forEach(this.itemBuilder::addItemModifier);
        StringReplacerApplier.apply(itemBuilder, true);
    }

    public RequiredItem(String material) {
        itemBuilder = new BukkitItemBuilder();
        itemBuilder.addItemModifier(new MaterialModifier().setMaterial(material));
        StringReplacerApplier.apply(itemBuilder, true);
    }

    public ItemStack getItemStack(Player player, Integer amount) {
        ItemStack itemStack = itemBuilder.build(player.getUniqueId());
        itemStack.setAmount(amount != null ? amount : itemStack.getAmount());
        return itemStack;
    }

    public ItemUtils.ItemCheckSession createSession(Player player, boolean oldCheck, Integer amount) {
        int amountNeeded = amount != null ? amount : itemBuilder.build(player.getUniqueId()).getAmount();
        return ItemUtils.createItemCheckSession(player.getInventory(), itemStack -> compare(player, itemStack, oldCheck), amountNeeded);
    }

    private boolean compare(Player player, ItemStack itemStack, boolean oldCheck) {
        if (itemStack == null) {
            return false;
        }
        if (oldCheck) {
            return itemStack.isSimilar(itemBuilder.build(player.getUniqueId()));
        } else {
            for (ItemModifier<ItemStack> itemModifier : itemBuilder.getItemModifiers()) {
                if (itemModifier instanceof AmountModifier) {
                    continue;
                }
                if (!(itemModifier instanceof ItemComparator)) {
                    continue;
                }
                //noinspection unchecked
                ItemComparator<ItemStack> itemComparator = (ItemComparator<ItemStack>) itemModifier;
                if (!itemComparator.compare(itemStack, player.getUniqueId(), itemBuilder.getStringReplacers())) {
                    return false;
                }
            }
            return true;
        }
    }
}
