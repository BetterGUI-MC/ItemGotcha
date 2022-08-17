package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.builder.ItemModifierBuilder;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.hscore.bukkit.addon.PluginAddon;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.bukkit.utils.ItemUtils;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.variable.VariableManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.io.File;

public final class Main extends PluginAddon {
    private final Command giveCommand = new GiveItemCommand();
    private final Config mainConfig = new BukkitConfig(new File(getDataFolder(), "config.yml"));
    private final ExtraMessageConfig messageConfig = new ExtraMessageConfig(new BukkitConfig(new File(getDataFolder(), "messages.yml")));

    @Override
    public boolean onLoad() {
        mainConfig.setup();
        messageConfig.setup();
        Manager.setConfig(mainConfig);
        Manager.setMessageConfig(messageConfig);
        return true;
    }

    @Override
    public void onEnable() {
        ActionBuilder.INSTANCE.register(GiveItemAction::new, "give-item", "give");
        RequirementBuilder.INSTANCE.register(ItemRequirement::new, "item");
        ItemModifierBuilder.INSTANCE.register(ItemGotchaModifier::new, "item-gotcha", "required-item");

        VariableManager.register("check_item_", (original, uuid) -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                return "";
            }
            RequiredItemExecute execute = RequiredItemExecute.get(original);
            ItemUtils.ItemCheckSession session = execute.createSession(player);
            return session.isAllMatched ? messageConfig.checkItemSuccess : messageConfig.checkItemFailed;
        });
        BetterGUI.getInstance().getCommandManager().register(giveCommand);
    }

    @Override
    public void onPostEnable() {
        Manager.load();
    }

    @Override
    public void onReload() {
        Manager.clear();
        mainConfig.reload();
        messageConfig.reload();
        Manager.load();
    }

    @Override
    public void onDisable() {
        Manager.clear();
        BetterGUI.getInstance().getCommandManager().unregister(giveCommand);
    }

    public ExtraMessageConfig getMessageConfig() {
        return messageConfig;
    }
}
