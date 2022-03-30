package com.drathonix.dubiousdevices.devices.overworld.compacter;

import com.drathonix.dubiousdevices.recipe.DDRecipeFlags;
import com.drathonix.dubiousdevices.recipe.ItemRecipeExtraOutputs;
import com.drathonix.dubiousdevices.recipe.RecipeParseResult;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CompactorRecipe extends ItemRecipeExtraOutputs<CompactorRecipe> {
    public boolean accelerate = false;
    public CompactorRecipe(List<ItemStack> inputs, List<ItemStack> outputs){
        super(inputs,outputs);
    }
    public CompactorRecipe(List<ItemStack> inputs, List<ItemStack> outputs, boolean ignoreNBT){
        super(inputs,outputs, ignoreNBT);
    }
    public CompactorRecipe(List<ItemStack> inputs, List<ItemStack> outputs, List<String> flags){
        super(inputs,outputs,flags);
        accelerate = flags.contains(DDRecipeFlags.CANBEACCELERATED.name);
    }

    @Override
    public CompactorRecipe fromRecipeParseResult(RecipeParseResult r) {
        return new CompactorRecipe(r.inputs,r.outputs,r.flags);
    }
}
