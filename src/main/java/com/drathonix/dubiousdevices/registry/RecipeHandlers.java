package com.drathonix.dubiousdevices.registry;

import com.drathonix.dubiousdevices.devices.overworld.crusher.CrusherRecipe;
import com.drathonix.dubiousdevices.recipe.*;
import com.drathonix.dubiousdevices.util.DubiousDirectories;
import com.google.common.collect.Lists;
import com.vicious.viciouslibkit.util.map.ItemStackMap;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.*;

public class RecipeHandlers {
    public static final Map<String, RecipeHandler<?>> handlers = new HashMap<>();
    public static final MappedRecipeHandler.Named<CrusherRecipe> CRUSHER = add(new MappedRecipeHandler.Named<CrusherRecipe>("Crusher", DubiousDirectories.ddrecipes,Lists.newArrayList(DDRecipeFlags.ALLOWEXTRAOUTPUTS),CrusherRecipe::new));
    private static <T extends ItemRecipe<T>> MappedRecipeHandler.Named<T> add(MappedRecipeHandler.Named<T> rh) {
        handlers.put(rh.name.toLowerCase(Locale.ROOT),rh);
        handlers.put(rh.name,rh);
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
                                CRUSHER.addRecipe(new CrusherRecipe(mapStacks, Lists.newArrayList(r.getResult()),Lists.newArrayList(DDRecipeFlags.NONBT.name,DDRecipeFlags.ALLOWEXTRAOUTPUTS.name)));
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
    }
}
