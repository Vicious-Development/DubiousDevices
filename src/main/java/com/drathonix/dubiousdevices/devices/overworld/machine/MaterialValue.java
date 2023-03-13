package com.drathonix.dubiousdevices.devices.overworld.machine;

import org.bukkit.Material;
import org.bukkit.block.Block;

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

        map.put(Material.COPPER_BLOCK,1);
        map.put(Material.WEATHERED_COPPER,1);
        map.put(Material.EXPOSED_COPPER,1);
        map.put(Material.OXIDIZED_COPPER,0);
        map.put(Material.CUT_COPPER,1);
        map.put(Material.WEATHERED_CUT_COPPER,1);
        map.put(Material.EXPOSED_CUT_COPPER,1);
        map.put(Material.OXIDIZED_CUT_COPPER,0);
        map.put(Material.WAXED_COPPER_BLOCK,1);
        map.put(Material.WAXED_WEATHERED_COPPER,1);
        map.put(Material.WAXED_EXPOSED_COPPER,1);
        map.put(Material.WAXED_OXIDIZED_COPPER,0);
        map.put(Material.WAXED_CUT_COPPER,1);
        map.put(Material.WAXED_WEATHERED_CUT_COPPER,1);
        map.put(Material.WAXED_EXPOSED_CUT_COPPER,1);
        map.put(Material.WAXED_OXIDIZED_CUT_COPPER,0);
    }
    public static int getMaterialValue(Material mat){
        Integer ret = map.get(mat);
        return ret == null ? (int)(Math.log10(mat.getHardness())*2) : ret;
    }
    public static int getMaterialValue(Block b){
        Integer ret = map.get(b.getType());
        return ret == null ? (int)(Math.log10(b.getType().getHardness())*2) : ret;
    }
}

