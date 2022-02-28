package com.drathonix.dubiousdevices.recipe;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RecipeParseResult {
    public List<ItemStack> inputs;
    public List<ItemStack> outputs;
    public List<String> flags;
    public RecipeParseResult(List<ItemStack> inputs, List<ItemStack> outputs, List<String> flags){
        this.inputs=inputs;
        this.outputs=outputs;
        this.flags=flags;
    }

    public static String serialize(List<ItemStack> inputs, List<ItemStack> outputs, List<String> flags) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < inputs.size(); i++) {
            buildIngredient(inputs.get(i),ret);
            if(i != inputs.size()-1){
                ret.append(',');
            }
        }
        ret.append('~');
        for (int i = 0; i < outputs.size(); i++) {
            buildIngredient(outputs.get(i),ret);
            if(i != outputs.size()-1){
                ret.append(',');
            }
        }
        if(flags.size() > 0){
            ret.append('~');
            for (int i = 0; i < flags.size(); i++) {
                String flag = flags.get(i);
                ret.append(flag);
                if(i != flags.size()-1){
                    ret.append(',');
                }
            }
        }
        ret.append(';');
        return ret.toString();
    }
    public static void buildIngredient(ItemStack stack, StringBuilder ret){
        ret.append('<').append(stack.getType().name());
        if(stack.getAmount() > 1) ret.append('*').append(stack.getAmount());
        ret.append('>');
    }

    @Override
    public String toString() {
        return "RecipeParseResult{" +
                "inputs=" + inputs +
                ", outputs=" + outputs +
                ", flags=" + flags +
                '}';
    }
}
