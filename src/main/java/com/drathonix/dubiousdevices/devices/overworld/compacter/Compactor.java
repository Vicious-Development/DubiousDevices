package com.drathonix.dubiousdevices.devices.overworld.compacter;

import com.drathonix.dubiousdevices.DDBlockInstances;
import com.drathonix.dubiousdevices.devices.overworld.machine.DeviceItemIO;
import com.drathonix.dubiousdevices.recipe.RecipeHandler;
import com.vicious.viciouslibkit.block.BlockTemplate;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstance;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstanceSolid;
import com.vicious.viciouslibkit.data.provided.multiblock.MultiBlockInstance;
import com.vicious.viciouslibkit.util.ChunkPos;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

import java.util.UUID;

public class Compactor extends DeviceItemIO<CompactorRecipe> {
    public Compactor(Class<? extends MultiBlockInstance> mbType, World w, Location l, BlockFace dir, boolean flipped, UUID id) {
        super(mbType, w, l, dir, flipped, id);
    }

    public Compactor(Class<? extends MultiBlockInstance> type, World w, UUID id, ChunkPos cpos) {
        super(type, w, id, cpos);
    }

    @Override
    protected RecipeHandler<CompactorRecipe> getRecipeHandler() {
        return null;
    }

    @Override
    public void initOutputInvs() {

    }

    @Override
    public void initInputInvs() {

    }

    @Override
    protected void process() {

    }
    public static BlockTemplate template(){
        BlockInstance n = null;
        BlockInstance a = BlockInstance.AIR;
        BlockInstance b = DDBlockInstances.ALLDEEPSLATE;
        BlockInstance s = DDBlockInstances.ALLDEEPSLATESTAIRSIGNORE;
        BlockInstance c = DDBlockInstances.ALLDEEPSLATESLABSBOTTOM;
        BlockInstance d = DDBlockInstances.ALLDEEPSLATESLABSTOP;
        BlockInstance i = DDBlockInstances.IOBLOCKS;
        BlockInstance f = DDBlockInstances.ALLCOPPERBLOCKS;
        BlockInstance p = new BlockInstance(Material.PISTON).facing(BlockFace.DOWN);
        BlockInstanceSolid k = new BlockInstanceSolid();

        return BlockTemplate.start()
                .x(n,b,b,b,n).z()
                .x(i,b,k,b,i).y()

                .x(n,s,b,s,n).z()
                .x(n,a,a,a,n).y()

                .x(n,n,b,n,n).z()
                .x(n,n,p,n,n).y()

                .x(n,n,s,n,n).z()
                .x(n,n,b,n,n).y()
                .finish(2,2,1)
                ;
    }
}
