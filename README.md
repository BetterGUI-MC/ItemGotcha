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
***
## Addon Command
| Command | Permission | Description |
| --- | --- | --- |
| `items` | bettergui.items | Open the inventory that contains all items from config.yml |
***
## Command Type
| Command | Description | Argument | Example |
| ------- | ----------- | -------- | ------- |
| `give: <item>` | Give the item to the player | `<item>` <br> The icon in `config.yml`, <br> or the format `<material>` <br> or `<material>,<amount>` | `give: test` <br> <br> `give: DIAMOND_SWORD` <br> <br> `give: STONE, 10`
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
  * You can enable the new method by adding `:false` after the value (Ex: `stone:false`, which `stone` is an icon from config.yml)
* **Examples**
***
```yaml
item: "stone"
```
```yaml
item: "stone:false"
```
```yaml
item:
  value: 
  - "stone:true"
  - "cobblestone"
  take: false
```
***
