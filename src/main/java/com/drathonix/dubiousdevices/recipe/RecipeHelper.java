package com.drathonix.dubiousdevices.recipe;

import com.google.common.collect.Lists;
import com.vicious.viciouslibkit.util.map.ItemStackMap;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
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
    public static boolean isIrreversibleCraftingRecipe(Recipe r){
        ItemStackMap ingredients;
        if(r instanceof ShapedRecipe){
            ingredients = getShapedIngredients((ShapedRecipe) r);
        }
        else if(r instanceof ShapelessRecipe){
            ingredients = getShapelessIngredients((ShapelessRecipe) r);
        }
        else return false;
        ItemStack[] recipe = new ItemStack[9];
        for (int i = 0; i < Math.min(r.getResult().getAmount(),9); i++) {
            recipe[i] = new ItemStack(r.getResult().getType());
        }
        Recipe undo = Bukkit.getCraftingRecipe(recipe,Bukkit.getWorlds().get(0));
        if(undo == null) return true;
        return !ingredients.hasAll(Lists.newArrayList(undo.getResult()));
    }
    public static ItemStackMap getShapelessIngredients(ShapelessRecipe recipe) {
        ItemStackMap stackMap = new ItemStackMap();
        for (ItemStack stack : recipe.getIngredientList()) {
            if(stack == null) continue;
            stackMap.add(stack);
        }
        return stackMap;
    }

    public static boolean isCraftingRecipe(Recipe r) {
        return r instanceof ShapedRecipe || r instanceof ShapelessRecipe;
    }

    public static ItemStackMap getIngredients(Recipe r) {
        if(r instanceof ShapelessRecipe) return getShapelessIngredients((ShapelessRecipe) r);
        if(r instanceof ShapedRecipe) return getShapedIngredients((ShapedRecipe) r);
        return null;
    }
}
