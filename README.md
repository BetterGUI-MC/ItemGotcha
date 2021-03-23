# ItemGotcha
Actions and Requirements related to Item

## How to use
> This is not easy as you thought, please read [the BetterGUI wiki](https://github.com/BetterGUI-MC/BetterGUI/wiki) first, then read this carefully
### Setup the items
#### Info
* All the items you want to use in the menus will be stored in a special file
* That file is called `config.yml` and it's located in the addon folder (In this case, it's on `plugins/BetterGUI/addon/ItemGotcha`)
### Make an item
* The config should be easy if you are familiar with [**Button**](https://github.com/BetterGUI-MC/BetterGUI/wiki/Button) and, particularly, [**Dummy Button**](https://github.com/BetterGUI-MC/BetterGUI/wiki/Button#dummy-button)
* Yes, you can think of the `config.yml` as a Menu setup, but with [**dummy buttons**](https://github.com/BetterGUI-MC/BetterGUI/wiki/Button#dummy-button)
* Therefore, if you want to make an item, you can set it up in the `config.yml` as how you did in your menu (Name, Material, Lore, etc)
* Also, here are some example items that I made just to demonstrate
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
  - ''
  - '&kYes, it's very dummy and stupid'
```
## Command
> Just a simple command to get yourself the items you made
| Command | Permission | Description |
| --- | --- | --- |
| `giveitem` | bettergui.items | Give an item to the player |
## Action
* **The format**
  * `give: <item>`
  * `give: <item>, <amount>`
  * `give: <material>, <amount>`
* This action will give the player the `<item>` (specified in the [`config.yml`](#setup-the-items)) or the [`<material>`](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html) with the `<amount>`
* Example (with the example [`config.yml`](#setup-the-items))
  * `give: stone`
  * `give: advanced_stone`
  * `give: DIAMOND_SWORD`
  * `give: STONE, 10`
## Requirement Type
* **The format**
```yaml
item: <item>
```
```yaml
item:
  value: <item>
  take: <true/false>
```
```yaml
item: <item>, [amount]
```
```yaml
item: 
  value: <item>, [amount]
  take: <true/false>
```
```yaml
item: <item>, [amount] : [format]
```
```yaml
item:
  value: <item>, [amount] : [format]
  take: <true/false>
```
* This requirement will check if the player has the `<item>` (specified in the [`config.yml`](#setup-the-items)) in his inventory
* You can set the `amount` value to specify the amount of `<item>` the requirement should check
* You can set the `format` value with 2 values
  * `true` if you want the requirement to convert the `<item>` to the final item and check that item with the player's inventory
  * `false` if you want the requirement to check simplier (the material, then item modifiers, and finally the amount).
* You can set the `take` value (`true` or `false`) to allow/disallow the plugin to take the items of the player alter checking successfully
* Example
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
