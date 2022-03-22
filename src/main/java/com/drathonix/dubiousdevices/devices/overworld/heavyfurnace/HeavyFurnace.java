package com.drathonix.dubiousdevices.devices.overworld.heavyfurnace;

import com.drathonix.dubiousdevices.DDBlockInstances;
import com.drathonix.dubiousdevices.DubiousDevices;
import com.drathonix.dubiousdevices.devices.overworld.machine.DeviceItemIO;
import com.drathonix.dubiousdevices.devices.overworld.machine.MachineStatus;
import com.drathonix.dubiousdevices.devices.overworld.machine.MaterialValue;
import com.drathonix.dubiousdevices.devices.overworld.redstone.IFurnaceFuel;
import com.drathonix.dubiousdevices.recipe.RecipeHandler;
import com.drathonix.dubiousdevices.registry.RecipeHandlers;
import com.vicious.viciouslib.database.objectTypes.SQLVector3i;
import com.vicious.viciouslibkit.VLKHooks;
import com.vicious.viciouslibkit.block.BlockTemplate;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstance;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstanceMaterialOnly;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstanceSolid;
import com.vicious.viciouslibkit.data.provided.multiblock.MultiBlockInstance;
import com.vicious.viciouslibkit.item.ItemStackHelper;
import com.vicious.viciouslibkit.util.ChunkPos;
import com.vicious.viciouslibkit.util.LibKitUtil;
import com.vicious.viciouslibkit.util.interfaces.INotifier;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HeavyFurnace extends DeviceItemIO<MetalSmeltingRecipe> implements IFurnaceFuel, INotifier<MachineStatus> {
    private List<Inventory> fuelInput = new ArrayList<>();
    private int fuelTicksRemaining = 0;
    private int maxFuelTicks = 0;
    private double chanceToDouble = 0;
    private double fuelBurnTimeMultiplier = 0;
    private ItemStack fuel = null;
    public HeavyFurnace(Class<? extends MultiBlockInstance> mbType, World w, Location l, BlockFace dir, boolean flipped, UUID id) {
        super(mbType, w, l, dir, flipped, id);
    }

    public HeavyFurnace(Class<? extends MultiBlockInstance> type, World w, UUID id, ChunkPos cpos) {
        super(type, w, id, cpos);
    }

    protected void processStart() throws Exception{
        super.processStart();
        input();
    }
    protected void processEnd() throws Exception{
        if(output()) {
            super.processEnd();
        }
    }

    @Override
    public void validate() {
        super.validate();
        //if(!DubiousCFG.getInstance().crusherEnabled.value()) return;
        int materialValue = getTotalMaterialValue();
        double avgMV = ((double) materialValue/getMetalBlockLocations().size());
        //With netherite this is 1.
        chanceToDouble = avgMV/4;
        //With netherite this is 2.
        fuelBurnTimeMultiplier = avgMV/2;
        Bukkit.getScheduler().scheduleSyncDelayedTask(DubiousDevices.INSTANCE, this::initInputInvs,1);
    }
    public int getTotalMaterialValue(){
        int ret = 0;
        SQLVector3i vec = xyz.value();
        for (SQLVector3i l : getMetalBlockLocations()) {
            ret += MaterialValue.getMaterialValue(world.getBlockAt(vec.x + l.x,vec.y + l.y,vec.z + l.z).getType());
        }
        return ret;
    }

    /**
     * This only needs to be used on machine validation so no need to cache.
     */
    public List<SQLVector3i> getMetalBlockLocations(){
        List<SQLVector3i> metalLocations = new ArrayList<>();
        //0
        metalLocations.add(LibKitUtil.orientate(new SQLVector3i(0, -1, -1), facing.value(), flipped.value()));
        //1
        metalLocations.add(LibKitUtil.orientate(new SQLVector3i(-1, 0, -1), facing.value(), flipped.value()));
        metalLocations.add(LibKitUtil.orientate(new SQLVector3i(1, 0, -1), facing.value(), flipped.value()));
        metalLocations.add(LibKitUtil.orientate(new SQLVector3i(0, 0, -2), facing.value(), flipped.value()));
        //2
        metalLocations.add(LibKitUtil.orientate(new SQLVector3i(-1, 1, -1), facing.value(), flipped.value()));
        metalLocations.add(LibKitUtil.orientate(new SQLVector3i(1, 1, -1), facing.value(), flipped.value()));
        metalLocations.add(LibKitUtil.orientate(new SQLVector3i(0, 1, -2), facing.value(), flipped.value()));
        //3
        metalLocations.add(LibKitUtil.orientate(new SQLVector3i(-1, 2, -1), facing.value(), flipped.value()));
        metalLocations.add(LibKitUtil.orientate(new SQLVector3i(1, 2, -1), facing.value(), flipped.value()));
        metalLocations.add(LibKitUtil.orientate(new SQLVector3i(0, 2, -2), facing.value(), flipped.value()));
        //4
        metalLocations.add(LibKitUtil.orientate(new SQLVector3i(-1, 3, -1), facing.value(), flipped.value()));
        metalLocations.add(LibKitUtil.orientate(new SQLVector3i(1, 3, -1), facing.value(), flipped.value()));
        metalLocations.add(LibKitUtil.orientate(new SQLVector3i(0, 3, -2), facing.value(), flipped.value()));
        metalLocations.add(LibKitUtil.orientate(new SQLVector3i(0, 3, 0), facing.value(), flipped.value()));
        //5
        metalLocations.add(LibKitUtil.orientate(new SQLVector3i(-1, 4, -1), facing.value(), flipped.value()));
        metalLocations.add(LibKitUtil.orientate(new SQLVector3i(1, 4, -1), facing.value(), flipped.value()));
        metalLocations.add(LibKitUtil.orientate(new SQLVector3i(0, 4, -2), facing.value(), flipped.value()));
        return metalLocations;
    }

    @Override
    public void tick() {
        if(fuelTicksRemaining > 0){
            fuelTicksRemaining--;
        }
        else{
            if (!consumeFuel()) {
                removeFromTicker();
                timer = 0;
                return;
            }
        }
        super.tick();
    }

    @Override
    protected void process() {
        if(fuelTicksRemaining == 0) {
            if (!consumeFuel()) {
                removeFromTicker();
                timer = 0;
            }
        }
        super.process();
    }

    protected boolean consumeFuel() {
        initFuelInventories();
        for (Inventory inv : fuelInput) {
            for (ItemStack content : inv.getContents()) {
                if(content == null) continue;
                int burntime = (int) (VLKHooks.getBurnTime(content)*(1+fuelBurnTimeMultiplier));
                if(burntime > 0){
                    ItemStackHelper.addTo(content,-1);
                    fuel = content.clone();
                    fuel.setAmount(1);
                    maxFuelTicks = burntime;
                    fuelTicksRemaining = burntime;
                    return true;
                }
            }
        }
        return false;
    }
    protected void applyOutputEffects() {
        if(recipe.doExtraOutputs){
            if(chanceToDouble > 0) {
                for (int i = 0; i < storedItemOutputs.size(); i++) {
                    ItemStack s = storedItemOutputs.get(i);
                    int amount = (int) Math.ceil(chanceToDouble-Math.random()); // either 0 or 1.
                    if(amount != 0){
                        ItemStack s2 = s.clone();
                        s2.setAmount(amount);
                        storedItemOutputs.add(0,s2);
                        i++;
                    }
                }
            }
        }
    }

    @Override
    protected RecipeHandler<MetalSmeltingRecipe> getRecipeHandler() {
        return RecipeHandlers.HEAVYFURNACE;
    }

    @Override
    public void initOutputInvs() {
        if(outputs.isEmpty()){
            SQLVector3i l = LibKitUtil.orientate(new SQLVector3i(0, -1, 1), facing.value(), flipped.value());
            addIfNonNull(outputs,getAndListenToInventory(xyz.value().add(l.x,l.y,l.z)));
            addIfNonNull(outputs,getAndListenToInventory(xyz.value().add(0,l.y,0)));
        }
        else{
            for (int i = 0; i < outputs.size(); i++) {
                outputs.set(i,getInventory(outputs.get(i).getLocation()));
            }
        }
    }

    @Override
    public void initInputInvs() {
        if(inputs.isEmpty()) {
            SQLVector3i inputx1 = LibKitUtil.orientate(new SQLVector3i(-2, 2, -1), facing.value(), flipped.value());
            SQLVector3i inputx2 = LibKitUtil.orientate(new SQLVector3i(2, 2, -1), facing.value(), flipped.value());
            inputx1 = xyz.value().add(inputx1.x, inputx1.y, inputx1.z);
            inputx2 = xyz.value().add(inputx2.x, inputx2.y, inputx2.z);
            addIfNonNull(inputs,getAndListenToInventory(inputx1));
            addIfNonNull(inputs,getAndListenToInventory(inputx2));
            addIfNonNull(inputs,getAndListenToInventory(inputx1.add(0, 1, 0)));
            addIfNonNull(inputs,getAndListenToInventory(inputx2.add(0, 1, 0)));
            initOutputInvs();
            initFuelInventories();
        }
        else{
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i,getInventory(inputs.get(i).getLocation()));
            }
        }
    }
    public void initFuelInventories() {
        if (fuelInput.isEmpty()) {
            SQLVector3i input = LibKitUtil.orientate(new SQLVector3i(0, 1, -3), facing.value(), flipped.value());
            input = xyz.value().add(input.x, input.y, input.z);
            addIfNonNull(fuelInput,(getAndListenToInventory(input)));
            addIfNonNull(fuelInput,getAndListenToInventory(input.add(0, 1, 0)));
        } else {
            for (int i = 0; i < fuelInput.size(); i++) {
                fuelInput.set(i, getInventory(fuelInput.get(i).getLocation()));
            }
        }
    }

    @Override
    public int getFuelRemaining() {
        return fuelTicksRemaining;
    }

    @Override
    public int getFuelMax() {
        return maxFuelTicks;
    }

    @Override
    public ItemStack getFuelType() {
        return fuel;
    }

    @Override
    public boolean tickOnInit() {
        return true;
    }
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
                .x(n,m,a,m,n).z()
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
                .x(n,b,s,b,n).z()
                .x(5,n).z()
                .x(5,n).z()
                .x(5,n)
                .finish(2,1,3)
                ;
    }
}
