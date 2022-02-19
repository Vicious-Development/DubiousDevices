package com.drathonix.dubiousdevices.recipe;

import com.drathonix.dubiousdevices.DubiousDevices;
import com.vicious.viciouslibkit.util.map.ItemStackMap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MappedRecipeHandler<T extends ItemRecipe<T>> extends RecipeHandler<T>{
    //For all the inputs in a recipe, the recipe will be mapped to each input.
    //This speeds up the recipe search speed (And trust me there are many) at the cost of memory.
    private final Map<Material,List<T>> recipes = new EnumMap<>(Material.class);

    /**
     * Recommended method. Faster than the output removal variant.
     */
    public void removeRecipe(List<ItemStack> inputs){
        T recipe = getRecipe(inputs);
        if(recipe == null){
            DubiousDevices.warn("Failed to remove a recipe: Did not find a recipe containing the inputs: " + itemStacksToString(inputs));
            return;
        }
        removeRecipe(recipe);
    }
    public void removeRecipe(T recipe){
        for (ItemStack rin : recipe.inputs) {
            List<T> recList = recipes.get(rin.getType());
            recList.remove(recipe);
            if (recList.isEmpty()) recipes.remove(rin.getType());
        }
    }
    public void addRecipe(T recipe){
        for (ItemStack input : recipe.inputs) {
            recipes.putIfAbsent(input.getType(),new ArrayList<>());
            List<T> list = recipes.get(input.getType());
            list.add(recipe);
        }
    }
    public T getRecipe(List<ItemStack> inputs){
        for (ItemStack input : inputs) {
            List<T> recipeList = recipes.get(input.getType());
            if(recipeList == null) continue;
            for (T recipe : recipeList) {
                if(recipe.matches(inputs)) return recipe;
            }
        }
        return null;
    }
    public T getRecipe(ItemStackMap inputs) {
        for (ItemStack input : inputs.values()) {
            List<T> recipeList = recipes.get(input.getType());
            //Equal to continue in this case.
            if(recipeList == null) continue;
            for (T recipe : recipeList) {
                if(recipe.matches(inputs)) return recipe;
            }
        }
        return null;
    }

}
