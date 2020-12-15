package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.api.addon.BetterGUIAddon;
import me.hsgamer.bettergui.lib.core.bukkit.utils.PermissionUtils;
import me.hsgamer.bettergui.lib.core.config.path.StringConfigPath;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import static me.hsgamer.bettergui.BetterGUI.getInstance;

public final class Main extends BetterGUIAddon {

    public static final StringConfigPath INVALID_ITEM = new StringConfigPath("invalid-item", "&cUnable to get required item. Inform the staff");
    public static final StringConfigPath ITEM_REQUIRED = new StringConfigPath("item-required", "&cYou should specify an item");
    public static final Permission PERMISSION = PermissionUtils.createPermission(getInstance().getName().toLowerCase() + ".items", null, PermissionDefault.OP);

    @Override
    public boolean onLoad() {
        setupConfig();
        INVALID_ITEM.setConfig(getInstance().getMessageConfig());
        ITEM_REQUIRED.setConfig(getInstance().getMessageConfig());
        getInstance().getMessageConfig().saveConfig();
        return true;
    }
}
