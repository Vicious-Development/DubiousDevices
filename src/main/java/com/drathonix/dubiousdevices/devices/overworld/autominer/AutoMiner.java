package com.drathonix.dubiousdevices.devices.overworld.autominer;

import com.vicious.viciouslibkit.block.BlockTemplate;
import com.vicious.viciouslibkit.data.provided.multiblock.MultiBlockInstance;
import com.vicious.viciouslibkit.services.multiblock.TickableMultiBlock;
import com.vicious.viciouslibkit.util.ChunkPos;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

import java.util.UUID;

public class AutoMiner extends TickableMultiBlock {
    public AutoMiner(Class<? extends MultiBlockInstance> mbType, World w, Location l, BlockFace dir, boolean flipped, boolean upsidedown, UUID id) {
        super(mbType, w, l, dir, flipped, upsidedown,id);
    }

    public AutoMiner(Class<? extends MultiBlockInstance> type, World w, UUID id, ChunkPos cpos) {
        super(type, w, id, cpos);
    }

    @Override
    public void tick() {
        super.tick();

    }

    @Override
    public boolean tickOnInit() {
        return true;
    }
    public static BlockTemplate template(){
        return BlockTemplate.start();
    }
}
