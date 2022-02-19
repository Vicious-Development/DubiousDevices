package com.drathonix.dubiousdevices.devices;

import com.vicious.viciouslibkit.block.BlockInstance;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Slab;

public class SpecialBlockInstance extends BlockInstance {
    public SpecialBlockInstance(Material material) {
        super(material);
    }

    public SpecialBlockInstance(Block b) {
        super(b);
    }
    public BlockInstance facing(BlockFace face) {
        return this;
    }

    public BlockInstance vOrientation(Bisected.Half half) {
        return this;
    }

    public BlockInstance waterLogged(boolean isWaterLogged) {
        return this;
    }

    public BlockInstance slabType(Slab.Type type) {
        return this;
    }

    public BlockInstance rotateCounterClockwise() {
        return this;
    }

    public BlockInstance rotateClockwise() {
        return this;
    }

    public BlockInstance orientation(Axis axis) {
        return this;
    }
}
