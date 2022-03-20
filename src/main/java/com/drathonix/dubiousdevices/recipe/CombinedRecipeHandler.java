package com.drathonix.dubiousdevices.recipe;

import com.vicious.viciouslibkit.util.map.ItemStackMap;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombinedRecipeHandler<T extends ItemRecipe<T>> extends RecipeHandler<T>{
    public List<RecipeHandler<T>> handlers = new ArrayList<>();

    public CombinedRecipeHandler(List<RecipeFlag> validFlags, RecipeConstructor<T> defaultConstructor, RecipeHandler<T>... handlers) {
        super(validFlags, defaultConstructor);
        this.handlers.addAll(Arrays.asList(handlers));
    }

    @Override
    public void removeRecipe(List<ItemStack> inputs) {
        for (RecipeHandler<T> handler : handlers) {
            handler.removeRecipe(inputs);
        }
    }

    @Override
    public T getRecipe(List<ItemStack> inputs) {
        return getRecipe(new ItemStackMap().addAll(inputs));
    }

    @Override
    public T getRecipe(ItemStackMap inputs) {
        for (RecipeHandler<T> handler : handlers) {
            T ret = handler.getRecipe(inputs);
            if(ret != null) return ret;
        }
        return null;
    }

    /**
     * Combined handlers are internal only, no custom recipes will be added via the combined handler.
     */
    @Override
    public void addRecipeAndWrite(T recipe) {

    }
}
