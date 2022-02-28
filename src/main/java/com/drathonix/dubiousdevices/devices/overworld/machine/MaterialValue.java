package com.drathonix.dubiousdevices.devices.overworld.machine;

import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;

public class MaterialValue {
    private static final Map<Material,Integer> map = new EnumMap<>(Material.class);
    static{
        map.put(Material.IRON_BLOCK, 2);
        map.put(Material.GOLD_BLOCK, 2);
        map.put(Material.DIAMOND_BLOCK, 3);
        map.put(Material.EMERALD_BLOCK, 3);
        map.put(Material.NETHERITE_BLOCK, 4);
        map.put(Material.BEACON, 4);
    }
    public static int getMaterialValue(Material mat){
        Integer ret = map.get(mat);
        return ret == null ? (int)(Math.log10(mat.getHardness())*2) : ret;
    }
}

