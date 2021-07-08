package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.api.addon.BetterGUIAddon;
import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.lib.core.config.path.StringConfigPath;
import me.hsgamer.bettergui.lib.core.variable.VariableManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import static me.hsgamer.bettergui.BetterGUI.getInstance;

public final class Main extends BetterGUIAddon {

    public static final StringConfigPath INVALID_ITEM = new StringConfigPath("invalid-item", "&cUnable to get required item. Inform the staff");
    public static final StringConfigPath ITEM_REQUIRED = new StringConfigPath("item-required", "&cYou should specify an item");
    public static final StringConfigPath CHECK_ITEM_SUCCESS = new StringConfigPath("check-item-success", "&a✓");
    public static final StringConfigPath CHECK_ITEM_FAILED = new StringConfigPath("check-item-failed", "&c✗");
    private final Command giveCommand = new GiveItemCommand();

    @Override
    public boolean onLoad() {
        Manager.setConfig(getConfig());

        INVALID_ITEM.setConfig(getInstance().getMessageConfig());
        ITEM_REQUIRED.setConfig(getInstance().getMessageConfig());
        CHECK_ITEM_SUCCESS.setConfig(getInstance().getMessageConfig());
        CHECK_ITEM_FAILED.setConfig(getInstance().getMessageConfig());
        getInstance().getMessageConfig().save();
        return true;
    }

    @Override
    public void onEnable() {
        Manager.load();

        ActionBuilder.INSTANCE.register(GiveItemAction::new, "give-item", "give");
        RequirementBuilder.INSTANCE.register(ItemRequirement::new, "item");

        VariableManager.register("check_item_", (original, uuid) -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                return "";
            }
            return RequiredItemExecute.get(original).map(requiredItemExecute -> requiredItemExecute.check(player) ? CHECK_ITEM_SUCCESS.getValue() : CHECK_ITEM_FAILED.getValue()).orElse(null);
        });
        registerCommand(giveCommand);
    }

    @Override
    public void onReload() {
        Manager.clear();
        reloadConfig();
        Manager.load();
    }

    @Override
    public void onDisable() {
        Manager.clear();
        unregisterCommand(giveCommand);
    }
}
