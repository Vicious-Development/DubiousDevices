package com.drathonix.dubiousdevices.devices.overworld.autominer;

import com.drathonix.dubiousdevices.DDBlockInstances;
import com.drathonix.dubiousdevices.devices.overworld.machine.MaterialValue;
import com.drathonix.dubiousdevices.util.ProcessTimer;
import com.vicious.viciouslib.database.objectTypes.SQLVector3i;
import com.vicious.viciouslibkit.block.BlockTemplate;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstance;
import com.vicious.viciouslibkit.data.provided.multiblock.MultiBlockInstance;
import com.vicious.viciouslibkit.inventory.optimization.InventorySearcher;
import com.vicious.viciouslibkit.services.multiblock.TickableMultiBlock;
import com.vicious.viciouslibkit.util.ToolUtil;
import com.vicious.viciouslibkit.util.vector.ChunkPos;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.UUID;

public class AutoMiner extends TickableMultiBlock {
    InventorySearcher dispenserSearcher = new InventorySearcher();
    int materialValue;
    private ProcessTimer timer = new ProcessTimer();
    public AutoMiner(Class<? extends MultiBlockInstance> mbType, World w, Location l, BlockFace dir, boolean flipped, boolean upsidedown, UUID id) {
        super(mbType, w, l, dir, flipped, upsidedown,id);
    }

    public AutoMiner(Class<? extends MultiBlockInstance> type, World w, UUID id, ChunkPos cpos) {
        super(type, w, id, cpos);
    }

    @Override
    public void validate() {
        super.validate();
        SQLVector3i v = getRelativePosition(new SQLVector3i(0,0,-1));
        materialValue = MaterialValue.getMaterialValue(world.getBlockAt(v.x,v.y,v.z));
        v = getRelativePosition(new SQLVector3i(0,0,-2));
        materialValue += MaterialValue.getMaterialValue(world.getBlockAt(v.x,v.y,v.z));
    }

    @Override
    public void tick() {
        super.tick();
        if(timer.hasPassedOrReached(20)) {
            Block b = getBlock();
            if (b == null) return;
            else if (!b.isLiquid() || b.isEmpty()) {
                SQLVector3i v = xyz.value();
                BlockState c = world.getBlockAt(v.x, v.y, v.z).getState();
                if (c instanceof Dispenser) {
                    Dispenser d = (Dispenser) c;
                    Inventory inv = d.getInventory();
                    ItemStack breaker = dispenserSearcher.getItemStackThatCanBreak(inv, b);
                    if (breaker == null || !ToolUtil.willNotBreak(breaker)) {
                        b.breakNaturally(new ItemStack(Material.STONE_PICKAXE));
                    } else {
                        b.breakNaturally(breaker);
                        if(breaker.getData() instanceof Damageable) {
                            if(materialValue*Math.random() > 1) {
                                if(breaker.containsEnchantment(Enchantment.DURABILITY)){
                                    double level = 1 + breaker.getEnchantmentLevel(Enchantment.DURABILITY);
                                    if(Math.random() < 1.0/level){
                                        ToolUtil.damage(breaker);
                                    }
                                }
                                else ToolUtil.damage(breaker);
                            }
                        }
                    }
                    timer.reset();
                } else throw new IllegalStateException("Expected to find a dispenser at: " + v + " but did not find one");
            }
        }
        timer.tick();
    }
    public Block getBlock(){
        SQLVector3i v = getRelativePosition(new SQLVector3i(0,0,1));
        return world.getBlockAt(v.x,v.y,v.z);
    }

    @Override
    public boolean tickOnInit() {
        return true;
    }
    public static BlockTemplate template(){
        BlockInstance s = DDBlockInstances.ALLDEEPSLATESLABSBOTTOM;
        BlockInstance t = DDBlockInstances.ALLDEEPSLATESTAIRSIGNORE;
        BlockInstance d = DDBlockInstances.ALLDEEPSLATE;
        BlockInstance n = null;
        BlockInstance e = new BlockInstance(Material.DISPENSER).facing(BlockFace.SOUTH);
        return BlockTemplate.start()
                .x(s,t,s).z()
                .x(3,d).z()
                .x(s,t,s).y()
                .x(n,n,n).z()
                .x(n,n,n).z()
                .x(n,e,n).finish(1,1,2,true);
    }
}
