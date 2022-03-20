package com.drathonix.dubiousdevices.devices.overworld.machine;

import com.drathonix.dubiousdevices.recipe.ItemRecipe;
import com.drathonix.dubiousdevices.recipe.RecipeHandler;
import com.vicious.viciouslibkit.data.provided.multiblock.MultiBlockInstance;
import com.vicious.viciouslibkit.inventory.InventoryHelper;
import com.vicious.viciouslibkit.util.ChunkPos;
import com.vicious.viciouslibkit.util.map.ItemStackMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public abstract class DeviceItemIO<T extends ItemRecipe<T>> extends DeviceMachine{
    public Inventory inputs;
    public Inventory outputs;
    public T recipe = null;

    public DeviceItemIO(Class<? extends MultiBlockInstance> mbType, World w, Location l, BlockFace dir, boolean flipped, UUID id) {
        super(mbType, w, l, dir, flipped, id);
    }

    public DeviceItemIO(Class<? extends MultiBlockInstance> type, World w, UUID id, ChunkPos cpos) {
        super(type, w, id, cpos);
    }
    @Override
    public void tick() {
        super.tick();
        //Logic. No recipe, check the input inventory. Still no recipe, stop ticking.
        if(timer == 0){
            initInputInv();
            if(inputs != null) {
                if (!checkRecipe(mapInventory(inputs))) {
                    removeFromTicker();
                    postTick();
                    return;
                }
            }
            else {
                postTick();
                return;
            }
        }
        process();
        postTick();
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
        initInputInv();
        if(!recipe.ignoresNBT()) {
            for (ItemStack input : recipe.getInputs()) {
                inputs.removeItem(input);
            }
        }
        else {
            for (ItemStack input : recipe.getInputs()) {
                extractIgnoreNBT(input,inputs);
            }
        }
        storedItemInputs = recipe.cloneInputs();
    }

    /**
     * Call to output a recipe.
     * Override applyOutputEffects() to do special stuff.
     */
    public boolean output(){
        initOutputInv();
        if(storedItemOutputs.size() == 0){
            storedItemOutputs = recipe.cloneOutputs();
            applyOutputEffects();
        }
        InventoryHelper.moveFrom(outputs, storedItemOutputs);
        return storedItemOutputs.size() == 0;
    }

    protected void applyOutputEffects() {}
    public abstract void initOutputInv();
    public abstract void initInputInv();
}
