package me.hsgamer.bettergui.itemgotcha;

import java.util.Optional;
import me.hsgamer.bettergui.object.ClickableItem;
import me.hsgamer.bettergui.object.Icon;
import me.hsgamer.bettergui.object.Menu;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class IconType extends Icon {

  private String icon = "";

  public IconType(String name, Menu<?> menu) {
    super(name, menu);
  }

  public IconType(Icon original) {
    super(original);
    if (original instanceof IconType) {
      this.icon = ((IconType) original).icon;
    }
  }

  @Override
  public void setFromSection(ConfigurationSection configurationSection) {
    configurationSection.getKeys(false).forEach(key -> {
      if (key.equalsIgnoreCase("icon")) {
        icon = configurationSection.getString(key);
      }
    });
  }

  @Override
  public Optional<ClickableItem> createClickableItem(Player player) {
    String parsed = hasVariables(player, icon) ? setVariables(icon, player) : icon;
    return RequiredItem.getRequiredItem(parsed, player)
        .flatMap(requiredItem -> requiredItem.getIcon().createClickableItem(player));
  }

  @Override
  public Optional<ClickableItem> updateClickableItem(Player player) {
    String parsed = hasVariables(player, icon) ? setVariables(icon, player) : icon;
    return RequiredItem.getRequiredItem(parsed, player)
        .flatMap(requiredItem -> requiredItem.getIcon().updateClickableItem(player));
  }
}
