package com.drathonix.dubiousdevices.devices.overworld.crusher;

import com.drathonix.dubiousdevices.recipe.ItemRecipe;
import com.drathonix.dubiousdevices.recipe.RecipeHandler;
import com.drathonix.dubiousdevices.registry.RecipeHandlers;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * For adding crusher recipes to the game.
 * By default these vanilla recipes are included and are registered in this order (order is important):
 * 1. Any crafting recipe that takes in X items and returns 1.
 * 2. Any crafting recipe that takes in one input.
 */
public class CrusherRecipe extends ItemRecipe<CrusherRecipe> {
    public CrusherRecipe(List<ItemStack> inputs, List<ItemStack> outputs){
        super(inputs,outputs);
    }
    public void addRecipe(List<ItemStack> inputs, List<ItemStack> outputs){
        RecipeHandlers.CRUSHER.addRecipe(new CrusherRecipe(inputs,outputs));
    }
}
