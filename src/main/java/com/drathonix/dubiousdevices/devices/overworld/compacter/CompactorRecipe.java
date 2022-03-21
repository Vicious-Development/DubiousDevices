package com.drathonix.dubiousdevices.devices.overworld.compacter;

import com.drathonix.dubiousdevices.recipe.ItemRecipeExtraOutputs;
import com.drathonix.dubiousdevices.recipe.RecipeParseResult;
import com.drathonix.dubiousdevices.registry.RecipeHandlers;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CompactorRecipe extends ItemRecipeExtraOutputs<CompactorRecipe> {
    public CompactorRecipe(List<ItemStack> inputs, List<ItemStack> outputs){
        super(inputs,outputs);
    }
    public CompactorRecipe(List<ItemStack> inputs, List<ItemStack> outputs, boolean ignoreNBT){
        super(inputs,outputs, ignoreNBT);
    }
    public CompactorRecipe(List<ItemStack> inputs, List<ItemStack> outputs, List<String> flags){
        super(inputs,outputs,flags);
    }

    public void addRecipe(List<ItemStack> inputs, List<ItemStack> outputs){
        RecipeHandlers.COMPACTORRECIPE.addRecipe(new CompactorRecipe(inputs,outputs));
    }

    @Override
    public CompactorRecipe fromRecipeParseResult(RecipeParseResult r) {
        return new CompactorRecipe(r.inputs,r.outputs,r.flags);
    }
}
