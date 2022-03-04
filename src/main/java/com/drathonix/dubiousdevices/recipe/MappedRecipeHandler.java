package com.drathonix.dubiousdevices.recipe;

import com.drathonix.dubiousdevices.DubiousCFG;
import com.drathonix.dubiousdevices.DubiousDevices;
import com.vicious.viciouslib.util.FileUtil;
import com.vicious.viciouslibkit.util.map.ItemStackMap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MappedRecipeHandler<T extends ItemRecipe<T>> extends RecipeHandler<T>{
    //For all the inputs in a recipe, the recipe will be mapped to each input.
    //This speeds up the recipe search speed (And trust me there are many) at the cost of memory.
    protected final Map<Material,List<T>> recipeMap = new EnumMap<>(Material.class);

    public MappedRecipeHandler(List<RecipeFlag> validFlags, RecipeConstructor<T> defaultConstructor) {
        super(validFlags,defaultConstructor);
    }

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
        super.removeRecipe(recipe);
        for (ItemStack rin : recipe.inputs) {
            List<T> recList = recipeMap.get(rin.getType());
            recList.remove(recipe);
            if (recList.isEmpty()) recipeMap.remove(rin.getType());
        }
    }
    public void addRecipeToFront(T recipe) {
        super.addRecipeToFront(recipe);
        for (ItemStack input : recipe.inputs) {
            recipeMap.putIfAbsent(input.getType(),new ArrayList<>());
            List<T> list = recipeMap.get(input.getType());
            if(!list.contains(recipe)) list.add(recipe);
        }
    }
    public void addRecipe(T recipe){
        super.addRecipe(recipe);
        for (ItemStack input : recipe.inputs) {
            recipeMap.putIfAbsent(input.getType(),new ArrayList<>());
            List<T> list = recipeMap.get(input.getType());
            if(!list.contains(recipe)) list.add(recipe);
        }
    }
    public T getRecipe(List<ItemStack> inputs){
        for (ItemStack input : inputs) {
            List<T> recipeList = recipeMap.get(input.getType());
            if(recipeList == null) continue;
            for (T recipe : recipeList) {
                if(recipe.matches(inputs)) return recipe;
            }
        }
        return null;
    }
    public T getRecipe(ItemStackMap inputs) {
        for (ItemStack input : inputs.values()) {
            List<T> recipeList = recipeMap.get(input.getType());
            //Equal to continue in this case.
            if(recipeList == null) continue;
            for (T recipe : recipeList) {
                if(recipe.matches(inputs)) return recipe;
            }
        }
        return null;
    }

    @Override
    public void addRecipeAndWrite(T recipe) {
        addRecipe(recipe);
    }

    public static class Named<T extends ItemRecipe<T>> extends MappedRecipeHandler<T>{
        public final String name;
        private final Path destination;
        public Named(@Nonnull String name, Path directory, List<RecipeFlag> validFlags, RecipeConstructor<T> defaultConstructor){
            super(validFlags,defaultConstructor);
            this.name = name;
            this.destination = FileUtil.toPath(directory.toAbsolutePath() + "/" + name + ".txt");
        }
        public void initIfDNE(Runnable defaultRecipeGenerator, Function<RecipeParseResult,T> recipeDeserializer)  {
            if(!Files.exists(destination)){
                try {
                    Files.createFile(destination);
                    try {
                        defaultRecipeGenerator.run();
                    } catch (Exception e){
                        DubiousDevices.LOGGER.severe(name + " recipe generator failed: " + e.getMessage());
                        e.printStackTrace();
                    }
                    write();
                } catch (IOException e) {
                    DubiousDevices.LOGGER.severe("Failed to generate the recipe file.");
                    e.printStackTrace();
                }
            }
            else{
                List<RecipeParseResult> parses = RecipeLang.parseFile(new File(destination.toAbsolutePath().toString()));
                for (RecipeParseResult parse : parses) {
                    addRecipe(recipeDeserializer.apply(parse));
                }
            }
        }
        public void write() {
            recipes.forEach((r)-> {
                try {
                    Files.write(destination,r.serialize().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                    Files.write(destination,"\n".getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    DubiousDevices.LOGGER.warning("Failed to write a recipe: " + r);
                    e.printStackTrace();
                }
            });
        }
        public void overwrite() {
            try {
                Files.write(destination,"".getBytes(StandardCharsets.UTF_8),StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                DubiousDevices.LOGGER.warning("Failed to clear recipe script file: " + e.getMessage());
                e.printStackTrace();
            }
            recipes.forEach((r)-> {
                try {
                    Files.write(destination,r.serialize().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                    Files.write(destination,"\n".getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    DubiousDevices.LOGGER.warning("Failed to write a recipe: " + r + " caused by " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }


        @Override
        public void addRecipeAndWrite(T r) {
            if (DubiousCFG.getInstance().addRecipesToFront.value()){
                addRecipeToFront(r);
                overwrite();
            }
            else{
                addRecipe(r);
                try {
                    Files.write(destination, r.serialize().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                    Files.write(destination, "\n".getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    DubiousDevices.LOGGER.warning("Failed to write a recipe: " + r + " caused by: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
