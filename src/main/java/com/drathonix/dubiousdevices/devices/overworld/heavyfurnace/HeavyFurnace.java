package com.drathonix.dubiousdevices.devices.overworld.heavyfurnace;

import com.drathonix.dubiousdevices.DDBlockInstances;
import com.drathonix.dubiousdevices.devices.overworld.machine.MachineStatus;
import com.drathonix.dubiousdevices.devices.overworld.redstone.HeatMeter;
import com.drathonix.dubiousdevices.devices.overworld.redstone.IFurnaceFuel;
import com.vicious.viciouslibkit.block.BlockTemplate;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstance;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstanceMaterialOnly;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstanceSolid;
import com.vicious.viciouslibkit.data.provided.multiblock.MultiBlockInstance;
import com.vicious.viciouslibkit.services.multiblock.TickableMultiBlock;
import com.vicious.viciouslibkit.util.ChunkPos;
import com.vicious.viciouslibkit.util.interfaces.INotifiable;
import com.vicious.viciouslibkit.util.interfaces.INotifier;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HeavyFurnace extends TickableMultiBlock implements IFurnaceFuel, INotifier<MachineStatus> {
    private List<INotifiable<MachineStatus>> connectedHeatMeters = new ArrayList<>();
    private int fuelTicksRemaining = 0;
    private int maxFuelTicks = 0;
    private ItemStack fuel = null;
    public HeavyFurnace(Class<? extends MultiBlockInstance> mbType, World w, Location l, BlockFace dir, boolean flipped, UUID id) {
        super(mbType, w, l, dir, flipped, id);
    }

    public HeavyFurnace(Class<? extends MultiBlockInstance> type, World w, UUID id, ChunkPos cpos) {
        super(type, w, id, cpos);
    }
    @Override
    public int getFuelRemaining() {
        return fuelTicksRemaining;
    }

    @Override
    public int getFuelMax() {
        return maxFuelTicks;
    }

    @Override
    public ItemStack getFuelType() {
        return fuel;
    }

    @Override
    public boolean tickOnInit() {
        return true;
    }
    @Override
    public void sendNotification(MachineStatus machineStatus) {
        for (INotifiable<MachineStatus> connectedHeatMeter : connectedHeatMeters) {
            connectedHeatMeter.notify(this,machineStatus);
        }
    }

    @Override
    public void listen(INotifiable<MachineStatus> iNotifiable) {
        if(iNotifiable instanceof HeatMeter) connectedHeatMeters.add(iNotifiable);
    }

    @Override
    public void stopListening(INotifiable<MachineStatus> iNotifiable) {
        if(iNotifiable instanceof HeatMeter) connectedHeatMeters.remove(iNotifiable);
    }

    public static BlockTemplate template(){
        BlockInstance n = null;
        BlockInstance a = BlockInstance.AIR;
        BlockInstance b = DDBlockInstances.ALLDEEPSLATE;
        BlockInstance c = DDBlockInstances.ALLDEEPSLATESLABSBOTTOM;
        BlockInstance d = DDBlockInstances.ALLDEEPSLATESLABSTOP;
        BlockInstance i = DDBlockInstances.IOBLOCKS;
        BlockInstance m = DDBlockInstances.METALBLOCKS;
        BlockInstance t = new BlockInstance(Material.TINTED_GLASS);
        BlockInstance h = new BlockInstanceMaterialOnly(Material.HOPPER);
        BlockInstance s = DDBlockInstances.ALLDEEPSLATESTAIRSIGNORE;
        BlockInstanceSolid k = new BlockInstanceSolid();
        return BlockTemplate.start()
                .x(b,b,b,b,b).z()
                .x(b,b,b,b,b).z()
                .x(b,b,m,b,b).z()
                .x(b,b,h,b,b).z()
                .x(n,s,i,s,n).y()

                .x(b,s,c,s,b).z()
                .x(c,b,m,b,c).z()
                .x(b,m,a,m,b).z()
                .x(c,b,t,b,c).z()
                .x(n,s,a,s,n).y()

                .x(b,a,h,a,b).z()
                .x(n,b,m,b,n).z()
                .x(n,m,a,m,n).z()
                .x(n,b,t,b,n).z()
                .x(n,c,a,c,n).y()

                .x(b,s,i,s,b).z()
                .x(s,b,m,b,s).z()
                .x(h,m,a,m,h).z()
                .x(n,b,t,b,n).z()
                .x(5,n).y()

                .x(s,s,s,s,s).z()
                .x(b,b,m,b,b).z()
                .x(i,m,a,m,i).z()
                .x(n,s,n,s,n).z()
                .x(5,n).y()

                .x(n,s,s,s,n).z()
                .x(s,b,m,b,s).z()
                .x(n,m,a,m,n).z()
                .x(5,n).z()
                .x(5,n).y()

                .x(n,s,s,s,n).z()
                .x(s,b,s,b,s).z()
                .x(5,n).z()
                .x(5,n).z()
                .x(5,n)
                .finish(2,1,3)
                ;
    }
}
