package com.drathonix.dubiousdevices.registry;

import com.drathonix.dubiousdevices.devices.overworld.crusher.Crusher;
import com.vicious.viciouslibkit.services.multiblock.MultiBlockService;
import org.bukkit.Material;

public class MultiblockRegistry {
    static {
        //MultiBlockService.registerMultiblock(Enderizer.class,"DubiousDevicesEnderizer",Enderizer.template(),Enderizer::new,Enderizer::new);
        //MultiBlockService.registerClickListener(Material.PURPUR_PILLAR,Enderizer.class);
        MultiBlockService.registerMultiblock(Crusher.class,"DubiousDevicesCrusher",Crusher.template(),Crusher::new,Crusher::new);
        MultiBlockService.registerClickListener(Material.STICKY_PISTON,Crusher.class);
        //System.out.println(Crusher.template().allOrientations());
    }
}
