# Lazy AE2 #

*AE2 for people who hate microcrafting*

**Lazy AE2** adds a few more Applied Energistics machines that help speed up certain operations. It was designed with the intent of creating alternate channels for automating AE2's in-world crafting mechanisms, which had a tendency to clash with lag-clearing tools.

![](show.png)

![](ma_chamber.png)

## New Features

* **Fluix Aggregator** - Performs the in-world fluix crystal crafting operation
* **Pulse Centrifuge** - Performs the in-world crystal seed growing operation
* **ME Circuit Etcher** - Etches circuits without needing to waste time pressing the components
* **Crystal Energizer** - Charges certus quartz more efficiently than the AE2 charger
* **Preemptive Assembly Unit** - Alternative to the ME interface that dispatches crafting operations eagerly
    *   Instead of dispatching a recipe one-at-a-time, as much of the recipe's inputs are dumped into the processing machine as possible
    *   Useful for when you're late-game and your processing machines process faster than ME interfaces can provide ingredients
* **ME Level Maintainer** - Maintains a quantity of certain items in an ME network by requesting autocrafting when needed
* **Mass Assembly Chamber** - A really big multi-block molecular assembler. Goes fast and holds lots of patterns!

Everything is JEI-integrated, so you won't have to waste time guessing at recipes.

## Required Dependencies

*   [Applied Energistics 2](https://minecraft.curseforge.com/projects/applied-energistics-2) ([source](https://github.com/AppliedEnergistics/Applied-Energistics-2))
*   [LibNine](https://minecraft.curseforge.com/projects/libnine) ([source](https://github.com/phantamanta44/libnine))

## CraftTweaker Support

Lazy AE2 has full CraftTweaker support for its processing recipes. Check out the documentation [in the wiki](https://github.com/phantamanta44/Lazy-AE2/wiki/CraftTweaker-API).
