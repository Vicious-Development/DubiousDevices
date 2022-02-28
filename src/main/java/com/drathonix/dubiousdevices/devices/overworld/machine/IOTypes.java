package com.drathonix.dubiousdevices.devices.overworld.machine;

import com.drathonix.dubiousdevices.blockinstances.MaterialOnlyBlockInstance;
import com.vicious.viciouslibkit.block.BlockInstance;
import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;

public class IOTypes {
    public static Map<Material, BlockInstance> inputs = new EnumMap<>(Material.class);
    public static Map<Material, BlockInstance> outputs = new EnumMap<>(Material.class);
    static{
        inputs.put(Material.CHEST, new MaterialOnlyBlockInstance(Material.CHEST));
        inputs.put(Material.BARREL, new MaterialOnlyBlockInstance(Material.BARREL));
        outputs.put(Material.TRAPPED_CHEST, new MaterialOnlyBlockInstance(Material.TRAPPED_CHEST));
        outputs.put(Material.HOPPER, new MaterialOnlyBlockInstance(Material.HOPPER));
        outputs.put(Material.DROPPER, new MaterialOnlyBlockInstance(Material.DROPPER));
        outputs.put(Material.DISPENSER, new MaterialOnlyBlockInstance(Material.DISPENSER));
    }
    public static boolean isOutput(Material in){
        return outputs.containsKey(in);
    }
    public static boolean isInput(Material in){
        return inputs.containsKey(in);
    }

}
