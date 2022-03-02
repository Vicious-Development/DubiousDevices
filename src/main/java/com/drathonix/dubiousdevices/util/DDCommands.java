package com.drathonix.dubiousdevices.util;

import com.drathonix.dubiousdevices.guis.RecipeCreation;
import com.drathonix.dubiousdevices.guis.RecipeView;
import com.drathonix.dubiousdevices.inventory.gui.CustomGUIInventory;
import com.drathonix.dubiousdevices.recipe.RecipeHandler;
import com.drathonix.dubiousdevices.registry.RecipeHandlers;
import com.vicious.viciouslib.util.TriConsumer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DDCommands {
    private static Map<String, TriConsumer<Player,RecipeHandler<?>,String[]>> recipeCommands = new HashMap<>();
    static{
        recipeCommands.put("view",(plr,handler,args)->{
            CustomGUIInventory gui = RecipeView.viewAll(args[0],handler,0);
            gui.open(plr);
        });
        recipeCommands.put("add",(plr,handler,args)->{
            CustomGUIInventory gui = RecipeCreation.inputsPage(args[0],handler);
            gui.open(plr);
        });

    }
    private static boolean playerCheck(CommandSender sender, Supplier<Boolean> run){
        if(sender instanceof Player){
            return run.get();
        }
        else {
            sender.sendMessage(ChatColor.RED + "Only players can use this command");
            return false;
        }
    }
    public static boolean recipeCMD(CommandSender sender, Command command, String label, String[] args) {
        return playerCheck(sender,()->{
            Player plr = (Player) sender;
            String device = args[0];
            RecipeHandler<?> handler = RecipeHandlers.handlers.get(device);
            if(handler == null){
                plr.sendMessage(ChatColor.RED + args[1] + " is not a valid recipe handler for /recipe!");
                return false;
            }
            TriConsumer<Player,RecipeHandler<?>,String[]> consumer = recipeCommands.get(args[1]);
            if(consumer == null){
                plr.sendMessage(ChatColor.RED + args[1] + " is not a valid subcommand for /recipe " + device + "!");
                return false;
            }
            return true;
        });
    }
}