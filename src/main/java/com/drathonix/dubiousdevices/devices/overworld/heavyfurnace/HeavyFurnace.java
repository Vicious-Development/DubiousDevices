package com.drathonix.dubiousdevices.devices.overworld.heavyfurnace;

import com.drathonix.dubiousdevices.DDBlockInstances;
import com.vicious.viciouslibkit.block.BlockTemplate;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstance;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstanceMaterialOnly;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstanceMultiple;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstanceSolid;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Slab;

public class HeavyFurnace {
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
