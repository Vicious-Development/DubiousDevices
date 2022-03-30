package com.drathonix.dubiousdevices.registry;

import com.drathonix.dubiousdevices.devices.overworld.compacter.Compactor;
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
        MultiBlockService.registerClickListener(Material.GLASS,HeavyFurnace.class);
        MultiBlockService.registerClickListener(Material.WHITE_STAINED_GLASS,HeavyFurnace.class);
        MultiBlockService.registerClickListener(Material.GRAY_STAINED_GLASS,HeavyFurnace.class);
        MultiBlockService.registerClickListener(Material.LIGHT_GRAY_STAINED_GLASS,HeavyFurnace.class);
        MultiBlockService.registerClickListener(Material.BLACK_STAINED_GLASS,HeavyFurnace.class);
        MultiBlockService.registerClickListener(Material.BLUE_STAINED_GLASS,HeavyFurnace.class);
        MultiBlockService.registerClickListener(Material.LIGHT_BLUE_STAINED_GLASS,HeavyFurnace.class);
        MultiBlockService.registerClickListener(Material.CYAN_STAINED_GLASS,HeavyFurnace.class);
        MultiBlockService.registerClickListener(Material.MAGENTA_STAINED_GLASS,HeavyFurnace.class);
        MultiBlockService.registerClickListener(Material.PINK_STAINED_GLASS,HeavyFurnace.class);
        MultiBlockService.registerClickListener(Material.PURPLE_STAINED_GLASS,HeavyFurnace.class);
        MultiBlockService.registerClickListener(Material.RED_STAINED_GLASS,HeavyFurnace.class);
        MultiBlockService.registerClickListener(Material.ORANGE_STAINED_GLASS,HeavyFurnace.class);
        MultiBlockService.registerClickListener(Material.YELLOW_STAINED_GLASS,HeavyFurnace.class);
        MultiBlockService.registerClickListener(Material.GREEN_STAINED_GLASS,HeavyFurnace.class);
        MultiBlockService.registerClickListener(Material.LIME_STAINED_GLASS,HeavyFurnace.class);
        MultiBlockService.registerClickListener(Material.BROWN_STAINED_GLASS,HeavyFurnace.class);
        MultiBlockService.registerMultiblock(Compactor.class,"DubiousDevicesCompactor", Compactor.template(),Compactor::new,Compactor::new);
        MultiBlockService.registerClickListener(Material.PISTON,Compactor.class);

    }
}
