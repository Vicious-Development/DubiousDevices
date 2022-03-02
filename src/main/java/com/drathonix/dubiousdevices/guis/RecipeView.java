package com.drathonix.dubiousdevices.guis;

import com.drathonix.dubiousdevices.inventory.gui.CustomGUIInventory;
import com.drathonix.dubiousdevices.inventory.gui.GUIElement;
import com.drathonix.dubiousdevices.inventory.gui.TickableGUIElement;
import com.drathonix.dubiousdevices.recipe.ItemRecipe;
import com.drathonix.dubiousdevices.recipe.RecipeHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RecipeView {
    public static <T extends ItemRecipe<T>> CustomGUIInventory viewAll(String deviceName, RecipeHandler<T> handler, int startRecipe){
        CustomGUIInventory gui = CustomGUIInventory.newGUI(deviceName + " Recipes",54);
        List<T> recipes = handler.recipes;
        int row = 0;
        int index = 0;
        for (int i = startRecipe; i < recipes.size() && row <= 4; i++) {
            T recipe = recipes.get(i);
            List<String> flagLore = new ArrayList<>();
            flagLore.add(ChatColor.BLUE + "Inputs");
            List<ItemStack> inputs = recipe.cloneInputs();
            if(inputs.size() == 0) continue;
            for (ItemStack input : inputs) {
                flagLore.add(input.getType().toString());
            }
            flagLore.add(ChatColor.GOLD + "Outputs");
            List<ItemStack> outputs = recipe.cloneInputs();
            if(outputs.size() == 0) continue;
            for (ItemStack output : outputs) {
                flagLore.add(output.getType().toString());
            }
            //XIIIIIIDO
            flagLore.add(ChatColor.DARK_BLUE + "Flags");
            flagLore.addAll(recipe.rFlags());
            GUIElement<?> flagElem = GUIElement.loredElement(new ItemStack(Material.FILLED_MAP), ChatColor.AQUA.toString() + ChatColor.BOLD + "Recipe Info",flagLore);
            gui.setElement(flagElem,row,index++);
            List<List<ItemStack>> inputBoxes = fitInto(inputs,6);
            List<ItemStack> l1 = inputBoxes.get(0);
            for (int j = 0; j < l1.size(); j++){
                TickableGUIElement inputElem = new TickableGUIElement(l1.get(j));
                inputElem.lore(ChatColor.BLUE + "Input");
                AtomicInteger boxCycle = new AtomicInteger(1);
                int col = j;
                inputElem.onTick(()->{
                    if(boxCycle.get() >= inputBoxes.size()) boxCycle.set(0);
                    if(inputBoxes.size() <= boxCycle.get()) inputElem.setStack(null);
                    else {
                        List<ItemStack> ln = inputBoxes.get(boxCycle.get());
                        if(ln.size() <= j) inputElem.setStack(null);
                        inputElem.setStack(ln.get(col));
                        inputElem.lore(ChatColor.BLUE + "Input");
                    }
                    boxCycle.set(boxCycle.get()+1);
                });
                gui.setElement(inputElem,row,index);
                index++;
            }
            GUIElement<?> divider = GUIElement.loredElement(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)," ");
            TickableGUIElement outputElem = new TickableGUIElement(outputs.get(0));
            AtomicInteger boxCycle = new AtomicInteger(1);
            outputElem.onTick(()->{
                if(outputs.size() <= boxCycle.get()) boxCycle.set(0);
                else{
                    outputElem.setStack(outputs.get(boxCycle.get()));
                    outputElem.lore(ChatColor.GOLD + "Output");
                }
                boxCycle.set(boxCycle.get()+1);
            });
            row++;
        }
        row = 5;
        if(startRecipe + 5 < recipes.size()){

        }
        return gui;
    }
    public static <T> List<List<T>> fitInto(List<T> lst, int maxListSize){
        List<List<T>> boxes = new ArrayList<>();
        List<T> currentBox = new ArrayList<>();
        for (T t : lst) {
            currentBox.add(t);
            if(currentBox.size() == maxListSize){
                boxes.add(currentBox);
                currentBox = new ArrayList<>();
            }
        }
        return boxes;
    }
}
