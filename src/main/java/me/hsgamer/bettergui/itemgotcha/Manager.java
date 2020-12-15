package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.lib.core.collections.map.CaseInsensitiveStringHashMap;
import me.hsgamer.bettergui.lib.core.config.Config;
import me.hsgamer.bettergui.lib.simpleyaml.configuration.ConfigurationSection;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class Manager {
    private static final Map<String, RequiredItem> requiredItemMap = new CaseInsensitiveStringHashMap<>();
    private static Config config;

    private Manager() {
        // EMPTY
    }

    public static void setConfig(Config config) {
        Manager.config = config;
    }

    public static void load() {
        config.getConfig().getValues(false).forEach((key, value) -> {
            if (!(value instanceof ConfigurationSection)) {
                return;
            }
            requiredItemMap.put(key, new RequiredItem((ConfigurationSection) value));
        });
    }

    public static void clear() {
        requiredItemMap.clear();
    }

    public static Optional<RequiredItem> getRequiredItem(String name) {
        return Optional.ofNullable(requiredItemMap.get(name));
    }

    public static Map<String, RequiredItem> getRequiredItemMap() {
        return Collections.unmodifiableMap(requiredItemMap);
    }
}
