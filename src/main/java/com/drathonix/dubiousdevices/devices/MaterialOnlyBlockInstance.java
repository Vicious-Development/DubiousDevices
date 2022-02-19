package com.drathonix.dubiousdevices.devices;

import com.vicious.viciouslibkit.block.BlockInstance;
import com.vicious.viciouslibkit.util.FacingUtil;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.Slab;

public class MaterialOnlyBlockInstance extends SpecialBlockInstance {

    public MaterialOnlyBlockInstance(Material material) {
        super(material);
    }

    @Override
    public boolean matches(Block in) {
        return in.getType() == material;
    }

    @Override
    public boolean matches(BlockInstance in) {
        return in.material == material;
    }

    @Override
    public String toString() {
        return "MO-" + super.toString();
    }
}
