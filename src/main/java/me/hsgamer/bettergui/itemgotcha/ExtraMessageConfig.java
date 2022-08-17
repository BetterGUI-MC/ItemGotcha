package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.annotated.AnnotatedConfig;
import me.hsgamer.hscore.config.annotation.ConfigPath;

public class ExtraMessageConfig extends AnnotatedConfig {
    public final @ConfigPath("check-item-success") String checkItemSuccess;
    public final @ConfigPath("check-item-failed") String checkItemFailed;

    public ExtraMessageConfig(Config config) {
        super(config);
        checkItemSuccess = "&a✓";
        checkItemFailed = "&c✗";
    }
}
