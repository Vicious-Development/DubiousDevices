package com.drathonix.dubiousdevices;

import com.drathonix.dubiousdevices.data.InventoryWrapperChunkHandler;
import com.drathonix.dubiousdevices.devices.compactor.Compactor;
import com.drathonix.dubiousdevices.event.InventoryEvents;
import com.drathonix.dubiousdevices.util.NMSHelper;
import com.drathonix.dubiousdevices.registry.MultiblockRegistry;
import com.drathonix.dubiousdevices.util.DubiousDirectories;
import com.vicious.viciouslib.util.reflect.deep.DeepReflection;
import com.vicious.viciouslibkit.data.worldstorage.PluginChunkData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Logger;

public final class DubiousDevices extends JavaPlugin {
    public static DubiousDevices INSTANCE;
    public static Logger LOGGER;
    public static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    //The location of paper.jar Used to scan all classes included within.
    public static Enumeration<URL> jarURL;

    public static void warn(String s) {
        LOGGER.warning(s);
    }

    @Override
    public void onEnable() {
        INSTANCE=this;
        LOGGER =this.getLogger();
        try {
            jarURL = classLoader.getResources("net/minecraft");
            DeepReflection.mapClasses(jarURL,classLoader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new MultiblockRegistry();
        new NMSHelper();
        getServer().getPluginManager().registerEvents(new Compactor(), this);
        getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
        // Plugin startup logic
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this,DubiousDevices::onServerTick,0,1);
        DubiousDirectories.initializePluginDependents();
        PluginChunkData.registerDataType(InventoryWrapperChunkHandler.class, InventoryWrapperChunkHandler::new);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static void onServerTick(){
        Compactor.tick();
    }
}
