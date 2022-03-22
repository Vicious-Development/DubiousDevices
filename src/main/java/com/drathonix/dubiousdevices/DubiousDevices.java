package com.drathonix.dubiousdevices;

import com.drathonix.dubiousdevices.commands.DDCommands;
import com.drathonix.dubiousdevices.registry.MultiblockRegistry;
import com.drathonix.dubiousdevices.registry.RecipeHandlers;
import com.drathonix.dubiousdevices.util.DubiousDirectories;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.logging.Logger;

public final class DubiousDevices extends JavaPlugin {
    public static DubiousDevices INSTANCE;
    public static Random random = new Random();
    public static Logger LOGGER;


    public static void warn(String s) {
        LOGGER.warning(s);
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        LOGGER = this.getLogger();
        try {
            DubiousDirectories.initializePluginDependents();
            new MultiblockRegistry();
            new RecipeHandlers();
            this.getCommand("devicerecipe").setExecutor(DDCommands::recipeCMD);
            this.getCommand("ddwiki").setExecutor(DDCommands::wikiCMD);
            this.getCommand("ddreload").setExecutor(DDCommands::reloadCMD);
        } catch (Exception ex){
            LOGGER.severe("Dubious Devices failed to load properly, caused by: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
