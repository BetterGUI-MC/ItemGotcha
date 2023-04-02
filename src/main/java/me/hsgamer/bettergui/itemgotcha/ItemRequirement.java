package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.api.requirement.TakableRequirement;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import me.hsgamer.hscore.bukkit.utils.ItemUtils;
import me.hsgamer.hscore.common.CollectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemRequirement extends TakableRequirement<List<ItemUtils.ItemCheckSession>> {
    public ItemRequirement(RequirementBuilder.Input input) {
        super(input);
    }

    @Override
    protected boolean getDefaultTake() {
        return true;
    }

    @Override
    protected Object getDefaultValue() {
        return Collections.<String>emptyList();
    }

    @Override
    protected List<ItemUtils.ItemCheckSession> convert(Object value, UUID uuid) {
        List<String> list = CollectionUtils.createStringListFromObject(value, true);
        list.replaceAll(s -> StringReplacerApplier.replace(s, uuid, this));
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(RequiredItemExecute::get)
                .map(requiredItemExecute -> requiredItemExecute.createSession(player))
                .collect(Collectors.toList());
    }

    @Override
    protected Result checkConverted(UUID uuid, List<ItemUtils.ItemCheckSession> value) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return Result.success();
        }
        for (ItemUtils.ItemCheckSession session : value) {
            if (!session.isAllMatched) {
                return Result.fail();
            }
        }
        return successConditional((uuid1, process) -> Scheduler.CURRENT.runEntityTaskWithFinalizer(BetterGUI.getInstance(), player, () -> {
            for (ItemUtils.ItemCheckSession session : value) {
                session.takeRunnable.run();
            }
        }, process::next, false));
    }
}
