package com.drathonix.dubiousdevices.registry;

import com.drathonix.dubiousdevices.devices.overworld.crusher.CrusherRecipe;
import com.drathonix.dubiousdevices.devices.overworld.heavyfurnace.MetalSmeltingRecipe;
import com.drathonix.dubiousdevices.recipe.*;
import com.drathonix.dubiousdevices.util.DubiousDirectories;
import com.google.common.collect.Lists;
import com.vicious.viciouslibkit.util.map.ItemStackMap;
import org.bukkit.Bukkit;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.*;

public class RecipeHandlers {
    public static final Map<String, RecipeHandler<?>> handlers = new HashMap<>();
    public static final MappedRecipeHandler.Named<CrusherRecipe> CRUSHER = add(new MappedRecipeHandler.Named<CrusherRecipe>("Crusher", DubiousDirectories.ddrecipes,Lists.newArrayList(DDRecipeFlags.ALLOWEXTRAOUTPUTS),CrusherRecipe::new));
    public static final MappedRecipeHandler.Named<MetalSmeltingRecipe> METALSMELTING = add(new MappedRecipeHandler.Named<MetalSmeltingRecipe>("MetalSmelting", DubiousDirectories.ddrecipes,Lists.newArrayList(DDRecipeFlags.ALLOWEXTRAOUTPUTS), MetalSmeltingRecipe::new));
    public static final MappedRecipeHandler.Named<MetalSmeltingRecipe> HEAVYFURNACE = add(new MappedRecipeHandler.Named<MetalSmeltingRecipe>("HeavyFurnace", DubiousDirectories.ddrecipes,Lists.newArrayList(DDRecipeFlags.ALLOWEXTRAOUTPUTS), MetalSmeltingRecipe::new));
    public static final CombinedRecipeHandler<MetalSmeltingRecipe> HEAVYFURNACECOMBINEDHANDLER = new CombinedRecipeHandler<>(Lists.newArrayList(DDRecipeFlags.ALLOWEXTRAOUTPUTS), MetalSmeltingRecipe::new,METALSMELTING,HEAVYFURNACE);

    private static <T extends ItemRecipe<T>> MappedRecipeHandler.Named<T> add(MappedRecipeHandler.Named<T> rh, String... altNames) {
        handlers.put(rh.name.toLowerCase(Locale.ROOT),rh);
        handlers.put(rh.name,rh);
        for (String altName : altNames) {
            handlers.put(altName,rh);
            handlers.put(altName.toLowerCase(Locale.ROOT),rh);
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
                if(RecipeHelper.isMetalOre(br.getInput().getType())){
                    flags.add(DDRecipeFlags.ALLOWEXTRAOUTPUTS.name);
                }
                METALSMELTING.addRecipe(new MetalSmeltingRecipe(Lists.newArrayList(br.getInput()),Lists.newArrayList(br.getResult()),flags));
            });
        },(r)->new MetalSmeltingRecipe(r.inputs,r.outputs,r.flags));
    }
}
