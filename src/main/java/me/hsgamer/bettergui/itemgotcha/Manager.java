package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.hscore.collections.map.CaseInsensitiveStringMap;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.hscore.config.Config;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Manager {
    private static final Map<String, RequiredItem> requiredItemMap = new CaseInsensitiveStringMap<>(new ConcurrentHashMap<>());
    private static Config config;
    private static ExtraMessageConfig messageConfig;

    private Manager() {
        // EMPTY
    }

    public static void setConfig(Config config) {
        Manager.config = config;
    }

    public static ExtraMessageConfig getMessageConfig() {
        return messageConfig;
    }

    public static void setMessageConfig(ExtraMessageConfig messageConfig) {
        Manager.messageConfig = messageConfig;
    }

    public static void load() {
        config.getNormalizedValues(false)
                .forEach((key, value) -> MapUtils
                        .castOptionalStringObjectMap(value)
                        .ifPresent(map -> requiredItemMap.put(key[0], new RequiredItem(map)))
                );
    }

    public static void clear() {
        requiredItemMap.clear();
    }

    public static RequiredItem getRequiredItem(String name) {
        return requiredItemMap.computeIfAbsent(name, RequiredItem::new);
    }

    public static Map<String, RequiredItem> getRequiredItemMap() {
        return Collections.unmodifiableMap(requiredItemMap);
    }
}
