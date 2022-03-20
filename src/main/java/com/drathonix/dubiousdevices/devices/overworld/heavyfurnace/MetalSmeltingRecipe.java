package com.drathonix.dubiousdevices.devices.overworld.heavyfurnace;

import com.drathonix.dubiousdevices.recipe.ItemRecipeExtraOutputs;
import com.drathonix.dubiousdevices.recipe.RecipeParseResult;
import com.drathonix.dubiousdevices.registry.RecipeHandlers;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MetalSmeltingRecipe extends ItemRecipeExtraOutputs<MetalSmeltingRecipe> {
    public MetalSmeltingRecipe(List<ItemStack> inputs, List<ItemStack> outputs){
        super(inputs,outputs);
    }
    public MetalSmeltingRecipe(List<ItemStack> inputs, List<ItemStack> outputs, boolean ignoreNBT){
        super(inputs,outputs, ignoreNBT);
    }
    public MetalSmeltingRecipe(List<ItemStack> inputs, List<ItemStack> outputs, List<String> flags){
        super(inputs,outputs,flags);
    }

    public void addRecipe(List<ItemStack> inputs, List<ItemStack> outputs){
        RecipeHandlers.METALSMELTING.addRecipe(new MetalSmeltingRecipe(inputs,outputs));
    }

    @Override
    public MetalSmeltingRecipe fromRecipeParseResult(RecipeParseResult r) {
        return new MetalSmeltingRecipe(r.inputs,r.outputs,r.flags);
    }
}
