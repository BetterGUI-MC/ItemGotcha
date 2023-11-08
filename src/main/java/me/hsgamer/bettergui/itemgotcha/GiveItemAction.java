package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.api.action.BaseAction;
import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

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
        Scheduler.current().sync().runEntityTaskWithFinalizer(player, () -> RequiredItemExecute.get(parsed).giveItem(player), process::next);
    }
}
