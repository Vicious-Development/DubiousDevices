package com.drathonix.dubiousdevices.recipe;

import com.google.common.collect.Lists;
import com.vicious.viciouslibkit.item.StackType;
import com.vicious.viciouslibkit.util.map.ItemStackMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class RecipeHelper {
    private static Map<Material,Material> metalOreMap = new EnumMap<>(Material.class);
    static{
        metalOreMap.put(Material.RAW_COPPER,Material.COPPER_INGOT);
        metalOreMap.put(Material.RAW_GOLD,Material.GOLD_INGOT);
        metalOreMap.put(Material.RAW_IRON,Material.IRON_INGOT);
        metalOreMap.put(Material.COPPER_ORE,Material.COPPER_INGOT);
        metalOreMap.put(Material.GOLD_ORE,Material.GOLD_INGOT);
        metalOreMap.put(Material.IRON_ORE,Material.IRON_INGOT);
        metalOreMap.put(Material.DEEPSLATE_COPPER_ORE,Material.COPPER_INGOT);
        metalOreMap.put(Material.DEEPSLATE_GOLD_ORE,Material.GOLD_INGOT);
        metalOreMap.put(Material.DEEPSLATE_IRON_ORE,Material.IRON_INGOT);
    }
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
        ItemStackMap ingredients = getIngredients(r);
        if(ingredients == null) return false;
        ItemStack[] recipe = new ItemStack[9];
        for (int i = 0; i < Math.min(r.getResult().getAmount(),9); i++) {
            recipe[i] = new ItemStack(r.getResult().getType());
        }
        Recipe undo = Bukkit.getCraftingRecipe(recipe,Bukkit.getWorlds().get(0));
        if(undo == null) return true;
        return !ingredients.hasAll(Lists.newArrayList(undo.getResult()));
    }

    /**
     * Very rudimentary, only checks a very short line of recipes. Should cover most of MC's recipes though.
     */
    public static boolean doesNotHaveDupeRoute(Recipe r){
        ItemStackMap ingredients = getIngredients(r);
        ItemStack[] recipe = new ItemStack[9];
        for (int i = 0; i < Math.min(r.getResult().getAmount(),9); i++) {
            recipe[i] = new ItemStack(r.getResult().getType());
        }
        Recipe undo = Bukkit.getCraftingRecipe(recipe,Bukkit.getWorlds().get(0));
        if(undo == null) return true;
        for (ItemStack stack : ingredients.values()) {
            List<Recipe> recipesFor = Bukkit.getRecipesFor(stack);
            for (Recipe value : recipesFor) {
                ingredients = getIngredients(value);
                if(ingredients == null) continue;
                System.out.println(value.getResult() + " = " + ingredients.values());
                if(ingredients.containsKey(new StackType(undo.getResult()))) return false;
            }
        }
        return true;
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

    public static boolean isMetalOre(Material type) {
        return metalOreMap.containsKey(type);
    }
}
