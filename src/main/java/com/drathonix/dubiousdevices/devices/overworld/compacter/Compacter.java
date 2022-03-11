package com.drathonix.dubiousdevices.devices.overworld.compacter;

import com.drathonix.dubiousdevices.DDBlockInstances;
import com.vicious.viciouslibkit.block.BlockTemplate;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstance;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstanceSolid;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Compacter {
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
