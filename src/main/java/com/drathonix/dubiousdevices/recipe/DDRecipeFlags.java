package com.drathonix.dubiousdevices.recipe;

import org.bukkit.Material;

public class DDRecipeFlags {

    public static RecipeFlag ALLOWEXTRAOUTPUTS = new RecipeFlag("ALLOWEXTRAOUTPUTS","Enables the ability for a recipe to yield extra items.",Material.NETHERITE_BLOCK);
    public static RecipeFlag NONBT = new RecipeFlag("NONBT","Disables damage checking and the like. On for most recipes by default.", Material.BARRIER,true);
}
