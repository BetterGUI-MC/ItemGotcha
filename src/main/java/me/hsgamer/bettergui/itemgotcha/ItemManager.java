package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.object.menu.DummyMenu;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ItemManager {

  private DummyMenu menu;
  private FileConfiguration configuration;

  public ItemManager(FileConfiguration config) {
    this.configuration = config;
  }

  public void initializeMenu() {
    menu = new DummyMenu("dummyitems");
    menu.setPermission(Main.PERMISSION);
    menu.setFromFile(configuration);
  }

  public void createMenu(Player player) {
    menu.createInventory(player, false);
  }

  public DummyMenu getMenu() {
    return menu;
  }
}
