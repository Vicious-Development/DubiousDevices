package com.drathonix.dubiousdevices.devices;

import com.drathonix.dubiousdevices.DubiousDevices;
import com.vicious.viciouslib.database.objectTypes.SQLVector3i;
import com.vicious.viciouslibkit.data.provided.multiblock.MultiBlockInstance;
import com.vicious.viciouslibkit.inventory.InventoryHelper;
import com.vicious.viciouslibkit.services.multiblock.TickableMultiBlock;
import com.vicious.viciouslibkit.util.ChunkPos;
import com.vicious.viciouslibkit.util.map.ItemStackMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class Device extends TickableMultiBlock {
    public Device(Class<? extends MultiBlockInstance> mbType, World w, Location l, BlockFace dir, boolean flipped, UUID id) {
        super(mbType, w, l, dir, flipped, id);
    }

    public Device(Class<? extends MultiBlockInstance> type, World w, UUID id, ChunkPos cpos) {
        super(type, w, id, cpos);
    }

    @Override
    public boolean tickOnInit() {
        return false;
    }
    protected static ItemStackMap mapInventory(List<Inventory> inventories){
        ItemStackMap map = new ItemStackMap();
        for (Inventory inventory : inventories) {
            for (ItemStack stack : inventory.getStorageContents()) {
                if(stack == null) continue;
                map.add(stack);
            }
        }
        return map;
    }
    protected static ItemStackMap mapInventory(Inventory... inventories){
        ItemStackMap map = new ItemStackMap();
        for (Inventory inventory : inventories) {
            for (ItemStack stack : inventory.getStorageContents()) {
                if(stack == null) continue;
                map.add(stack);
            }
        }
        return map;
    }

    protected Inventory getInventory(SQLVector3i p) {
        Block b = world.getBlockAt(p.x,p.y,p.z);
        if(b.getState() instanceof Container){
            return ((Container) b.getState()).getInventory();
        } else return null;
    }
    protected Inventory getInventory(Location l) {
        Block b = world.getBlockAt(l.getBlockX(),l.getBlockY(),l.getBlockZ());
        if(b.getState() instanceof Container){
            return ((Container) b.getState()).getInventory();
        } else return null;
    }
    protected void removeItem(List<Inventory> inputs, ItemStack input, boolean ignoreNBT) {
        input = input.clone();
        int fCount = input.getAmount();
        for (Inventory inv : inputs) {
            input.setAmount(fCount);
            if(input.getAmount() > 0) fCount = InventoryHelper.extract(input,inv,ignoreNBT);
            else return;
        }
        if(fCount > 0) {
            DubiousDevices.LOGGER.severe("DUPLICATION HAS OCCURED!!! PLEASE REPORT TO THE GIT IMMEDIATELY");
            new Exception().printStackTrace();
        }
    }
    public <T> void addIfNonNull(List<T> lst, T obj) {
        if(obj != null) lst.add(obj);
    }
}
