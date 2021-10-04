package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.api.action.BaseAction;
import me.hsgamer.bettergui.lib.core.bukkit.utils.MessageUtils;
import me.hsgamer.bettergui.lib.taskchain.TaskChain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
        return optionalRequiredItem.map(requiredItemExecute -> requiredItemExecute.getItemStack(player)).orElse(null);
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
