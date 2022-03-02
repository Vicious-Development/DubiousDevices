package com.drathonix.dubiousdevices.inventory.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PagedGUI {
    public List<CustomGUIInventory> pages = new ArrayList<>();
    private String title = "";
    public PagedGUI(String title){
        this.title = title;
    }
    public PagedGUI(List<CustomGUIInventory> pages){
        this.pages=pages;
    }
    public PagedGUI(CustomGUIInventory... pages){
        this.pages.addAll(Arrays.asList(pages));
    }
    public CustomGUIInventory newPage(int size){
        CustomGUIInventory gui = CustomGUIInventory.newGUI(title + " : " + pages.size(),size);
        pages.add(gui);
        gui.parent=this;
        return gui;
    }
    public CustomGUIInventory getPage(int page){
        return pages.get(page);
    }
}
