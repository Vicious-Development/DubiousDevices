package com.drathonix.dubiousdevices.devices.overworld.machine;

import com.drathonix.dubiousdevices.DubiousDevices;
import com.drathonix.dubiousdevices.devices.Device;
import com.drathonix.dubiousdevices.util.ProcessTimer;
import com.vicious.viciouslibkit.data.provided.multiblock.MultiBlockInstance;
import com.vicious.viciouslibkit.util.vector.ChunkPos;
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
    protected ProcessTimer timer = new ProcessTimer();
    public int processTime = 20;
    public int defaultProcessTime;
    protected boolean isProcessing = false;
    public DeviceMachine(Class<? extends MultiBlockInstance> mbType, World w, Location l, BlockFace dir, boolean flipped, boolean upsidedown, UUID id) {
        super(mbType, w, l, dir, flipped, upsidedown,id);
    }

    public DeviceMachine(Class<? extends MultiBlockInstance> type, World w, UUID id, ChunkPos cpos) {
        super(type, w, id, cpos);
    }
    protected void process() throws Exception {
        if(timer.isAt(0) && !isProcessing){
            try {
                processStart();
            } catch (Exception e){
                DubiousDevices.LOGGER.warning("Caught exception on processing start: " + e.getMessage());
                e.printStackTrace();
            }
        }
        if(timer.hasPassedOrReached(processTime) && isProcessing){
            try{
                processEnd();
            } catch(Exception e){
                DubiousDevices.LOGGER.warning("Caught exception on processing end: " + e.getMessage());
                e.printStackTrace();
            }
        }
        else timer.tick();
    }

    /**
     * Called when the machine starts processing. (when timer == 0 and !isProcessing)
     */
    protected void processStart() throws Exception{
        isProcessing = true;
    }

    /**
     * Called when timer == processTime.
     */
    protected void processEnd() throws Exception{
        isProcessing = false;
        timer.reset();
    }
}
