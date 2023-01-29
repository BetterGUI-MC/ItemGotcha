package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.api.action.BaseAction;
import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

import static me.hsgamer.bettergui.BetterGUI.getInstance;

public class GiveItemAction extends BaseAction {
    public GiveItemAction(ActionBuilder.Input input) {
        super(input);
    }

    @Override
    public void accept(UUID uuid, TaskProcess process) {
        String parsed = getReplacedString(uuid);
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            process.next();
            return;
        }
        Bukkit.getScheduler().runTask(getInstance(), () -> {
            RequiredItemExecute.get(parsed).giveItem(player);
            process.next();
        });
    }
}
