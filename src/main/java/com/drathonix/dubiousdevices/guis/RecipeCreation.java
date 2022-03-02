package com.drathonix.dubiousdevices.guis;

import com.drathonix.dubiousdevices.inventory.gui.CustomGUIInventory;
import com.drathonix.dubiousdevices.inventory.gui.GUIElement;
import com.drathonix.dubiousdevices.recipe.ItemRecipe;
import com.drathonix.dubiousdevices.recipe.RecipeFlag;
import com.drathonix.dubiousdevices.recipe.RecipeHandler;
import com.vicious.viciouslibkit.util.map.ItemStackMap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class RecipeCreation {
    public static <T extends ItemRecipe<T>> CustomGUIInventory inputsPage(String deviceName, RecipeHandler<T> handler){
        CustomGUIInventory gui = CustomGUIInventory.newGUI(deviceName + " Inputs",27);
        //Allow modification.
        for (GUIElement<?> element : gui.elements) {
            element.cancel = false;
        }
        GUIElement<?> outputsPageElement = GUIElement.loredElement(new ItemStack(Material.LIME_WOOL), ChatColor.GOLD.toString() + ChatColor.BOLD + "Outputs",ChatColor.GREEN + "Click me when you're ready to set the recipe inputs and move to the outputs");
        outputsPageElement.onLeftClick((ev)->{
            RecipeBuilder builder = new RecipeBuilder();
            ItemStackMap stackMap = new ItemStackMap();
            outputsPageElement.setStack(null);
            gui.updateElement(outputsPageElement);
            for (ItemStack s : gui.GUI.getContents()) {
                if(s != null){
                    stackMap.add(s);
                }
            }
            builder.inputs=stackMap.getStacks();
            gui.forceClose();
            outputsPage(deviceName,handler,builder).open((Player) ev.getWhoClicked());
        });
        gui.setElement(outputsPageElement,2,8);
        return gui;
    }
    public static <T extends ItemRecipe<T>> CustomGUIInventory outputsPage(String deviceName, RecipeHandler<T> handler, RecipeBuilder builder) {
        CustomGUIInventory gui = CustomGUIInventory.newGUI(deviceName + " Outputs",27);
        //Allow modification.
        for (GUIElement<?> element : gui.elements) {
            element.cancel = false;
        }
        GUIElement<?> flagsPageElement = GUIElement.loredElement(new ItemStack(Material.LIME_WOOL), ChatColor.GOLD.toString() + ChatColor.BOLD + "Flags",ChatColor.GREEN + "Click me when you're ready to set the recipe outputs and move to the flags");
        flagsPageElement.onLeftClick((ev)->{
            ItemStackMap stackMap = new ItemStackMap();
            flagsPageElement.setStack(null);
            gui.updateElement(flagsPageElement);
            for (ItemStack s : gui.GUI.getContents()) {
                if(s != null){
                    stackMap.add(s);
                }
            }
            builder.outputs=stackMap.getStacks();
            System.out.println(builder.outputs);
            gui.forceClose();
            flagsPage(deviceName,handler,builder).open((Player) ev.getWhoClicked());
        });
        gui.setElement(flagsPageElement,2,8);
        return gui;
    }
    public static <T extends ItemRecipe<T>> CustomGUIInventory flagsPage(String deviceName, RecipeHandler<T> handler, RecipeBuilder builder){
        CustomGUIInventory gui = CustomGUIInventory.newGUI(deviceName + " Flags",54);
        //Allow modification.
        for (GUIElement<?> element : gui.elements) {
            element.cancel = false;
        }
        List<String> activeFlags = new ArrayList<>();
        List<RecipeFlag> validFlags = handler.validFlags();
        for (int i = 0; i < validFlags.size(); i++) {
            RecipeFlag flag = validFlags.get(i);
            GUIElement<?> flagElem = GUIElement.loredElement(new ItemStack(flag.material),flag.name,flag.description, flag.defaultState ? ChatColor.GREEN.toString() + ChatColor.BOLD + "ENABLED" : ChatColor.GREEN.toString() + ChatColor.BOLD + "DISABLED");
            if(flag.defaultState){
                flagElem.toggleEnchant();
                activeFlags.add(flag.name);
            }
            flagElem.onLeftClick((ev)->{
                flagElem.toggleEnchant();
                toggleFlag(activeFlags,flag,flagElem);
                gui.updateElement(flagElem);
            });
            gui.setElement(flagElem,i);
        }
        GUIElement<?> done = GUIElement.loredElement(new ItemStack(Material.LIME_WOOL), ChatColor.GOLD.toString() + ChatColor.BOLD + "Done",ChatColor.GREEN + "Click me when you're ready to set the recipe flags and add the recipe!");
        done.onLeftClick((ev)->{
            builder.flags=activeFlags;
            handler.addRecipeAndWrite(handler.defaultConstructor.construct(builder.inputs, builder.outputs, builder.flags));
            gui.forceClose();
        });
        gui.setElement(done,5,8);
        return gui;
    }

    private static void toggleFlag(List<String> activeFlags, RecipeFlag flag, GUIElement elem) {
        int idx = activeFlags.indexOf(flag.name);
        if(idx == -1){
            activeFlags.add(flag.name);
            ItemStack stack = elem.getStack();
            ItemMeta metaverse = stack.getItemMeta();
            if(metaverse != null) {
                List<String> lore = metaverse.getLore();
                lore.set(lore.size()-1,ChatColor.GREEN.toString() + ChatColor.BOLD + "ENABLED");
                metaverse.setLore(lore);
            }
            stack.setItemMeta(metaverse);
        }
        else {
            activeFlags.remove(idx);
            ItemStack stack = elem.getStack();
            ItemMeta metaverse = stack.getItemMeta();
            if(metaverse != null) {
                List<String> lore = metaverse.getLore();
                lore.set(lore.size()-1,ChatColor.DARK_RED.toString() + ChatColor.BOLD + "DISABLED");
                metaverse.setLore(lore);
            }
            stack.setItemMeta(metaverse);
        }
    }
}
