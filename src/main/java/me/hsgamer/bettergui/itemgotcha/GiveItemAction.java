package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.api.action.BaseAction;
import me.hsgamer.bettergui.config.MessageConfig;
import me.hsgamer.bettergui.lib.core.bukkit.utils.MessageUtils;
import me.hsgamer.bettergui.lib.core.common.Validate;
import me.hsgamer.bettergui.lib.taskchain.TaskChain;
import me.hsgamer.bettergui.lib.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

import static me.hsgamer.bettergui.BetterGUI.getInstance;

public class GiveItemAction extends BaseAction {
    public GiveItemAction(String string) {
        super(string);
    }

    private ItemStack getItemStack(Player player, String input) {
        Optional<RequiredItemExecute> optionalRequiredItem = RequiredItemExecute.get(input);
        if (optionalRequiredItem.isPresent()) {
            return optionalRequiredItem.get().getItemStack(player);
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
                MessageUtils.sendMessage(player, MessageConfig.INVALID_NUMBER.getValue().replace("{input}", split[1]));
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
    public void addToTaskChain(UUID uuid, TaskChain<?> taskChain) {
        String parsed = getReplacedString(uuid);
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }
        ItemStack itemStack = getItemStack(player, parsed);
        if (itemStack != null) {
            taskChain.sync(() -> player.getInventory().addItem(itemStack));
        } else {
            getInstance().getLogger().log(Level.WARNING, "Invalid item on {0}", parsed);
            MessageUtils.sendMessage(player, Main.INVALID_ITEM.getValue());
        }
    }
}
