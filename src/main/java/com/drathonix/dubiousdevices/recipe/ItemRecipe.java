package com.drathonix.dubiousdevices.recipe;

import com.drathonix.dubiousdevices.devices.overworld.crusher.CrusherRecipe;
import com.vicious.viciouslibkit.util.map.ItemStackMap;
import com.vicious.viciouslibkit.util.map.RoughItemStackMap;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class ItemRecipe<T extends ItemRecipe<T>> implements ISerializableRecipe<CrusherRecipe> {
    protected final List<ItemStack> inputs;
    protected final List<ItemStack> outputs;
    protected boolean ignoreNBT = false;
    public ItemRecipe(List<ItemStack> inputs, List<ItemStack> outputs) {
        this.inputs=inputs;
        this.outputs=outputs;
    }
    public ItemRecipe(List<ItemStack> inputs, List<ItemStack> outputs, boolean ignoreNBT) {
        this.inputs=inputs;
        this.outputs=outputs;
        this.ignoreNBT=ignoreNBT;
    }
    public ItemRecipe(List<ItemStack> inputs, List<ItemStack> outputs, List<String> flags){
        this.inputs=inputs;
        this.outputs=outputs;
        this.ignoreNBT=flags.contains(DDRecipeFlags.NONBT.name);
    }
    public boolean matches(List<ItemStack> inputs){
        return ignoreNBT ? matches(new RoughItemStackMap().addAll(inputs)) : matches(new ItemStackMap().addAll(inputs));
    }
    public List<String> rFlags(){
        List<String> flags = new ArrayList<>();
        if(ignoreNBT) flags.add(DDRecipeFlags.NONBT.name);
        return flags;
    }

    /**
     * Recommended to use over ItemStack lists as this speeds up look up times.
     * @param inputs
     * @return
     */
    public boolean matches(ItemStackMap inputs){
        if(ignoreNBT) return matches(new RoughItemStackMap().addAll(inputs.values()));
        return inputs.hasAll(this.inputs);
    }
    public boolean matches(RoughItemStackMap inputs){
        //Cannot be converted back to FineItemStackMap (ItemStackMap).
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
    public boolean ignoresNBT(){
        return ignoreNBT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRecipe<?> that = (ItemRecipe<?>) o;
        return Objects.equals(inputs, that.inputs) && Objects.equals(outputs, that.outputs);
    }
    @Override
    public String serialize() {
        return RecipeParseResult.serialize(inputs,outputs,rFlags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputs, outputs);
    }

    @Override
    public String toString() {
        return "ItemRecipe{" +
                "inputs=" + inputs +
                ", outputs=" + outputs +
                '}';
    }
}
