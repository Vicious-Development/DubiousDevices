package com.drathonix.dubiousdevices;

import com.drathonix.dubiousdevices.commands.DDCommands;
import com.drathonix.dubiousdevices.registry.MultiblockRegistry;
import com.drathonix.dubiousdevices.registry.RecipeHandlers;
import com.drathonix.dubiousdevices.util.DubiousDirectories;
import com.vicious.viciouslibkit.VersionRange;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.logging.Logger;

public final class DubiousDevices extends JavaPlugin {
    public static DubiousDevices INSTANCE;
    public static Random random = new Random();
    public static Logger LOGGER;
    public static final String VLKVER = "1.3.7-*";


    public static void warn(String s) {
        LOGGER.warning(s);
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        if(!VersionRange.fromString(VLKVER).isWithin(Bukkit.getPluginManager().getPlugin("ViciousLibKit").getDescription().getVersion())){
            System.out.println("Your ViciousLibKit Version is not supported by DubiousDevices. Please use a supported version in range: " + VLKVER);
        }
        LOGGER = this.getLogger();
        try {
            DubiousDirectories.initializePluginDependents();
            new MultiblockRegistry();
            new RecipeHandlers();
            this.getCommand("devicerecipe").setExecutor(DDCommands::recipeCMD);
            this.getCommand("ddwiki").setExecutor(DDCommands::wikiCMD);
            this.getCommand("ddreload").setExecutor(DDCommands::reloadCMD);
            this.getCommand("dditeminfo").setExecutor(DDCommands::itemMetaStringCMD);
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
