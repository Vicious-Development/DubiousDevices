package com.drathonix.dubiousdevices.registry;

import com.drathonix.dubiousdevices.devices.overworld.crusher.CrusherRecipe;
import com.drathonix.dubiousdevices.devices.overworld.crusher.ExtraDropsCrusherRecipe;
import com.drathonix.dubiousdevices.recipe.DDRecipeFlags;
import com.drathonix.dubiousdevices.recipe.MappedRecipeHandler;
import com.drathonix.dubiousdevices.util.DubiousDirectories;
import com.drathonix.dubiousdevices.recipe.RecipeHelper;
import com.google.common.collect.Lists;
import com.vicious.viciouslibkit.util.map.ItemStackMap;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.Iterator;
import java.util.List;

public class RecipeHandlers {
    public static final MappedRecipeHandler.Named<CrusherRecipe> CRUSHER = new MappedRecipeHandler.Named<>("Crusher", DubiousDirectories.ddrecipes);
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
                                CRUSHER.addRecipe(new ExtraDropsCrusherRecipe(mapStacks, Lists.newArrayList(r.getResult()),true));
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
        },(r)->{
            if(r.flags.contains(DDRecipeFlags.ALLOWEXTRAOUTPUTS)){
                return new ExtraDropsCrusherRecipe(r.inputs,r.outputs);
            }
            else return new CrusherRecipe(r.inputs,r.outputs);
        });
    }
}
