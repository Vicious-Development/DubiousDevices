package com.drathonix.dubiousdevices.devices.end.enderizer;

import com.vicious.viciouslibkit.block.BlockTemplate;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstance;
import com.vicious.viciouslibkit.data.provided.multiblock.MultiBlockInstance;
import com.vicious.viciouslibkit.util.ChunkPos;
import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Slab;

import java.util.UUID;

public class Enderizer extends MultiBlockInstance {
    public Enderizer(Class<? extends MultiBlockInstance> mbType, World w, Location l, BlockFace dir, boolean flipped, UUID id) {
        super(mbType, w, l, dir, flipped, id);
    }

    public Enderizer(Class<? extends MultiBlockInstance> type, World w, UUID id, ChunkPos cpos) {
        super(type, w, id, cpos);
    }

    public static BlockTemplate template() {
        BlockInstance e = BlockInstance.ENDSTONE;
        BlockInstance d = BlockInstance.ENDSTONEBRICK;
        BlockInstance n = null;
        BlockInstance a = BlockInstance.AIR;
        BlockInstance o = new BlockInstance(Material.CRYING_OBSIDIAN);
        BlockInstance p = new BlockInstance(Material.PURPUR_PILLAR).orientation(Axis.Y);
        BlockInstance sb = new BlockInstance(Material.PURPUR_SLAB).slabType(Slab.Type.BOTTOM);
        BlockInstance su = new BlockInstance(Material.PURPUR_SLAB).slabType(Slab.Type.TOP);
        BlockInstance sbs = new BlockInstance(Material.PURPUR_STAIRS).vOrientation(Bisected.Half.BOTTOM).facing(BlockFace.SOUTH);
        BlockInstance sbe = new BlockInstance(Material.PURPUR_STAIRS).vOrientation(Bisected.Half.BOTTOM).facing(BlockFace.EAST);
        BlockInstance sbw = new BlockInstance(Material.PURPUR_STAIRS).vOrientation(Bisected.Half.BOTTOM).facing(BlockFace.WEST);
        BlockInstance sbn = new BlockInstance(Material.PURPUR_STAIRS).vOrientation(Bisected.Half.BOTTOM).facing(BlockFace.NORTH);
        BlockInstance sus = new BlockInstance(Material.PURPUR_STAIRS).vOrientation(Bisected.Half.TOP).facing(BlockFace.SOUTH);
        BlockInstance sue = new BlockInstance(Material.PURPUR_STAIRS).vOrientation(Bisected.Half.TOP).facing(BlockFace.EAST);
        BlockInstance suw = new BlockInstance(Material.PURPUR_STAIRS).vOrientation(Bisected.Half.TOP).facing(BlockFace.WEST);
        BlockInstance sun = new BlockInstance(Material.PURPUR_STAIRS).vOrientation(Bisected.Half.TOP).facing(BlockFace.NORTH);
        return BlockTemplate.start()
                .x(n, n, n, n, n, e, e, e, n, n, n, n, n).z()
                .x(n, n, n, e, e, e, e, e, e, e, n, n, n).z()
                .x(n, n, e, e, e, d, d, d, e, e, e, n, n).z()
                .x(n, e, e, d, d, e, e, e, d, d, e, e, n).z()
                .x(n, e, e, d, e, e, e, e, e, d, e, e, n).z()
                .x(e, e, d, e, e, e, e, e, e, e, d, e, e).z()
                .x(e, e, d, e, e, e, e, e, e, e, d, e, e).z()
                .x(e, e, d, e, e, e, e, e, e, e, d, e, e).z()
                .x(n, e, e, d, e, e, e, e, e, d, e, e, n).z()
                .x(n, e, e, d, d, e, e, e, d, d, e, e, n).z()
                .x(n, n, e, e, e, d, d, d, e, e, e, n, n).z()
                .x(n, n, n, e, e, e, e, e, e, e, n, n, n).z()
                .x(n, n, n, n, n, e, e, e, n, n, n, n, n).y()
//2
                .x(n, n, n, n, n, a, a, a, n, n, n, n, n).z()
                .x(n, n, n, a, a, a, p, a, a, a, n, n, n).z()
                .x(n, n, a, a, a, a, a, a, a, a, a, n, n).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(a, a, a, a, a, sb, sbs, sb, a, a, a, a, a).z()
                .x(a, p, a, a, a, sbe, p, sbw, a, a, a, p, a).z()
                .x(a, a, a, a, a, sb, sbn, sb, a, a, a, a, a).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(n, n, a, a, a, a, a, a, a, a, a, n, n).z()
                .x(n, n, n, a, a, a, p, a, a, a, n, n, n).z()
                .x(n, n, n, n, n, a, a, a, n, n, n, n, n).y()
//3
                .x(n, n, n, n, n, a, a, a, n, n, n, n, n).z()
                .x(n, n, n, a, a, a, p, a, a, a, n, n, n).z()
                .x(n, n, a, a, a, a, a, a, a, a, a, n, n).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(a, a, a, a, a, a, a, a, a, a, a, a, a).z()
                .x(a, p, a, a, a, a, a, a, a, a, a, p, a).z()
                .x(a, a, a, a, a, a, a, a, a, a, a, a, a).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(n, n, a, a, a, a, a, a, a, a, a, n, n).z()
                .x(n, n, n, a, a, a, p, a, a, a, n, n, n).z()
                .x(n, n, n, n, n, a, a, a, n, n, n, n, n).y()
//4
                .x(n, n, n, n, n, a, a, a, n, n, n, n, n).z()
                .x(n, n, n, a, a, a, p, a, a, a, n, n, n).z()
                .x(n, n, a, a, a, a, su, a, a, a, a, n, n).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(a, a, a, a, a, a, a, a, a, a, a, a, a).z()
                .x(a, p, su, a, a, a, a, a, a, a, su, p, a).z()
                .x(a, a, a, a, a, a, a, a, a, a, a, a, a).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(n, n, a, a, a, a, su, a, a, a, a, n, n).z()
                .x(n, n, n, a, a, a, p, a, a, a, n, n, n).z()
                .x(n, n, n, n, n, a, a, a, n, n, n, n, n).y()
//5
                .x(n, n, n, n, n, a, a, a, n, n, n, n, n).z()
                .x(n, n, n, a, a, a, p, a, a, a, n, n, n).z()
                .x(n, n, a, a, a, a, sb, a, a, a, a, n, n).z()
                .x(n, a, a, a, a, a, sun, a, a, a, a, a, n).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(a, a, a, a, a, a, a, a, a, a, a, a, a).z()
                .x(a, p, sb, suw, a, a, a, a, a, sue, sb, p, a).z()
                .x(a, a, a, a, a, a, a, a, a, a, a, a, a).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(n, a, a, a, a, a, sus, a, a, a, a, a, n).z()
                .x(n, n, a, a, a, a, sb, a, a, a, a, n, n).z()
                .x(n, n, n, a, a, a, p, a, a, a, n, n, n).z()
                .x(n, n, n, n, n, a, a, a, n, n, n, n, n).y()
//6
                .x(n, n, n, n, n, a, a, a, n, n, n, n, n).z()
                .x(n, n, n, a, a, a, sbs, a, a, a, n, n, n).z()
                .x(n, n, a, a, a, a, sun, a, a, a, a, n, n).z()
                .x(n, a, a, a, a, su, o, su, a, a, a, a, n).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(a, a, a, su, a, a, a, a, a, su, a, a, a).z()
                .x(a, sbe, suw, o, a, a, new BlockInstance(Material.END_ROD).facing(BlockFace.DOWN), a, a, o, sue, sbw, a).z()
                .x(a, a, a, su, a, a, a, a, a, su, a, a, a).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(n, a, a, a, a, su, o, su, a, a, a, a, n).z()
                .x(n, n, a, a, a, a, sus, a, a, a, a, n, n).z()
                .x(n, n, n, a, a, a, sbn, a, a, a, n, n, n).z()
                .x(n, n, n, n, n, a, a, a, n, n, n, n, n).y()

                .x(n, n, n, n, n, a, a, a, n, n, n, n, n).z()
                .x(n, n, n, a, a, a, a, a, a, a, n, n, n).z()
                .x(n, n, a, a, a, a, sbs, a, a, a, a, n, n).z()
                .x(n, a, a, a, a, sb, o, sb, a, a, a, a, n).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(a, a, a, sb, a, a, a, a, a, sb, a, a, a).z()
                .x(a, a, sbe, o, a, a, new BlockInstance(Material.END_ROD).facing(BlockFace.UP), a, a, o, sbw, a, a).z()
                .x(a, a, a, sb, a, a, a, a, a, sb, a, a, a).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(n, a, a, a, a, sb, o, sb, a, a, a, a, n).z()
                .x(n, n, a, a, a, a, sbn, a, a, a, a, n, n).z()
                .x(n, n, n, a, a, a, a, a, a, a, n, n, n).z()
                .x(n, n, n, n, n, a, a, a, n, n, n, n, n).y()

                .x(n, n, n, n, n, a, a, a, n, n, n, n, n).z()
                .x(n, n, n, a, a, a, a, a, a, a, n, n, n).z()
                .x(n, n, a, a, a, a, a, a, a, a, a, n, n).z()
                .x(n, a, a, a, a, a, sb, a, a, a, a, a, n).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(a, a, a, a, a, a, a, a, a, a, a, a, a).z()
                .x(a, a, a, sb, a, a, a, a, a, sb, a, a, a).z()
                .x(a, a, a, a, a, a, a, a, a, a, a, a, a).z()
                .x(n, a, a, a, a, a, a, a, a, a, a, a, n).z()
                .x(n, a, a, a, a, a, sb, a, a, a, a, a, n).z()
                .x(n, n, a, a, a, a, a, a, a, a, a, n, n).z()
                .x(n, n, n, a, a, a, a, a, a, a, n, n, n).z()
                .x(n, n, n, n, n, a, a, a, n, n, n, n, n).y().finish(6, 1, 6);

    }
}
