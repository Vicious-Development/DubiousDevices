package com.drathonix.dubiousdevices.devices.overworld.compactor;

import com.vicious.viciouslib.database.objectTypes.SQLVector3i;
import com.vicious.viciouslibkit.util.map.ItemStackMap;
import com.vicious.viciouslibkit.worldcrafting.InWorldItemCrafting;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.inventory.*;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Compactor implements Listener {
    public static void tick() {
    }
    @EventHandler
    @SuppressWarnings("all")
    public void pistonRetract(BlockPistonRetractEvent ev){
        Location l = ev.getBlock().getLocation().add(ev.getDirection().getDirection());
        InWorldItemCrafting.emptyLocation(l.getWorld(),l);
    }
    @EventHandler
    public void pistonExtend(BlockPistonExtendEvent ev){
        if(ev.isCancelled()) return;
        if(!ev.getBlocks().isEmpty()) return;
        Location l = ev.getBlock().getLocation();
        World w = l.getWorld();
        Block b = w.getBlockAt(l.add(ev.getDirection().getDirection()));
        Block b2 = w.getBlockAt(b.getLocation().add(ev.getDirection().getDirection()));
        Collection<Entity> items = w.getNearbyEntities(BoundingBox.of(b), (e)-> e instanceof Item);
        items.addAll(w.getNearbyEntities(BoundingBox.of(b2), (e)-> e instanceof Item));
        Item[] itemArr = items.toArray(new Item[0]);
        compact(itemArr,w,b.getLocation());
    }
    public void compact(Item[] itemEntities, World w, Location l){
        if(l.getWorld() == null) l.setWorld(w);
        //Fast access times
        ItemStackMap stackMap = InWorldItemCrafting.inWorldCraftingItems.get(l.getWorld()).get(new SQLVector3i(l.getBlockX(),l.getBlockY(),l.getBlockZ()));
        //Cycle through itementities collected add them to the map.
        for (Item itemEntity : itemEntities) {
            stackMap.add(itemEntity.getItemStack());
            itemEntity.remove();
        }
        List<ItemStack> list = new ArrayList<>();
        for (ItemStack stack : stackMap.values()) {
            CompactorRecipeResult result = getRecipeResult(stack,w);
            if(result != null){
                stackMap.reduceBy(result.toReduce);
                list.add(result.result);
            }
        }
        stackMap.addAll(list);
    }
    public CompactorRecipeResult getRecipeResult(ItemStack item, World w){
        if(item.getAmount() < 4) return null;
        int reduction = 4;
        Recipe recipe = Bukkit.getCraftingRecipe(new ItemStack[]{item,item, new ItemStack(Material.AIR), item, item, new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)},w);
        if(recipe == null && item.getAmount() >= 9) {
            reduction = 9;
            recipe = Bukkit.getCraftingRecipe(new ItemStack[]{item, item, item, item, item, item, item, item, item},w);
        }
        if(recipe == null) return null;
        int multiplier = item.getAmount()/reduction;
        ItemStack res = recipe.getResult();
        res.setAmount(res.getAmount()*multiplier);
        ItemStack toReduce = item.clone();
        toReduce.setAmount(reduction*multiplier);
        return new CompactorRecipeResult(res,toReduce);
    }
}
