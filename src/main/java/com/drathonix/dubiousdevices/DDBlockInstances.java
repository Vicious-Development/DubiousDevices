package com.drathonix.dubiousdevices;

import com.vicious.viciouslibkit.block.BlockTemplate;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstance;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstanceMaterialOnly;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstanceMultiple;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstanceSolid;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Slab;

public class DDBlockInstances {
    public static BlockInstance ALLDEEPSLATE = new BlockInstanceMultiple()
            .add(new BlockInstanceMaterialOnly(Material.DEEPSLATE))
            .add(new BlockInstanceMaterialOnly(Material.DEEPSLATE_BRICKS))
            .add(new BlockInstanceMaterialOnly(Material.DEEPSLATE_TILES))
            .add(new BlockInstanceMaterialOnly(Material.COBBLED_DEEPSLATE))
            .add(new BlockInstanceMaterialOnly(Material.CHISELED_DEEPSLATE))
            .add(new BlockInstanceMaterialOnly(Material.POLISHED_DEEPSLATE))
            .add(new BlockInstanceMaterialOnly(Material.CRACKED_DEEPSLATE_BRICKS))
            .add(new BlockInstanceMaterialOnly(Material.CRACKED_DEEPSLATE_TILES));
    public static BlockInstance ALLDEEPSLATESLABSBOTTOM = new BlockInstanceMultiple()
            .add(new BlockInstance(Material.DEEPSLATE_BRICK_SLAB).slabType(Slab.Type.BOTTOM))
            .add(new BlockInstance(Material.DEEPSLATE_TILE_SLAB).slabType(Slab.Type.BOTTOM))
            .add(new BlockInstance(Material.POLISHED_DEEPSLATE_SLAB).slabType(Slab.Type.BOTTOM))
            .add(new BlockInstance(Material.COBBLED_DEEPSLATE_SLAB).slabType(Slab.Type.BOTTOM))
            ;
    public static BlockInstance ALLDEEPSLATESLABSTOP = new BlockInstanceMultiple()
            .add(new BlockInstance(Material.DEEPSLATE_BRICK_SLAB).slabType(Slab.Type.TOP))
            .add(new BlockInstance(Material.DEEPSLATE_TILE_SLAB).slabType(Slab.Type.TOP))
            .add(new BlockInstance(Material.POLISHED_DEEPSLATE_SLAB).slabType(Slab.Type.TOP))
            .add(new BlockInstance(Material.COBBLED_DEEPSLATE_SLAB).slabType(Slab.Type.TOP))
            ;
    public static BlockInstance ALLDEEPSLATESTAIRSIGNORE = new BlockInstanceMultiple()
            .add(new BlockInstanceMaterialOnly(Material.DEEPSLATE_BRICK_STAIRS))
            .add(new BlockInstanceMaterialOnly(Material.DEEPSLATE_TILE_STAIRS))
            .add(new BlockInstanceMaterialOnly(Material.POLISHED_DEEPSLATE_STAIRS))
            .add(new BlockInstanceMaterialOnly(Material.COBBLED_DEEPSLATE_STAIRS))
            ;
    public static BlockInstance IOBLOCKS = new BlockInstanceMultiple()
            .add(new BlockInstance(Material.HOPPER))
            .add(new BlockInstance(Material.TRAPPED_CHEST))
            .add(new BlockInstance(Material.BARREL))
            .add(new BlockInstance(Material.CHEST))
            .add(new BlockInstance(Material.DISPENSER))
            .add(new BlockInstance(Material.DROPPER));
    public static BlockInstance ALLCOPPERBLOCKS = new BlockInstanceMultiple()
            .add(new BlockInstance(Material.COPPER_BLOCK))
            .add(new BlockInstance(Material.WEATHERED_COPPER))
            .add(new BlockInstance(Material.EXPOSED_COPPER))
            .add(new BlockInstance(Material.OXIDIZED_COPPER))
            .add(new BlockInstance(Material.CUT_COPPER))
            .add(new BlockInstance(Material.WEATHERED_CUT_COPPER))
            .add(new BlockInstance(Material.EXPOSED_CUT_COPPER))
            .add(new BlockInstance(Material.OXIDIZED_CUT_COPPER))
            .add(new BlockInstance(Material.WAXED_COPPER_BLOCK))
            .add(new BlockInstance(Material.WAXED_WEATHERED_COPPER))
            .add(new BlockInstance(Material.WAXED_EXPOSED_COPPER))
            .add(new BlockInstance(Material.WAXED_OXIDIZED_COPPER))
            .add(new BlockInstance(Material.WAXED_CUT_COPPER))
            .add(new BlockInstance(Material.WAXED_WEATHERED_CUT_COPPER))
            .add(new BlockInstance(Material.WAXED_EXPOSED_CUT_COPPER))
            .add(new BlockInstance(Material.WAXED_OXIDIZED_CUT_COPPER))
            ;
    public static BlockInstance METALBLOCKS = new BlockInstanceMultiple()
            .add(new BlockInstance(Material.IRON_BLOCK))
            .add(new BlockInstance(Material.GOLD_BLOCK))
            .add(new BlockInstance(Material.NETHERITE_BLOCK))
            .add(new BlockInstance(Material.COPPER_BLOCK))
            .add(new BlockInstance(Material.WEATHERED_COPPER))
            .add(new BlockInstance(Material.EXPOSED_COPPER))
            .add(new BlockInstance(Material.OXIDIZED_COPPER))
            .add(new BlockInstance(Material.CUT_COPPER))
            .add(new BlockInstance(Material.WEATHERED_CUT_COPPER))
            .add(new BlockInstance(Material.EXPOSED_CUT_COPPER))
            .add(new BlockInstance(Material.OXIDIZED_CUT_COPPER))
            .add(new BlockInstance(Material.WAXED_COPPER_BLOCK))
            .add(new BlockInstance(Material.WAXED_WEATHERED_COPPER))
            .add(new BlockInstance(Material.WAXED_EXPOSED_COPPER))
            .add(new BlockInstance(Material.WAXED_OXIDIZED_COPPER))
            .add(new BlockInstance(Material.WAXED_CUT_COPPER))
            .add(new BlockInstance(Material.WAXED_WEATHERED_CUT_COPPER))
            .add(new BlockInstance(Material.WAXED_EXPOSED_CUT_COPPER))
            .add(new BlockInstance(Material.WAXED_OXIDIZED_CUT_COPPER))
            ;
}
