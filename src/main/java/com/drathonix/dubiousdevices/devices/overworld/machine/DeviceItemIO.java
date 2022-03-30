package com.drathonix.dubiousdevices.devices.overworld.machine;

import com.drathonix.dubiousdevices.recipe.ItemRecipe;
import com.drathonix.dubiousdevices.recipe.RecipeHandler;
import com.vicious.viciouslib.database.objectTypes.SQLVector3i;
import com.vicious.viciouslibkit.data.provided.multiblock.MultiBlockChunkDataHandler;
import com.vicious.viciouslibkit.data.provided.multiblock.MultiBlockInstance;
import com.vicious.viciouslibkit.data.worldstorage.PluginWorldData;
import com.vicious.viciouslibkit.inventory.InventoryHelper;
import com.vicious.viciouslibkit.inventory.wrapper.EInventoryUpdateStatus;
import com.vicious.viciouslibkit.inventory.wrapper.InventoryWrapper;
import com.vicious.viciouslibkit.inventory.wrapper.InventoryWrapperChunkHandler;
import com.vicious.viciouslibkit.util.ChunkPos;
import com.vicious.viciouslibkit.util.LibKitUtil;
import com.vicious.viciouslibkit.util.interfaces.INotifiable;
import com.vicious.viciouslibkit.util.interfaces.INotifier;
import com.vicious.viciouslibkit.util.map.ItemStackMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class DeviceItemIO<T extends ItemRecipe<T>> extends DeviceMachine implements INotifiable<EInventoryUpdateStatus>, INotifier<MachineStatus> {
    public List<Inventory> inputs = new ArrayList<>();
    public List<Inventory> outputs = new ArrayList<>();
    public T recipe = null;

    public DeviceItemIO(Class<? extends MultiBlockInstance> mbType, World w, Location l, BlockFace dir, boolean flipped, boolean upsidedown, UUID id) {
        super(mbType, w, l, dir, flipped, upsidedown,id);
    }

    public DeviceItemIO(Class<? extends MultiBlockInstance> type, World w, UUID id, ChunkPos cpos) {
        super(type, w, id, cpos);
    }
    @Override
    public void tick() {
        super.tick();
        //Logic. No recipe, check the input inventory. Still no recipe, stop ticking.
        if(!isProcessing){
            initInputInvs();
            if(!inputs.isEmpty()) {
                if (!checkRecipe(mapInventory(inputs))) {
                    removeFromTicker();
                    return;
                }
            }
            else {
                return;
            }
        }
        process();
    }
    protected void IOAutoSetup(List<Inventory> inputs,  List<Inventory> outputs, SQLVector3i... locations){
        for (SQLVector3i location : locations) {
            Block b = world.getBlockAt(location.x,location.y,location.z);
            if(IOTypes.isInput(b.getType())){
                addIfNonNull(inputs,getAndListenToInventory(location));
            }
            else{
                addIfNonNull(outputs,getAndListenToInventory(location));
            }
        }
    }

    protected void resetInputs(){
        for (int i = 0; i < inputs.size(); i++) {
            inputs.set(i,getInventory(inputs.get(i).getLocation()));
        }
    }
    protected void resetOutputs(){
        for (int i = 0; i < outputs.size(); i++) {
            outputs.set(i,getInventory(outputs.get(i).getLocation()));
        }
    }
    public boolean checkRecipe(ItemStackMap inputs){
        if(recipe != null && !recipe.matches(inputs)) recipe = null;
        if (!getRecipe(inputs)) {
            return false;
        } else {
            return true;
        }
    }
    public boolean getRecipe(ItemStackMap inputs){
        recipe = getRecipeHandler().getRecipe(inputs);
        return recipe != null;
    }

    protected abstract RecipeHandler<T> getRecipeHandler();

    public void input(){
        initInputInvs();
        for (ItemStack input : recipe.getInputs()) {
            removeItem(inputs,input,recipe.ignoresNBT());
        }
        storedItemInputs = recipe.cloneInputs();
    }

    /**
     * Call to output a recipe.
     * Override applyOutputEffects() to do special stuff.
     */
    public boolean output(){
        initOutputInvs();
        if(storedItemOutputs.size() == 0){
            storedItemOutputs = recipe.cloneOutputs();
            applyOutputEffects();
        }
        for (Inventory output : outputs) {
            if(storedItemOutputs.size() == 0) break;
            InventoryHelper.moveFrom(output, storedItemOutputs);
        }
        return storedItemOutputs.size() == 0;
    }
    public void stopListeningToInventories(){
        for (Inventory input : inputs) {
            InventoryWrapper iw = getInvWrapper(LibKitUtil.fromLocation(input.getLocation()));
            if(iw != null) iw.stopListening(this);
        }
        for (Inventory outputs : outputs) {
            InventoryWrapper iw = getInvWrapper(LibKitUtil.fromLocation(outputs.getLocation()));
            if(iw != null) iw.stopListening(this);
        }
    }

    @Override
    protected void invalidate(MultiBlockChunkDataHandler dat) {
        stopListeningToInventories();
        super.invalidate(dat);
    }

    public InventoryWrapper getInvWrapper(SQLVector3i p){
        InventoryWrapperChunkHandler handler = PluginWorldData.getChunkDataHandler(world,ChunkPos.fromBlockPos(p), InventoryWrapperChunkHandler.class);
        return handler.getOrCreateWrapper(world,p);
    }

    protected void applyOutputEffects() {}

    /**
     * Override if you need to customize.
     */
    public void initOutputInvs() {
        if(outputs.isEmpty()){
            initInputInvs();
        }
        else{
            resetOutputs();
        }
    }
    public abstract void initInputInvs();

    private final List<INotifiable<MachineStatus>> listening = new ArrayList<>();
    @Override
    public void sendNotification(MachineStatus machineStatus) {
        for (INotifiable<MachineStatus> listener : listening) {
            listener.notify(this,machineStatus);
        }
    }

    @Override
    public void listen(INotifiable<MachineStatus> iNotifiable) {
        listening.add(iNotifiable);
    }

    @Override
    public void stopListening(INotifiable<MachineStatus> iNotifiable) {
        listening.remove(iNotifiable);
    }
    @Override
    public void notify(INotifier<EInventoryUpdateStatus> sender, EInventoryUpdateStatus status) {
        addToTicker();
    }
    protected Inventory getAndListenToInventory(SQLVector3i o){
        InventoryWrapperChunkHandler iwch = PluginWorldData.getChunkDataHandler(world,ChunkPos.fromBlockPos(o),InventoryWrapperChunkHandler.class);
        InventoryWrapper wrapper = iwch.getOrCreateWrapper(world,o);
        if(wrapper == null) return null;
        wrapper.listen(this);
        return wrapper.INVENTORY;
    }
}
