package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.hscore.config.annotation.ConfigPath;

public interface ExtraMessageConfig {
    @ConfigPath("check-item-success")
    default String getCheckItemSuccess() {
        return "&a✓";
    }

    @ConfigPath("check-item-failed")
    default String getCheckItemFailed() {
        return "&c✗";
    }

    void reloadConfig();
}
