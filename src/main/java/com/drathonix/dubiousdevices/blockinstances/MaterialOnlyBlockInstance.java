package com.drathonix.dubiousdevices.blockinstances;

import com.vicious.viciouslibkit.block.BlockInstance;
import org.bukkit.Material;
import org.bukkit.block.Block;

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

    @Override
    public String verboseInfo() {
        return "A " + material + " block";
    }
}
