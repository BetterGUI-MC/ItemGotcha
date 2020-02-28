package me.hsgamer.bettergui.itemgotcha;

import me.hsgamer.bettergui.object.menu.DummyMenu;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ItemManager {

  private DummyMenu menu;

  public ItemManager(FileConfiguration config) {
    menu = new DummyMenu("dummyitems");
    menu.setPermission(Main.PERMISSION);
    menu.setFromFile(config);
  }

  public void createMenu(Player player) {
    menu.createInventory(player);
  }

  public DummyMenu getMenu() {
    return menu;
  }
}
