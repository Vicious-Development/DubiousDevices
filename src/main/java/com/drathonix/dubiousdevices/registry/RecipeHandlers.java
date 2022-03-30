package com.drathonix.dubiousdevices.registry;

import com.drathonix.dubiousdevices.devices.overworld.compacter.CompactorRecipe;
import com.drathonix.dubiousdevices.devices.overworld.crusher.CrusherRecipe;
import com.drathonix.dubiousdevices.devices.overworld.heavyfurnace.MetalSmeltingRecipe;
import com.drathonix.dubiousdevices.recipe.*;
import com.drathonix.dubiousdevices.util.DubiousDirectories;
import com.google.common.collect.Lists;
import com.vicious.viciouslibkit.util.map.ItemStackMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.*;

import java.util.*;

public class RecipeHandlers {
    public static final Map<String, RecipeHandler<?>> allhandlers = new HashMap<>();
    public static final Map<String, RecipeHandler<?>> viewingHandlers = new HashMap<>();
    public static final MappedRecipeHandler.Named<CrusherRecipe> CRUSHER = add(new MappedRecipeHandler.Named<CrusherRecipe>("Crusher", DubiousDirectories.ddrecipes,Lists.newArrayList(DDRecipeFlags.ALLOWEXTRAOUTPUTS),CrusherRecipe::new),true);
    public static final MappedRecipeHandler.Named<MetalSmeltingRecipe> METALSMELTING = add(new MappedRecipeHandler.Named<MetalSmeltingRecipe>("MetalSmelting", DubiousDirectories.ddrecipes,Lists.newArrayList(DDRecipeFlags.ALLOWEXTRAOUTPUTS), MetalSmeltingRecipe::new),true);
    public static final MappedRecipeHandler.Named<MetalSmeltingRecipe> HEAVYSMELTING = add(new MappedRecipeHandler.Named<MetalSmeltingRecipe>("HeavySmelting", DubiousDirectories.ddrecipes,Lists.newArrayList(DDRecipeFlags.ALLOWEXTRAOUTPUTS), MetalSmeltingRecipe::new),false);
    public static final MappedRecipeHandler.Named<CompactorRecipe> COMPACTOR = add(new MappedRecipeHandler.Named<CompactorRecipe>("Compactor", DubiousDirectories.ddrecipes,Lists.newArrayList(DDRecipeFlags.ALLOWEXTRAOUTPUTS), CompactorRecipe::new),true);
    public static final CombinedRecipeHandler<MetalSmeltingRecipe> HEAVYFURNACE = add(new CombinedRecipeHandler<MetalSmeltingRecipe>(Lists.newArrayList(DDRecipeFlags.ALLOWEXTRAOUTPUTS), MetalSmeltingRecipe::new, METALSMELTING, HEAVYSMELTING),true,"HeavyFurnace");

    private static <T extends RecipeHandler<? extends ItemRecipe<?>>> T add(T rh, boolean addToViewer, String... altNames) {
        if(rh instanceof MappedRecipeHandler.Named) {
            allhandlers.put(((MappedRecipeHandler.Named<?>) rh).name.toLowerCase(Locale.ROOT), rh);
            allhandlers.put(((MappedRecipeHandler.Named<?>) rh).name, rh);
        }
        if(addToViewer){
            if(rh instanceof MappedRecipeHandler.Named) {
                viewingHandlers.put(((MappedRecipeHandler.Named<?>) rh).name.toLowerCase(Locale.ROOT), rh);
                viewingHandlers.put(((MappedRecipeHandler.Named<?>) rh).name, rh);
            }
            for (String altName : altNames) {
                viewingHandlers.put(altName,rh);
                viewingHandlers.put(altName.toLowerCase(Locale.ROOT),rh);
            }
        }
        for (String altName : altNames) {
            allhandlers.put(altName,rh);
            allhandlers.put(altName.toLowerCase(Locale.ROOT),rh);
        }
        return rh;
    }

    static{
        CRUSHER.initIfDNE(()-> {
            Iterator<Recipe> recipes = Bukkit.recipeIterator();
            //CRUSHER INIT Part 1.
            recipes.forEachRemaining((r) -> {
                if(RecipeHelper.isCraftingRecipe(r)) {
                    ItemStackMap map = RecipeHelper.getIngredients(r);
                    if(map.size() == 1) {
                        List<ItemStack> mapStacks = map.getStacks();
                        ItemStack inputStack = mapStacks.get(0);
                        ItemStack outputStack = r.getResult();
                        if (RecipeHelper.isIrreversibleCraftingRecipe(r)) {
                            if (inputStack.getAmount() == 1) {
                                if(RecipeHelper.doesNotHaveDupeRoute(r)) {
                                    CRUSHER.addRecipe(new CrusherRecipe(mapStacks, Lists.newArrayList(r.getResult()), Lists.newArrayList(DDRecipeFlags.NONBT.name, DDRecipeFlags.ALLOWEXTRAOUTPUTS.name)));
                                }
                                else CRUSHER.addRecipe(new CrusherRecipe(mapStacks, Lists.newArrayList(r.getResult()), true));
                            }
                            else if(outputStack.getAmount() == 1){
                                CRUSHER.addRecipe(new CrusherRecipe(Lists.newArrayList(outputStack),mapStacks,true));
                            }
                        } else {
                            if ((inputStack.getAmount() == 4 || inputStack.getAmount() == 9) && r.getResult().getAmount() == 1) {
                                CRUSHER.addRecipe(new CrusherRecipe(Lists.newArrayList(outputStack), mapStacks,true));
                            }
                        }
                    }
                }
            });
        },(r)-> new CrusherRecipe(r.inputs,r.outputs,r.flags));
        METALSMELTING.initIfDNE(()->{
            Iterator<Recipe> recipes = Bukkit.recipeIterator();
            recipes.forEachRemaining((r)->{
                if(!(r instanceof BlastingRecipe)) return;
                BlastingRecipe br = (BlastingRecipe) r;
                List<String> flags = Lists.newArrayList(DDRecipeFlags.NONBT.name);
                if(br.getInput().getType() != Material.ANCIENT_DEBRIS){
                    flags.add(DDRecipeFlags.ALLOWEXTRAOUTPUTS.name);
                }
                if(br.getInputChoice() instanceof RecipeChoice.MaterialChoice){
                    for (Material choice : ((RecipeChoice.MaterialChoice) br.getInputChoice()).getChoices()) {
                        ItemStack in = new ItemStack(choice, br.getInput().getAmount());
                        METALSMELTING.addRecipe(new MetalSmeltingRecipe(Lists.newArrayList(in),Lists.newArrayList(br.getResult()),flags));
                    }
                }
                else METALSMELTING.addRecipe(new MetalSmeltingRecipe(Lists.newArrayList(br.getInput()),Lists.newArrayList(br.getResult()),flags));
            });
        },(r)->new MetalSmeltingRecipe(r.inputs,r.outputs,r.flags));
        HEAVYSMELTING.initIfDNE(()->{},(r)->new MetalSmeltingRecipe(r.inputs,r.outputs,r.flags));
        COMPACTOR.initIfDNE(()->{
            Iterator<Recipe> recipes = Bukkit.recipeIterator();
            recipes.forEachRemaining((r)->{
                if(!(r instanceof ShapedRecipe)) return;
                ShapedRecipe sr = (ShapedRecipe) r;
                ItemStackMap itemMap = RecipeHelper.getShapedIngredients(sr);
                if(itemMap.size() > 1) return;
                List<ItemStack> inputs = itemMap.getStacks();
                if(inputs.get(0).getAmount() == 4 || inputs.get(0).getAmount() == 9 && sr.getResult().getAmount() == 1){
                    COMPACTOR.addRecipe(new CompactorRecipe(inputs,Lists.newArrayList(sr.getResult()),Lists.newArrayList(DDRecipeFlags.NONBT.name,DDRecipeFlags.CANBEACCELERATED.name)));
                }
            });
        },(r)->new CompactorRecipe(r.inputs,r.outputs,r.flags));
    }

    public static void reload() {
        List<RecipeHandler<?>> reloaded = new ArrayList<>();
        for (RecipeHandler<?> value : allhandlers.values()) {
            if(!reloaded.contains(value)){
                value.reload();
                reloaded.add(value);
            }
        }
    }
}
