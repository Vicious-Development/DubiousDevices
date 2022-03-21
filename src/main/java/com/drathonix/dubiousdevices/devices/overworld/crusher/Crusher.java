package com.drathonix.dubiousdevices.devices.overworld.crusher;

import com.drathonix.dubiousdevices.DDBlockInstances;
import com.drathonix.dubiousdevices.DubiousCFG;
import com.drathonix.dubiousdevices.DubiousDevices;
import com.drathonix.dubiousdevices.devices.overworld.machine.DeviceItemIO;
import com.drathonix.dubiousdevices.devices.overworld.machine.IOTypes;
import com.drathonix.dubiousdevices.devices.overworld.machine.MaterialValue;
import com.drathonix.dubiousdevices.recipe.RecipeHandler;
import com.drathonix.dubiousdevices.registry.RecipeHandlers;
import com.vicious.viciouslib.database.objectTypes.SQLVector3i;
import com.vicious.viciouslibkit.block.BlockTemplate;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstance;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstanceSolid;
import com.vicious.viciouslibkit.data.provided.multiblock.MultiBlockInstance;
import com.vicious.viciouslibkit.util.ChunkPos;
import com.vicious.viciouslibkit.util.LibKitUtil;
import com.vicious.viciouslibkit.util.NMSHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Piston;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;

/*
TODO: Create a generic machine multiblock.
 */
public class Crusher extends DeviceItemIO<CrusherRecipe> {
    private int maxExtraDrops = 0;
    //Northfacing
    private static final SQLVector3i iol = new SQLVector3i(-2,-3,0);
    private SQLVector3i io1;
    private SQLVector3i io2;

    public Crusher(Class<? extends MultiBlockInstance> mbType, World w, Location l, BlockFace dir, boolean flipped, UUID id) {
        super(mbType, w, l, dir, flipped, id);
    }

    public Crusher(Class<? extends MultiBlockInstance> type, World w, UUID id, ChunkPos cpos) {
        super(type, w, id, cpos);
    }

    @Override
    protected RecipeHandler<CrusherRecipe> getRecipeHandler() {
        return RecipeHandlers.CRUSHER;
    }


    @Override
    public void validate() {
        super.validate();
        if(!DubiousCFG.getInstance().crusherEnabled.value()) return;
        io1 = LibKitUtil.orientate(iol,facing.value(),flipped.value());
        io2 = LibKitUtil.orientate(iol,facing.value(),!flipped.value());
        io1 = new SQLVector3i(xyz.value().x + io1.x,xyz.value().y + io1.y,xyz.value().z + io1.z);
        io2 = new SQLVector3i(xyz.value().x + io2.x,xyz.value().y + io2.y,xyz.value().z + io2.z);
        Block b = world.getBlockAt(xyz.value().x,xyz.value().y-1,xyz.value().z);
        maxExtraDrops = MaterialValue.getMaterialValue(b.getType());
        //Exception is caused by unloaded chunks, just ignore it it'll be fine.
        Bukkit.getScheduler().scheduleSyncDelayedTask(DubiousDevices.INSTANCE, this::initInputInvs,1);
    }

    protected void processStart() throws Exception{
        super.processStart();
        SQLVector3i vec = xyz.value();
        Block piston = world.getBlockAt(vec.x, vec.y, vec.z);
        Piston pdat = (Piston) piston.getBlockData();
        NMSHelper.setExtended(piston,true);
        pdat.setExtended(true);
        piston.setBlockData(pdat);
        input();
    }
    protected void processEnd() throws Exception{
        if(output()){
            super.processEnd();
            SQLVector3i vec = xyz.value();
            Block piston = world.getBlockAt(vec.x,vec.y,vec.z);
            Piston pdat = (Piston) piston.getBlockData();
            pdat.setExtended(false);
            piston.setBlockData(pdat);
        }
    }

    protected void applyOutputEffects() {
        if(recipe.doExtraOutputs){
            storedItemOutputs = recipe.cloneOutputs();
            if(maxExtraDrops > 0) {
                for (int i = 0; i < storedItemOutputs.size(); i++) {
                    ItemStack s = storedItemOutputs.get(i);
                    int amount = DubiousDevices.random.nextInt(maxExtraDrops+1);
                    if(amount != 0){
                        ItemStack s2 = s.clone();
                        s2.setAmount(amount);
                        storedItemOutputs.add(0,s2);
                        i++;
                    }
                }
            }
        }
        else storedItemOutputs = recipe.cloneOutputs();
    }

    @Override
    public boolean tickOnInit() {
        return DubiousCFG.getInstance().crusherEnabled.value();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return ((Crusher) o).ID.equals(this.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
    public void initInputInvs(){
        if(inputs.isEmpty()){
            Block b1 = world.getBlockAt(io1.x,io1.y,io1.z);
            if(IOTypes.isInput(b1.getType())){
                addIfNonNull(inputs,getAndListenToInventory(io1));
                addIfNonNull(outputs,getAndListenToInventory(io2));
            }
            else {
                addIfNonNull(inputs,getAndListenToInventory(io2));
                addIfNonNull(outputs,getAndListenToInventory(io1));
            }
        }
        else{
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i,getInventory(inputs.get(i).getLocation()));
            }
        }
    }

    public void initOutputInvs() {
        if(outputs.isEmpty()){
            initInputInvs();
        }
        else{
            for (int i = 0; i < outputs.size(); i++) {
                outputs.set(i,getInventory(outputs.get(i).getLocation()));
            }
        }
    }

    public static BlockTemplate template(){
        BlockInstance n = null;
        BlockInstance a = BlockInstance.AIR;
        BlockInstance b = DDBlockInstances.ALLDEEPSLATE;
        BlockInstance c = DDBlockInstances.ALLDEEPSLATESLABSBOTTOM;
        BlockInstance d = DDBlockInstances.ALLDEEPSLATESLABSTOP;
        BlockInstance i = DDBlockInstances.IOBLOCKS;
        BlockInstance f = DDBlockInstances.ALLCOPPERBLOCKS;
        BlockInstance p = new BlockInstance(Material.STICKY_PISTON).facing(BlockFace.DOWN);
        BlockInstanceSolid k = new BlockInstanceSolid();
        return BlockTemplate.start()
                .x(n,b,c,b,n).z()
                .x(i,b,b,b,i).z()
                .x(n,b,c,b,n).y()

                .x(n,b,c,b,n).z()
                .x(n,a,a,a,n).z()
                .x(n,b,c,b,n).y()

                .x(n,b,d,b,n).z()
                .x(n,d,k,d,n).z()
                .x(n,b,d,b,n).y()

                .x(n,c,d,c,n).z()
                .x(n,c,p,c,n).z()
                .x(n,c,d,c,n).y()

                .x(n,n,n,n,n).z()
                .x(n,n,f,n,n).z()
                .x(n,n,n,n,n).finish(2,3,1)
                ;
    }
}
