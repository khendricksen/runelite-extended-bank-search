# Extended Bank Search

A [RuneLite](https://runelite.net/) Plugin Hub plugin that extends the game's **native bank search
box** (the "Show items whose names contain..." prompt) with equipment-stat, slot, warmth and food
queries.

> **Naming:** the GitHub repository is `runelite-extended-bank-search`; the plugin's identifier on
> the Plugin Hub (the manifest file name under `plugins/` in the
> [plugin-hub](https://github.com/runelite/plugin-hub) repo) is **`extended-bank-search`**. The
> in-game display name is **Extended Bank Search**.

## Example queries

| Query | Meaning |
|---|---|
| `helm` | all head-slot items |
| `prayer` | anything with **any** prayer bonus |
| `prayer>6` | prayer bonus over 6 |
| `slash>30` | slash attack bonus over 30 |
| `prayer>6 magic attack>20` | prayer > 6 **AND** magic attack > 20 |
| `legs dstab>50` | legs slot **AND** stab defence > 50 |
| `helm prayer` | head-slot items that also have any prayer bonus |
| `prayer 2-6` | prayer bonus between 2 and 6 (inclusive) |
| `warm` | warm clothing for Wintertodt |
| `food` | edible items (anything with an "Eat" action) |
| `dragon` | falls through to native name search |

## Search behaviour

### Combining item names with stat searches

- Pure stat/slot/warmth/food queries are owned entirely by this plugin: clean **AND** logic.
- Mixing a **name** with stats (e.g. `dragon prayer>6`) gives an **OR**, not an AND. You'll see
  dragon items *and* prayer>6 items, not dragon items which have prayer bonus.

### A note on ambiguous stat words

`slash`, `crush` and `stab` mean the **attack** bonus. The genuinely
ambiguous words `magic` and `range` also default to **attack**. `magic>20` is magic attack,
`range>20` is ranged attack. Qualify them to get the others:

- `magic attack` / `magic def` / `magic damage`
- `ranged attack` / `ranged def` / `ranged strength`

Multi-word keys always win over the bare default, so `magic def>20` queries defence, not attack.

### Bare words mean "greater than 0"

A stat word with no operator after it means **that bonus > 0**. `prayer` is shorthand for
`prayer>0`; `melee str` is `str>0` (any words not recognised are ignored, then folded into the native name match). Slot words behave the same way on their own:
`helm` shows the whole head slot.

Because of the additive OR above, a bare stat word that's also a common item-name word (like
`magic` and `prayer`) will pull in matching gear **and** anything whose *name* contains that word.
Use compact aliases (`amagic`) if you want the stat only.

## Search terms

Every word below is recognised. Operators (`>`, `<`, `>=`, `<=`, `=`) and ranges (`2-6`) attach to
the **stat** terms; **slot** terms are used on their own.

### Slots

| Slot | Terms |
|---|---|
| Head | `head` `helm` `helmet` `hat` `hood` |
| Cape | `cape` `cloak` `back` |
| Amulet | `amulet` `ammy` `neck` `necklace` |
| Ammo | `ammo` `ammunition` `arrow` `arrows` `bolt` `bolts` `quiver` |
| Weapon | `weapon` `wep` `weap` `mainhand` `main-hand` `main` `2h` |
| Body | `body` `chest` `top` `torso` |
| Shield | `shield` `offhand` `off-hand` `off` |
| Legs | `legs` `leg` `bottom` `bottoms` `trousers` |
| Hands | `hands` `hand` `gloves` `glove` `gauntlets` `vambraces` |
| Feet | `feet` `foot` `boots` `boot` `shoes` |
| Ring | `ring` |

### Stats

Attack and defence words work in **either order**: `magic attack` ≡ `attack magic`, `stab defence`
≡ `defence stab`, and `def` is short for `defence`. `mage` is a synonym of `magic`, and `range` /
`ranged` are interchangeable.

| Bonus | Terms |
|---|---|
| Prayer | `prayer` `pray` |
| Melee strength | `str` `strength` `melee str` `melee strength` |
| Ranged strength | `rstr` `ranged str` `ranged strength` |
| Magic damage % | `mdmg` `magic damage` `magic dmg` |
| Attack — stab | `astab` `stab` `stab attack` `attack stab` |
| Attack — slash | `aslash` `slash` `slash attack` `attack slash` |
| Attack — crush | `acrush` `crush` `crush attack` `attack crush` |
| Attack — magic | `amagic` `magic` `mage` `magic attack` `attack magic` `mage attack` |
| Attack — ranged | `arange` `range` `ranged` `ranged attack` `attack ranged` |
| Defence — stab | `dstab` `stab def` `stab defence` `def stab` `defence stab` |
| Defence — slash | `dslash` `slash def` `slash defence` `def slash` `defence slash` |
| Defence — crush | `dcrush` `crush def` `crush defence` `def crush` `defence crush` |
| Defence — magic | `dmagic` `magic def` `magic defence` `def magic` `defence magic` |
| Defence — ranged | `drange` `ranged def` `ranged defence` `def ranged` `defence ranged` |
| Attack speed | `speed` `aspeed` |

### Warm clothing

`warm` (or `warmth`, `wintertodt`) force-shows every item the OSRS Wiki lists as warm clothing.
Useful for gearing up for Wintertodt without the need for bank tags or Wiki usage. Used on its own, or combined like `warm head`.

### Food

`food` (or `edible`, `eat`) force-shows every item with an "Eat" inventory action. Drinks
(potions, beer, ales, which use "Drink") are deliberately not included. Items with an "Eat" action that do not restore health will also be shown by this search (such as Araxyte venom sacs and Leechfin).

## For developers

### Building / running locally

Requires JDK 11.

```sh
./gradlew run
```

This launches RuneLite in developer mode with the plugin side-loaded. Enable **Extended Bank
Search** in the plugin list, open a bank, and click the search button to exercise the hook.

### Architecture

RuneLite's bank filter fires a `bankSearchFilter` script callback for each item while the search
prompt is open. This plugin hooks that event, using the same mechanism core RuneLite uses for its `ha > 5k` value search, and force-shows items matching the query.

## License

BSD 2-Clause: see [LICENSE](LICENSE).
