package com.drathonix.dubiousdevices.recipe;

import com.drathonix.dubiousdevices.DubiousDevices;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class RecipeLang {
    private static int lineIndex = 0;
    private static String line;
    private static char next(){
        if(lineIndex >= line.length()) return '\0';
        return line.charAt(lineIndex++);
    }
    public static ItemStack getItemStack(){
        char current = Character.toUpperCase(next());
        StringBuilder materialName = new StringBuilder();
        while(current != '*' && current != '>') {
            materialName.append(current);
            current = Character.toUpperCase(next());
        }
        Material m = Material.getMaterial(materialName.toString());
        if(m == null) throw new IllegalArgumentException(materialName + " is not a known item material!");
        if(current == '>') return new ItemStack(m);
        current = next();
        StringBuilder count = new StringBuilder();
        while(current != '>'){
            count.append(current);
            current = next();
        }
        try {
            int num = Integer.parseInt(count.toString());
            return new ItemStack(m,num);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Incorrect number format for: " + count);
        }
    }
    public static void writeToFile(File f, List<ISerializableRecipe<?>> recipes){
        try {
            FileWriter writer = new FileWriter(f);
            for (ISerializableRecipe<?> recipe : recipes) {
                writer.write(recipe.serialize());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<RecipeParseResult> parseFile(File file){
        List<RecipeParseResult> parses = new LinkedList<>();
        try {
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()){
                parses.add(parseLine(scan.nextLine()));
            }
            scan.close();
        } catch (FileNotFoundException e) {
            DubiousDevices.LOGGER.severe("How did we get here?");
            e.printStackTrace();
        }
        return parses;
    }
    public static RecipeParseResult parseLine(String l){
        line=l;
        lineIndex=0;
        char current = next();
        List<ItemStack> inputs = new ArrayList<>();
        List<ItemStack> outputs = new ArrayList<>();
        //Parse inputs
        while(current != '~'){
            if(current == '<'){
                inputs.add(getItemStack());
            }
            current = next();
        }
        //Parse outputs
        current=next();
        while(current != '~' && current != ';'){
            if(current == '<'){
                outputs.add(getItemStack());
            }
            current = next();
        }
        List<String> flags = new ArrayList<>();
        StringBuilder val = new StringBuilder();
        if(current == '~') {
            current = next();
            while (current != ';' && current != '\n' && current != '\0') {
                if (current == ',') {
                    flags.add(val.toString());
                    val = new StringBuilder();
                } else {
                    val.append(current);
                }
                current = next();
            }
            flags.add(val.toString());
        }
        return new RecipeParseResult(inputs,outputs,flags);
    }
}
