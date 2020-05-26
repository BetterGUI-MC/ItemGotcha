package me.hsgamer.bettergui.itemgotcha;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.hsgamer.bettergui.object.ClickableItem;
import me.hsgamer.bettergui.object.Icon;
import me.hsgamer.bettergui.object.Menu;
import me.hsgamer.bettergui.object.property.icon.SimpleIconPropertyBuilder;
import me.hsgamer.bettergui.object.property.icon.impl.ViewRequirement;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class IconType extends Icon {

  private final List<UUID> failToCreate = new ArrayList<>();
  private String icon = "";
  private SimpleIconPropertyBuilder iconPropertyBuilder = new SimpleIconPropertyBuilder(this);
  private boolean checkOnlyOnCreation = false;

  public IconType(String name, Menu<?> menu) {
    super(name, menu);
  }

  public IconType(Icon original) {
    super(original);
    if (original instanceof IconType) {
      this.icon = ((IconType) original).icon;
      this.iconPropertyBuilder = ((IconType) original).iconPropertyBuilder;
      this.checkOnlyOnCreation = ((IconType) original).checkOnlyOnCreation;
    }
  }

  @Override
  public void setFromSection(ConfigurationSection configurationSection) {
    iconPropertyBuilder.init(configurationSection);
    configurationSection.getKeys(false).forEach(key -> {
      if (key.equalsIgnoreCase("icon")) {
        icon = configurationSection.getString(key);
      } else if (key.equalsIgnoreCase("check-only-on-creation")) {
        checkOnlyOnCreation = configurationSection.getBoolean(key);
      }
    });
  }

  @Override
  public Optional<ClickableItem> createClickableItem(Player player) {
    failToCreate.remove(player.getUniqueId());
    ViewRequirement viewRequirement = iconPropertyBuilder.getViewRequirement();
    if (viewRequirement != null) {
      if (!viewRequirement.check(player)) {
        viewRequirement.sendFailCommand(player);
        failToCreate.add(player.getUniqueId());
        return Optional.empty();
      }
      viewRequirement.getCheckedRequirement(player).ifPresent(iconRequirementSet -> {
        iconRequirementSet.take(player);
        iconRequirementSet.sendSuccessCommands(player);
      });
    }
    return getClickableItem(player);
  }

  @Override
  public Optional<ClickableItem> updateClickableItem(Player player) {
    if (checkOnlyOnCreation) {
      if (failToCreate.contains(player.getUniqueId())) {
        return Optional.empty();
      }
    } else {
      ViewRequirement viewRequirement = iconPropertyBuilder.getViewRequirement();
      if (viewRequirement != null && !viewRequirement.check(player)) {
        return Optional.empty();
      }
    }
    return getClickableItem(player);
  }

  private Optional<ClickableItem> getClickableItem(Player player) {
    String parsed = hasVariables(player, icon) ? setVariables(icon, player) : icon;
    return RequiredItem.getRequiredItem(parsed, player)
        .flatMap(requiredItem -> requiredItem.getIcon().createClickableItem(player))
        .map(clickableItem -> new ClickableItem(clickableItem.getItem(),
            iconPropertyBuilder.createClickEvent(player)));
  }
}
