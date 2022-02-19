package com.drathonix.dubiousdevices.recipe;

import com.vicious.viciouslibkit.util.map.ItemStackMap;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class ItemRecipe<T extends ItemRecipe<T>>{
    protected final List<ItemStack> inputs;
    protected final List<ItemStack> outputs;
    public ItemRecipe(List<ItemStack> inputs, List<ItemStack> outputs) {
        this.inputs=inputs;
        this.outputs=outputs;
    }
    public boolean matches(List<ItemStack> inputs){
        return matches(new ItemStackMap().addAll(inputs));
    }

    /**
     * Recommended to use over ItemStack lists as this speeds up look up times.
     * @param inputs
     * @return
     */
    public boolean matches(ItemStackMap inputs){
        return inputs.hasAll(this.inputs);
    }

    public int getTotalPossible(ItemStackMap map) {
        int max = Integer.MAX_VALUE;
        for (ItemStack input : inputs) {
            max = Math.min(max,map.get(input).getAmount()/input.getAmount());
        }
        return max;
    }
    public List<ItemStack> getInputs(){
        return inputs;
    }
    public List<ItemStack> getOutputs(){
        return outputs;
    }
    public List<ItemStack> cloneInputs(){
        List<ItemStack> stacks = new ArrayList<>();
        for (ItemStack input : inputs) {
            stacks.add(input.clone());
        }
        return stacks;
    }
    public List<ItemStack> cloneOutputs(){
        List<ItemStack> stacks = new ArrayList<>();
        for (ItemStack output : outputs) {
            stacks.add(output.clone());
        }
        return stacks;
    }

    @Override
    public String toString() {
        return "ItemRecipe{" +
                "inputs=" + inputs +
                ", outputs=" + outputs +
                '}';
    }
}
