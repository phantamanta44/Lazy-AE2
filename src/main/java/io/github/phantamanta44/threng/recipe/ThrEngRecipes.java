package io.github.phantamanta44.threng.recipe;

import appeng.api.AEApi;
import appeng.api.definitions.IDefinitions;
import appeng.api.features.IInscriberRegistry;
import appeng.api.features.InscriberProcessType;
import io.github.phantamanta44.libnine.InitMe;
import io.github.phantamanta44.libnine.LibNine;
import io.github.phantamanta44.libnine.recipe.IRecipeList;
import io.github.phantamanta44.libnine.recipe.input.ItemStackInput;
import io.github.phantamanta44.libnine.recipe.output.ItemStackOutput;
import io.github.phantamanta44.libnine.util.helper.ItemUtils;
import io.github.phantamanta44.libnine.util.helper.OreDictUtils;
import io.github.phantamanta44.libnine.util.tuple.ITriple;
import io.github.phantamanta44.threng.item.ItemMaterial;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ThrEngRecipes {

    @InitMe
    public static void registerRecipeTypes() {
        LibNine.PROXY.getRecipeManager().addType(AggRecipe.class);
        LibNine.PROXY.getRecipeManager().addType(PurifyRecipe.class);
        LibNine.PROXY.getRecipeManager().addType(EtchRecipe.class);
    }

    public static void addRecipes() {
        IRecipeList<ITriple<ItemStack, ItemStack, ItemStack>, AggRecipe.Input, ItemStackOutput, AggRecipe> aggRecipes
                = LibNine.PROXY.getRecipeManager().getRecipeList(AggRecipe.class);
        IRecipeList<ItemStack, ItemStackInput, ItemStackOutput, PurifyRecipe> purifyRecipes
                = LibNine.PROXY.getRecipeManager().getRecipeList(PurifyRecipe.class);
        IRecipeList<ITriple<ItemStack, ItemStack, ItemStack>, EtchRecipe.Input, ItemStackOutput, EtchRecipe> etchRecipes
                = LibNine.PROXY.getRecipeManager().getRecipeList(EtchRecipe.class);
        IInscriberRegistry inscriber = AEApi.instance().registries().inscriber();
        IDefinitions defs = AEApi.instance().definitions();

        // steelmaking
        defs.materials().skyDust().maybeStack(1).ifPresent(sd -> inscriber.addRecipe(
                inscriber.builder()
                        .withProcessType(InscriberProcessType.PRESS)
                        .withInputs(Collections.singleton(new ItemStack(Items.IRON_INGOT)))
                        .withTopOptional(ItemMaterial.Type.STEEL_PROCESS_DUST.newStack(1))
                        .withBottomOptional(sd)
                        .withOutput(ItemMaterial.Type.STEEL_PROCESS_INGOT.newStack(1))
                        .build()));
        FurnaceRecipes.instance().addSmeltingRecipe(
                ItemMaterial.Type.STEEL_PROCESS_INGOT.newStack(1), ItemMaterial.Type.FLUIX_STEEL.newStack(1), 0);
        aggRecipes.add(new AggRecipe(
                Stream.of("dustCoal", "dustFluix", "ingotIron")
                        .map(OreDictUtils::matchesOredict).collect(Collectors.toList()),
                ItemMaterial.Type.FLUIX_STEEL.newStack(1)));
        aggRecipes.add(new AggRecipe(
                Stream.of("dustCoal", "dustFluix", "itemSilicon")
                        .map(OreDictUtils::matchesOredict).collect(Collectors.toList()),
                ItemMaterial.Type.STEEL_PROCESS_DUST.newStack(1)));

        // aggregation
        defs.materials().fluixCrystal().maybeStack(2).ifPresent(fc ->
                defs.materials().certusQuartzCrystalCharged().maybeStack(1).ifPresent(ccq ->
                        aggRecipes.add(new AggRecipe(
                                Arrays.asList(OreDictUtils.matchesOredict("gemQuartz"),
                                        OreDictUtils.matchesOredict("dustRedstone"),
                                        ItemUtils.matchesWithWildcard(ccq)), fc))));
        defs.materials().skyDust().maybeStack(1).ifPresent(sd ->
                aggRecipes.add(new AggRecipe(
                        Arrays.asList(OreDictUtils.matchesOredict("gemDiamond"),
                                ItemUtils.matchesWithWildcard(sd),
                                OreDictUtils.matchesOredict("dustEnderPearl")),
                        ItemMaterial.Type.SPACE_GEM.newStack(1))));
        defs.materials().skyDust().maybeStack(1).ifPresent(sd ->
                defs.materials().matterBall().maybeStack(1).ifPresent(mb ->
                        aggRecipes.add(new AggRecipe(
                                Arrays.asList(
                                        ItemUtils.matchesWithWildcard(sd),
                                        ItemUtils.matchesWithWildcard(mb),
                                        ItemUtils.matchesWithWildcard(ItemMaterial.Type.STEEL_PROCESS_DUST.newStack(1))),
                                ItemMaterial.Type.SPEC_CORE.newStack(1)))));

        // crystal purification
        defs.materials().purifiedCertusQuartzCrystal().maybeStack(2).ifPresent(pcq ->
                defs.materials().certusQuartzCrystal().maybeStack(1).ifPresent(cq ->
                        purifyRecipes.add(new PurifyRecipe(ItemUtils.matchesWithWildcard(cq), pcq))));
        defs.materials().purifiedNetherQuartzCrystal().maybeStack(2).ifPresent(pnq ->
                purifyRecipes.add(new PurifyRecipe(ItemUtils.matchesWithWildcard(new ItemStack(Items.QUARTZ)), pnq)));
        defs.materials().purifiedFluixCrystal().maybeStack(2).ifPresent(pfq ->
                defs.materials().fluixCrystal().maybeStack(1).ifPresent(fq ->
                        purifyRecipes.add(new PurifyRecipe(ItemUtils.matchesWithWildcard(fq), pfq))));

        // circuit etching
        defs.materials().logicProcessor().maybeStack(1).ifPresent(lp ->
                etchRecipes.add(new EtchRecipe(OreDictUtils.matchesOredict("ingotGold"), lp)));
        defs.materials().calcProcessor().maybeStack(1).ifPresent(cp ->
                etchRecipes.add(new EtchRecipe(OreDictUtils.matchesOredict("crystalPureCertusQuartz"), cp)));
        defs.materials().engProcessor().maybeStack(1).ifPresent(ep ->
                etchRecipes.add(new EtchRecipe(OreDictUtils.matchesOredict("gemDiamond"), ep)));
        etchRecipes.add(new EtchRecipe(
                ItemUtils.matchesWithWildcard(ItemMaterial.Type.SPACE_GEM.newStack(1)),
                ItemMaterial.Type.PARALLEL_PROCESSOR.newStack(1)));
        etchRecipes.add(new EtchRecipe(
                ItemUtils.matchesWithWildcard(ItemMaterial.Type.SPEC_CORE_64.newStack(1)),
                ItemMaterial.Type.SPEC_PROCESSOR.newStack(1)));

        // misc recipes
        defs.materials().skyDust().maybeStack(1).ifPresent(sd ->
                defs.blocks().skyStoneBlock().maybeStack(1).ifPresent(ss ->
                        purifyRecipes.add(new PurifyRecipe(ItemUtils.matchesWithWildcard(ss), sd))));
        defs.materials().enderDust().maybeStack(1).ifPresent(ed ->
                purifyRecipes.add(new PurifyRecipe(OreDictUtils.matchesOredict("enderpearl"), ed)));
        defs.materials().flour().maybeStack(1).ifPresent(f ->
                purifyRecipes.add(new PurifyRecipe(OreDictUtils.matchesOredict("cropWheat"), f)));
    }

}
