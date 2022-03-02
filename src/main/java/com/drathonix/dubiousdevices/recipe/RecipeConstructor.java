package com.drathonix.dubiousdevices.recipe;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface RecipeConstructor <T extends ItemRecipe<T>>{
    T construct(List<ItemStack> inputs, List<ItemStack> outputs, List<String> flags);
}
