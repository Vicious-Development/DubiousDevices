package com.drathonix.dubiousdevices.registry;

import com.drathonix.dubiousdevices.devices.overworld.crusher.Crusher;
import com.drathonix.dubiousdevices.devices.overworld.crusher.CrusherRecipe;
import com.drathonix.dubiousdevices.devices.overworld.crusher.ExtraDropsCrusherRecipe;
import com.drathonix.dubiousdevices.recipe.MappedRecipeHandler;
import com.drathonix.dubiousdevices.recipe.RecipeHandler;
import com.drathonix.dubiousdevices.util.RecipeHelper;
import com.google.common.collect.Lists;
import com.vicious.viciouslibkit.util.map.ItemStackMap;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecipeHandlers {
    public static final RecipeHandler<CrusherRecipe> CRUSHER = new MappedRecipeHandler<>();
    static{
        Iterator<Recipe> recipes = Bukkit.recipeIterator();
        //CRUSHER INIT Part 1.
        recipes.forEachRemaining((r)->{
            if(r instanceof ShapedRecipe){
                ShapedRecipe sr = (ShapedRecipe)r;
                ItemStackMap map = RecipeHelper.getShapedIngredients(sr);
                if(map.size() == 1) {
                    //Indicates a Block compression.
                    List<ItemStack> mapStacks = map.getStacks();
                    if(mapStacks.get(0).getAmount() > 1) {
                        CRUSHER.addRecipe(new CrusherRecipe(Lists.newArrayList(sr.getResult()), mapStacks));
                    }
                }
            }
        });
        //CRUSHER INIT part 2.
        recipes.forEachRemaining((r)->{
            if(r instanceof ShapelessRecipe){
                ShapelessRecipe sr = (ShapelessRecipe) r;
                ItemStackMap map = RecipeHelper.getShapelessIngredients(sr);
                if(map.size() == 1) {
                    //Indicates an item decompression
                    List<ItemStack> mapStacks = map.getStacks();
                    if(mapStacks.get(0).getAmount() == 1) {
                        CRUSHER.addRecipe(new ExtraDropsCrusherRecipe(mapStacks,Lists.newArrayList(sr.getResult())));
                    }
                }
            }
        });
    }
}
