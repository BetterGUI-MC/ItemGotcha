package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.util.SchedulerUtil;
import me.hsgamer.hscore.action.common.Action;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GiveItemAction implements Action {
    private final String value;

    public GiveItemAction(ActionBuilder.Input input) {
        this.value = input.getValue();
    }

    @Override
    public void apply(UUID uuid, TaskProcess process, StringReplacer stringReplacer) {
        String parsed = stringReplacer.replaceOrOriginal(value, uuid);
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            process.next();
            return;
        }
        SchedulerUtil.entity(player)
                .run(() -> {
                    try {
                        RequiredItemExecute.get(parsed).giveItem(player);
                    } finally {
                        process.next();
                    }
                }, process::next);
    }
}
