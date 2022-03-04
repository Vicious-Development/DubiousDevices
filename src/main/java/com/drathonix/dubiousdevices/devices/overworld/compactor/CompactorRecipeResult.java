package com.drathonix.dubiousdevices.devices.overworld.compactor;

import org.bukkit.inventory.ItemStack;

public class CompactorRecipeResult {
    public ItemStack result;
    public ItemStack toReduce;

    public CompactorRecipeResult(ItemStack result, ItemStack toReduce) {
        this.result=result;
        this.toReduce=toReduce;
    }
}
