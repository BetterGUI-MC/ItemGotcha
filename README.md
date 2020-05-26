# ItemGotcha
Commands and Requirements related to Item

## config.yml
***
* **`config.yml` is a special file. It contains all the icons that are used in `give:` and `Item Requirement`**
***
### Info
* Is a Dummy Menu
* Can be opened by using `/items`
* All the icons in this file are Dummy Icons.
### Usage
* Similar settings as other menus
```yaml
stone:
  id: STONE
  amount: 32

advanced_stone:
  id: STONE
  amount: "32 + 5"
  name: "&c{player}'s Stone"
  lore:
  - "A dummy Stone"
```
***
## Addon Command
| Command | Permission | Description |
| --- | --- | --- |
| `items` | bettergui.items | Open the inventory that contains all items from config.yml |
| `giveitem` | bettergui.items | Give an item to the player |
***
## Command Type
| Command | Description | Argument | Example |
| ------- | ----------- | -------- | ------- |
| `give: <item>` | Give the item to the player | `<item>` <br> use the format `<material>` <br> or `<material>, <amount>` <br> where `<material>` is the item in `config.yml` or the [Material](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html) | `give: test` <br> <br> `give: test, 10`  <br> <br> `give: DIAMOND_SWORD` <br> <br> `give: STONE, 10`
***
## Requirement Type
### Item
* **Keyword**: `item`
* **Value Type**: `String` or `List Of String`
* **Final Value**: `List Of String` (names of icons from config.yml)
* **Default Take**: `true`
* **Note**
  * There are two methods of checking required items:
    * Old: The plugin will convert the icon to the final item and check that item with the player's inventory
    * New: The plugin will look through the player's inventory. For each item, The plugin first check the material, then get all item properties and check each of them with the item, and finally check the amount.
  * You can define the amount needed by using the format `<item>, <format>`
  * You can enable the new method by adding `:false` after the value (Ex: `stone:false` or `stone, 10:false`, which `stone` is an icon from config.yml)
* **Examples**
***
```yaml
item: "stone"

item: "stone, 10"

item: "stone:false"

item: "stone, 10:false"

item:
  value: 
  - "stone:true"
  - "cobblestone"
  take: false
```
***
## Icon Type
* This addon also gives an icon type called `itemgotcha`, which represents the item from ItemGotcha's config
* Example on a menu:
```yaml
test-itemgotcha:
  slot: 1
  type: itemgotcha
  icon: stone # name of the item from the ItemGotcha config
  command: "telll: &aI'm a stone"
```
***
