package com.drathonix.dubiousdevices.recipe;

import com.vicious.viciouslibkit.util.map.ItemStackMap;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Should be an interfaces but Might
 */
public abstract class RecipeHandler<T extends ItemRecipe<T>> {
    public abstract void removeRecipe(List<ItemStack> inputs);
    public abstract void removeRecipe(T recipe);
    public abstract void addRecipe(T recipe);
    public abstract T getRecipe(List<ItemStack> inputs);
    public abstract T getRecipe(ItemStackMap inputs);
    public static String itemStacksToString(List<ItemStack> stacks){
        String s = "";
        for (int i = 0; i < stacks.size(); i++) {
            s += stacks.get(i).getType();
            if(i != stacks.size()-1) s += ",";
        }
        return s;
    }
}
