package com.drathonix.dubiousdevices;

import com.drathonix.dubiousdevices.inventory.InventoryEvents;
import com.drathonix.dubiousdevices.inventory.InventoryWrapperChunkHandler;
import com.drathonix.dubiousdevices.registry.MultiblockRegistry;
import com.drathonix.dubiousdevices.registry.RecipeHandlers;
import com.drathonix.dubiousdevices.util.DDCommands;
import com.drathonix.dubiousdevices.util.DubiousDirectories;
import com.drathonix.dubiousdevices.util.NMSHelper;
import com.vicious.viciouslib.util.reflect.deep.DeepReflection;
import com.vicious.viciouslibkit.data.worldstorage.PluginChunkData;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URL;
import java.util.Enumeration;
import java.util.Random;
import java.util.logging.Logger;

public final class DubiousDevices extends JavaPlugin {
    public static DubiousDevices INSTANCE;
    public static Random random = new Random();
    public static String VLKVer = "1.2.3";
    public static Logger LOGGER;
    public static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    //The location of paper.jar Used to scan all classes included within.
    public static Enumeration<URL> jarURL;

    public static void warn(String s) {
        LOGGER.warning(s);
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        LOGGER = this.getLogger();
        try {
            DubiousDirectories.initializePluginDependents();
            try {
                jarURL = classLoader.getResources("net/minecraft");
                DeepReflection.mapClasses(jarURL, classLoader);
            } catch (Exception e) {
                LOGGER.severe("Dubious Devices failed to load. Possibly due to a bad ViciousLibKit version. Dubious Devices requires minimum VLK " + VLKVer);
                e.printStackTrace();
                return;
            }
            new MultiblockRegistry();
            new NMSHelper();
            //getServer().getPluginManager().registerEvents(new Compactor(), this);
            getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
            // Plugin startup logic
            PluginChunkData.registerDataType(InventoryWrapperChunkHandler.class, InventoryWrapperChunkHandler::new);
            new RecipeHandlers();
            this.getCommand("devicerecipe").setExecutor(DDCommands::recipeCMD);
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
