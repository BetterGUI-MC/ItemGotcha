package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.object.addon.Addon;
import me.hsgamer.bettergui.object.menu.DummyMenu;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public final class ItemManager {

  private final Addon addon;
  private DummyMenu menu;
  private FileConfiguration configuration;

  public ItemManager(Addon addon) {
    this.addon = addon;
    this.configuration = addon.getConfig();
  }

  public void initializeMenu() {
    menu = new DummyMenu("dummyitems");
    menu.setPermission(Main.PERMISSION);
    menu.setFromFile(configuration);
  }

  public void createMenu(Player player) {
    menu.createInventory(player, new String[0], false);
  }

  public void reload() {
    this.configuration = addon.getConfig();
    closeMenu();
    initializeMenu();
  }

  public void closeMenu() {
    menu.closeAll();
  }

  public DummyMenu getMenu() {
    return menu;
  }
}
