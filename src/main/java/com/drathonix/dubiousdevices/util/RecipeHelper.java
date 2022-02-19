package com.drathonix.dubiousdevices.util;

import com.vicious.viciouslibkit.util.map.ItemStackMap;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.Map;

public class RecipeHelper {
    public static ItemStackMap getShapedIngredients(ShapedRecipe recipe){
        ItemStackMap stackMap = new ItemStackMap();
        Map<Character, ItemStack> dictionary = recipe.getIngredientMap();
        for (String s : recipe.getShape()) {
            for (int i = 0; i < s.length(); i++) {
                ItemStack stack = dictionary.get(s.charAt(i));
                if(stack == null) continue;
                stackMap.add(stack);
            }
        }
        return stackMap;
    }

    public static ItemStackMap getShapelessIngredients(ShapelessRecipe recipe) {
        ItemStackMap stackMap = new ItemStackMap();
        for (ItemStack stack : recipe.getIngredientList()) {
            if(stack == null) continue;
            stackMap.add(stack);
        }
        return stackMap;
    }
}
