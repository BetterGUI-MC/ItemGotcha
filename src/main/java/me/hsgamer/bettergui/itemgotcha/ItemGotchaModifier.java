package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.minecraft.item.ItemModifier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

public class ItemGotchaModifier implements ItemModifier<ItemStack> {
    private String name = "";

    @Override
    public @NotNull ItemStack modify(@NotNull ItemStack itemStack, UUID uuid, @NotNull Collection<StringReplacer> stringReplacers) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return itemStack;
        }
        return RequiredItemExecute.get(StringReplacer.replace(name, uuid, stringReplacers)).getItemStack(player);
    }

    @Override
    public Object toObject() {
        return name;
    }

    @Override
    public void loadFromObject(Object o) {
        this.name = String.valueOf(o);
    }
}
