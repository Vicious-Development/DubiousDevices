package com.drathonix.dubiousdevices.devices.overworld.crusher;

import com.drathonix.dubiousdevices.recipe.DDRecipeFlags;
import com.drathonix.dubiousdevices.recipe.RecipeParseResult;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ExtraDropsCrusherRecipe extends CrusherRecipe{
    public ExtraDropsCrusherRecipe(List<ItemStack> inputs, List<ItemStack> outputs) {
        super(inputs, outputs);
    }
    public ExtraDropsCrusherRecipe(List<ItemStack> inputs, List<ItemStack> outputs, boolean ignoreNBT) {
        super(inputs, outputs, ignoreNBT);
    }

    @Override
    public List<String> rFlags() {
        List<String> flags =  super.rFlags();
        flags.add(DDRecipeFlags.ALLOWEXTRAOUTPUTS);
        return flags;
    }

    @Override
    public String serialize() {
        return RecipeParseResult.serialize(inputs,outputs,rFlags());
    }

    @Override
    public String toString() {
        return "ExtraDropsCrusherRecipe{" +
                "inputs=" + inputs +
                ", outputs=" + outputs +
                '}';
    }
}
