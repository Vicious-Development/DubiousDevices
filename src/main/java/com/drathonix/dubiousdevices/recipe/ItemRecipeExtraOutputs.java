package com.drathonix.dubiousdevices.recipe;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class ItemRecipeExtraOutputs<T extends ItemRecipe<T>> extends ItemRecipe<T>{
    public boolean doExtraOutputs = false;
    public ItemRecipeExtraOutputs(List<ItemStack> inputs, List<ItemStack> outputs) {
        super(inputs, outputs);
    }

    public ItemRecipeExtraOutputs(List<ItemStack> inputs, List<ItemStack> outputs, boolean ignoreNBT) {
        super(inputs, outputs, ignoreNBT);
    }

    public ItemRecipeExtraOutputs(List<ItemStack> inputs, List<ItemStack> outputs, List<String> flags) {
        super(inputs, outputs, flags);
        doExtraOutputs = flags.contains(DDRecipeFlags.ALLOWEXTRAOUTPUTS.name);
    }

    @Override
    public List<String> rFlags() {
        List<String> flags = super.rFlags();
        if(doExtraOutputs) flags.add(DDRecipeFlags.ALLOWEXTRAOUTPUTS.name);
        return flags;
    }
}
