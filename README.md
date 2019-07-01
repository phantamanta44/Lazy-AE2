# Lazy AE2 #

*AE2 for people who hate microcrafting*

**Lazy AE2** adds a few more Applied Energistics machines that help speed up certain operations. It was designed with the intent of creating alternate channels for automating AE2's in-world crafting mechanisms, which had a tendency to clash with lag-clearing tools.

![](https://media.forgecdn.net/attachments/252/927/2019-05-22_01.png)

## New Features

*   **Fluix Aggregator** - Performs the in-world fluix crystal crafting operation
*   **Pulse Centrifuge** - Performs the in-world crystal seed growing operation
*   **ME Circuit Etcher** - Etches circuits without needing to waste time pressing the components
*   **Preemptive Assembly Unit** - Alternative to the ME interface that dispatches crafting operations eagerly
    *   Instead of dispatching a recipe one-at-a-time, as much of the recipe's inputs are dumped into the processing machine as possible
    *   Useful for when you're late-game and your processing machines process faster than ME interfaces can provide ingredients
*   **ME Level Maintainer** - Maintains a quantity of certain items in an ME network by requesting autocrafting when needed

Everything is JEI-integrated, so you won't have to waste time guessing at recipes.

## Required Dependencies

*   [Applied Energistics 2](https://minecraft.curseforge.com/projects/applied-energistics-2) ([source](https://github.com/AppliedEnergistics/Applied-Energistics-2))
*   [LibNine](https://minecraft.curseforge.com/projects/libnine) ([source](https://github.com/phantamanta44/libnine))

## CraftTweaker Support

Recipes for the processing machines can be modified as follows:

```zenscript
// add aggregator recipe
// addRecipe(ItemStack output, ItemMatcher input1, ItemMatcher input2, [ItemMatcher input3])
mods.threng.Aggregator.addRecipe(, , , );

// remove aggregator recipe
// removeRecipe(ItemStack output)
mods.threng.Aggregator.removeRecipe();

// add centrifuge recipe
// addRecipe(ItemStack output, ItemMatcher input)
mods.threng.Centrifuge.addRecipe(, );

// remove centrifuge recipe
// removeRecipe(ItemStack output)
mods.threng.Centrifuge.removeRecipe();

// add etcher recipe; redstone and silicon ingredients are fixed
// addRecipe(ItemStack output, ItemMatcher input)
mods.threng.Etcher.addRecipe(, );

// remove etcher recipe
// removeRecipe(ItemStack output)
mods.threng.Etcher.removeRecipe();
```
