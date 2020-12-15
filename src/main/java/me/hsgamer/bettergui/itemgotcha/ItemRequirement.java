package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.api.requirement.TakableRequirement;
import me.hsgamer.bettergui.lib.core.common.CollectionUtils;
import me.hsgamer.bettergui.lib.core.variable.VariableManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ItemRequirement extends TakableRequirement<List<RequiredItemExecute>> {
    private final Map<UUID, List<RequiredItemExecute>> checked = new HashMap<>();

    public ItemRequirement(String name) {
        super(name);
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
    public List<RequiredItemExecute> getParsedValue(UUID uuid) {
        List<String> list = CollectionUtils.createStringListFromObject(value, true);
        list.replaceAll(s -> VariableManager.setVariables(s, uuid));
        return list.stream().map(RequiredItemExecute::get).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

    @Override
    protected void takeChecked(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }
        for (RequiredItemExecute execute : checked.remove(uuid)) {
            execute.take(player);
        }
    }

    @Override
    public boolean check(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return true;
        }
        List<RequiredItemExecute> list = getParsedValue(uuid);
        for (RequiredItemExecute execute : list) {
            if (!execute.check(player)) {
                return false;
            }
        }
        checked.put(uuid, list);
        return true;
    }
}
