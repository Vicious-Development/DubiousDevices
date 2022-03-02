package com.drathonix.dubiousdevices.devices.overworld.machine;

import com.vicious.viciouslibkit.block.blockinstance.BlockInstance;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstanceMaterialOnly;
import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;

public class IOTypes {
    public static Map<Material, BlockInstance> inputs = new EnumMap<>(Material.class);
    public static Map<Material, BlockInstance> outputs = new EnumMap<>(Material.class);
    static{
        inputs.put(Material.CHEST, new BlockInstanceMaterialOnly(Material.CHEST));
        inputs.put(Material.BARREL, new BlockInstanceMaterialOnly(Material.BARREL));
        outputs.put(Material.TRAPPED_CHEST, new BlockInstanceMaterialOnly(Material.TRAPPED_CHEST));
        outputs.put(Material.HOPPER, new BlockInstanceMaterialOnly(Material.HOPPER));
        outputs.put(Material.DROPPER, new BlockInstanceMaterialOnly(Material.DROPPER));
        outputs.put(Material.DISPENSER, new BlockInstanceMaterialOnly(Material.DISPENSER));
    }
    public static boolean isOutput(Material in){
        return outputs.containsKey(in);
    }
    public static boolean isInput(Material in){
        return inputs.containsKey(in);
    }

}
