package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.api.addon.GetPlugin;
import me.hsgamer.bettergui.api.addon.PostEnable;
import me.hsgamer.bettergui.api.addon.Reloadable;
import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.builder.ItemModifierBuilder;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.bukkit.utils.ItemUtils;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;
import me.hsgamer.hscore.expansion.common.Expansion;
import me.hsgamer.hscore.expansion.extra.expansion.DataFolder;
import me.hsgamer.hscore.variable.VariableBundle;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.io.File;

public final class Main implements Expansion, DataFolder, GetPlugin, Reloadable, PostEnable {
    private final Command giveCommand = new GiveItemCommand();
    private final Config mainConfig = new BukkitConfig(new File(getDataFolder(), "config.yml"));
    private final ExtraMessageConfig messageConfig = ConfigGenerator.newInstance(ExtraMessageConfig.class, new BukkitConfig(new File(getDataFolder(), "messages.yml")));
    private final VariableBundle variableBundle = new VariableBundle();

    @Override
    public boolean onLoad() {
        mainConfig.setup();
        Manager.setConfig(mainConfig);
        Manager.setMessageConfig(messageConfig);
        return true;
    }

    @Override
    public void onEnable() {
        ActionBuilder.INSTANCE.register(GiveItemAction::new, "give-item", "give");
        RequirementBuilder.INSTANCE.register(ItemRequirement::new, "item");
        ItemModifierBuilder.INSTANCE.register(ItemGotchaModifier::new, "item-gotcha", "required-item");

        variableBundle.register("check_item_", StringReplacer.of((original, uuid) -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                return "";
            }
            RequiredItemExecute execute = RequiredItemExecute.get(original);
            ItemUtils.ItemCheckSession session = execute.createSession(player);
            return session.isAllMatched ? messageConfig.getCheckItemSuccess() : messageConfig.getCheckItemFailed();
        }));
        getPlugin().getCommandManager().register(giveCommand);
    }

    @Override
    public void onPostEnable() {
        Manager.load();
    }

    @Override
    public void onReload() {
        Manager.clear();
        mainConfig.reload();
        messageConfig.reloadConfig();
        Manager.load();
    }

    @Override
    public void onDisable() {
        Manager.clear();
        BetterGUI.getInstance().getCommandManager().unregister(giveCommand);
        variableBundle.unregisterAll();
    }

    public ExtraMessageConfig getMessageConfig() {
        return messageConfig;
    }
}
