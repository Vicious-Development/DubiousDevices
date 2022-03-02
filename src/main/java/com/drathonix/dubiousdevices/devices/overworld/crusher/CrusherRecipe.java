package com.drathonix.dubiousdevices.devices.overworld.crusher;

import com.drathonix.dubiousdevices.recipe.ItemRecipeExtraOutputs;
import com.drathonix.dubiousdevices.recipe.RecipeParseResult;
import com.drathonix.dubiousdevices.registry.RecipeHandlers;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * For adding crusher recipes to the game.
 * By default these vanilla recipes are included and are registered in this order (order is important):
 * 1. Any crafting recipe that takes in X items and returns 1.
 * 2. Any crafting recipe that takes in one input.
 */
public class CrusherRecipe extends ItemRecipeExtraOutputs<CrusherRecipe> {
    public CrusherRecipe(List<ItemStack> inputs, List<ItemStack> outputs){
        super(inputs,outputs);
    }
    public CrusherRecipe(List<ItemStack> inputs, List<ItemStack> outputs, boolean ignoreNBT){
        super(inputs,outputs, ignoreNBT);
    }
    public CrusherRecipe(List<ItemStack> inputs, List<ItemStack> outputs, List<String> flags){
        super(inputs,outputs,flags);
    }

    public void addRecipe(List<ItemStack> inputs, List<ItemStack> outputs){
        RecipeHandlers.CRUSHER.addRecipe(new CrusherRecipe(inputs,outputs));
    }

    @Override
    public CrusherRecipe fromRecipeParseResult(RecipeParseResult r) {
        return new CrusherRecipe(r.inputs,r.outputs,r.flags);
    }

    @Override
    public String toString() {
        return "CrusherRecipe{" +
                "inputs=" + inputs +
                ", outputs=" + outputs + (doExtraOutputs ? ", allows extra outputs" : "") +
                '}';
    }
}
