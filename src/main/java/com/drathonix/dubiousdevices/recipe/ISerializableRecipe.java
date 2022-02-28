package com.drathonix.dubiousdevices.recipe;

public interface ISerializableRecipe<T> {
    String serialize();
    T fromRecipeParseResult(RecipeParseResult r);
}
