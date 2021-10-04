package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.lib.core.bukkit.item.ItemModifier;
import me.hsgamer.bettergui.lib.core.common.interfaces.StringReplacer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class ItemGotchaModifier implements ItemModifier {
    private String name = "";

    @Override
    public String getName() {
        return "item-gotcha";
    }

    @Override
    public ItemStack modify(ItemStack itemStack, UUID uuid, Map<String, StringReplacer> map) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return itemStack;
        }
        return RequiredItemExecute.get(StringReplacer.replace(name, uuid, map.values()))
                .map(requiredItemExecute -> requiredItemExecute.getItemStack(player))
                .orElse(itemStack);
    }

    @Override
    public Object toObject() {
        return name;
    }

    @Override
    public void loadFromObject(Object o) {
        this.name = String.valueOf(o);
    }

    @Override
    public void loadFromItemStack(ItemStack itemStack) {
        // EMPTY
    }

    @Override
    public boolean compareWithItemStack(ItemStack itemStack, UUID uuid, Map<String, StringReplacer> map) {
        return false;
    }
}
