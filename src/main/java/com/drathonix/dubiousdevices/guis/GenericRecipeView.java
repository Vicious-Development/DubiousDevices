package com.drathonix.dubiousdevices.guis;

import com.drathonix.dubiousdevices.recipe.ItemRecipe;
import com.drathonix.dubiousdevices.recipe.RecipeHandler;
import com.vicious.viciouslibkit.inventory.gui.CustomGUIInventory;
import com.vicious.viciouslibkit.inventory.gui.GUIElement;
import com.vicious.viciouslibkit.inventory.gui.TickableGUIElement;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GenericRecipeView {
    public static <T extends ItemRecipe<T>> CustomGUIInventory viewAll(String deviceName, RecipeHandler<T> handler, int startRecipe, Material searchType, RecipeDividerElement<T> divider){
        CustomGUIInventory gui = CustomGUIInventory.newGUI(deviceName + " Recipes",54);
        List<T> recipes = handler.getRecipesFor(searchType);
        int row = 0;
        for (int i = startRecipe; i < recipes.size() && row <= 4; i++) {
            int index = 0;
            T recipe = recipes.get(i);
            List<String> flagLore = new ArrayList<>();
            flagLore.add(ChatColor.BLUE + "Inputs");
            List<ItemStack> inputs = recipe.cloneInputs();
            if(inputs.size() == 0) continue;
            for (ItemStack input : inputs) {
                flagLore.add(input.getType().toString());
            }
            flagLore.add(ChatColor.GOLD + "Outputs");
            List<ItemStack> outputs = recipe.cloneOutputs();
            if(outputs.size() == 0) continue;
            for (ItemStack output : outputs) {
                flagLore.add(output.getType().toString());
            }
            //XIIIIIIDO
            flagLore.add(ChatColor.DARK_BLUE + "Flags");
            flagLore.addAll(recipe.rFlags());
            GUIElement<?> flagElem = GUIElement.loredElement(new ItemStack(Material.WRITABLE_BOOK), ChatColor.AQUA.toString() + ChatColor.BOLD + "Recipe Info",flagLore);
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
                        if(ln.size() <= col) inputElem.setStack(null);
                        inputElem.setStack(ln.get(col));
                        inputElem.lore(ChatColor.BLUE + "Input");
                    }
                    boxCycle.set(boxCycle.get()+1);
                });
                gui.setElement(inputElem,row,index);
                index++;
            }
            TickableGUIElement outputElem = new TickableGUIElement(outputs.get(0));
            outputElem.lore(ChatColor.GOLD + "Output");
            AtomicInteger boxCycle = new AtomicInteger(1);
            outputElem.onTick(()->{
                if(outputs.size() <= boxCycle.get()) boxCycle.set(0);
                else{
                    outputElem.setStack(outputs.get(boxCycle.get()));
                    outputElem.lore(ChatColor.GOLD + "Output");
                }
                boxCycle.set(boxCycle.get()+1);
            });
            divider.recipe=recipe;
            gui.setElement(divider,row,7);
            gui.setElement(outputElem,row,8);
            row++;
        }
        row = 5;
        GUIElement<?> nextPage;
        if(startRecipe + 5 < recipes.size()){
            nextPage = GUIElement.loredElement(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE),ChatColor.GOLD.toString() + ChatColor.BOLD + "Next Page");
            nextPage.onLeftClick((ev)->{
                Player plr = (Player) ev.getWhoClicked();
                gui.close(plr);
                viewAll(deviceName,handler,startRecipe+5,null,divider).open(plr);
            });
        }
        else{
            nextPage = GUIElement.loredElement(new ItemStack(Material.GRAY_STAINED_GLASS_PANE),ChatColor.GRAY + "No more pages");
        }
        gui.setElement(nextPage,5,7);
        gui.setElement(nextPage,5,8);
        GUIElement<?> prevPage;
        if(startRecipe-5 >= 0){
            prevPage = GUIElement.loredElement(new ItemStack(Material.BLUE_STAINED_GLASS_PANE),ChatColor.BLUE.toString() + ChatColor.BOLD + "Previous Page");
            prevPage.onLeftClick((ev)->{
                Player plr = (Player) ev.getWhoClicked();
                gui.close(plr);
                viewAll(deviceName,handler,startRecipe-5,null,divider).open(plr);
            });
        }
        else{
            prevPage = GUIElement.loredElement(new ItemStack(Material.GRAY_STAINED_GLASS_PANE),ChatColor.GRAY + "No more pages");
        }
        gui.setElement(prevPage,row,0);
        gui.setElement(prevPage,row,1);
        GUIElement<?> close = GUIElement.loredElement(new ItemStack(Material.RED_STAINED_GLASS_PANE),ChatColor.RED.toString() + ChatColor.BOLD + "CLOSE");
        close.onLeftClick((ev)->{
            gui.close((Player) ev.getWhoClicked());
        });
        gui.setElement(close,row,2);

        gui.setElement(GUIElement.loredElement(new ItemStack(Material.BLUE_STAINED_GLASS_PANE),ChatColor.GREEN.toString() + ChatColor.BOLD + "SEARCH >>>"),row,3);
        GUIElement<?> searcher = GUIElement.loredElement(new ItemStack(Material.CYAN_STAINED_GLASS_PANE),ChatColor.DARK_AQUA.toString() + ChatColor.BOLD +"Place an item here to search for a recipe consuming it!");
        searcher.onLeftClick((ev)->{
            ItemStack toSearch = ev.getCursor();
            viewAll(deviceName,handler,0,toSearch.getType(),divider);
        });
        gui.setElement(searcher,row,4);
        gui.setElement(GUIElement.loredElement(new ItemStack(Material.BLUE_STAINED_GLASS),ChatColor.GREEN.toString() + ChatColor.BOLD + "<<< SEARCH"),row,5);


        gui.setElement(close,row,6);
        return gui;
    }
    public static <T> List<List<T>> fitInto(List<T> lst, int maxListSize){
        List<List<T>> boxes = new ArrayList<>();
        List<T> currentBox = new ArrayList<>();
        for (int i = 0; i < lst.size(); i++) {
            T t = lst.get(i);
            currentBox.add(t);
            if(currentBox.size() == maxListSize || i == lst.size()-1){
                boxes.add(currentBox);
                currentBox = new ArrayList<>();
            }
        }
        return boxes;
    }
}
