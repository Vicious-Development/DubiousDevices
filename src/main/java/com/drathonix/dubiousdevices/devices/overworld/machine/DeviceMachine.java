package com.drathonix.dubiousdevices.devices.overworld.machine;

import com.drathonix.dubiousdevices.devices.Device;
import com.vicious.viciouslibkit.data.provided.multiblock.MultiBlockInstance;
import com.vicious.viciouslibkit.util.ChunkPos;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A Device that processes items or other inputs.
 */
public abstract class DeviceMachine extends Device {
    protected List<ItemStack> storedItemOutputs = new ArrayList<>();
    protected List<ItemStack> storedItemInputs;
    public int timer = 0;
    public int processTime = 20;

    public DeviceMachine(Class<? extends MultiBlockInstance> mbType, World w, Location l, BlockFace dir, boolean flipped, UUID id) {
        super(mbType, w, l, dir, flipped, id);
    }

    public DeviceMachine(Class<? extends MultiBlockInstance> type, World w, UUID id, ChunkPos cpos) {
        super(type, w, id, cpos);
    }
    protected abstract void process();
    public void postTick(){}
}
