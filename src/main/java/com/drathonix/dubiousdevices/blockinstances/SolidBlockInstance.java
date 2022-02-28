package com.drathonix.dubiousdevices.blockinstances;

import com.vicious.viciouslibkit.block.BlockInstance;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class SolidBlockInstance extends SpecialBlockInstance {
    public SolidBlockInstance() {
        super(Material.OBSIDIAN);
    }

    @Override
    public boolean matches(Block in) {
        return !in.isEmpty() && !in.isPassable() && !in.isLiquid();
    }

    /**
     * Only called when block is broken, return false.
     * @param in
     * @return
     */
    @Override
    public boolean matches(BlockInstance in) {
        return false;
    }

    @Override
    public String verboseInfo() {
        return "ANY SOLID BLOCK";
    }

    @Override
    public String toString() {
        return "ANY SOLID BLOCK";
    }
}
