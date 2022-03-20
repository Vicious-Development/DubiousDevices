package com.drathonix.dubiousdevices.registry;

import com.drathonix.dubiousdevices.devices.overworld.crusher.Crusher;
import com.drathonix.dubiousdevices.devices.overworld.heavyfurnace.HeavyFurnace;
import com.drathonix.dubiousdevices.devices.overworld.redstone.HeatMeter;
import com.vicious.viciouslibkit.services.multiblock.MultiBlockService;
import org.bukkit.Material;

public class MultiblockRegistry {
    static {
        //MultiBlockService.registerMultiblock(Enderizer.class,"DubiousDevicesEnderizer",Enderizer.template(),Enderizer::new,Enderizer::new);
        //MultiBlockService.registerClickListener(Material.PURPUR_PILLAR,Enderizer.class);
        MultiBlockService.registerMultiblock(Crusher.class,"DubiousDevicesCrusher",Crusher.template(),Crusher::new,Crusher::new);
        MultiBlockService.registerClickListener(Material.STICKY_PISTON,Crusher.class);
        MultiBlockService.registerMultiblock(HeatMeter.class,"DubiousDevicesHeatMeter", HeatMeter.template(),HeatMeter::new,HeatMeter::new);
        MultiBlockService.registerClickListener(Material.COMPARATOR,HeatMeter.class);
        MultiBlockService.registerMultiblock(HeavyFurnace.class,"DubiousDevicesHeavyFurnace", HeavyFurnace.template(),HeavyFurnace::new,HeavyFurnace::new);
        MultiBlockService.registerClickListener(Material.TINTED_GLASS,HeavyFurnace.class);
    }
}
