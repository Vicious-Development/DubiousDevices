package com.drathonix.dubiousdevices.util;

import com.vicious.viciouslib.util.FileUtil;
import com.vicious.viciouslibkit.util.LibKitDirectories;
import org.bukkit.Bukkit;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DubiousDirectories {
    public static Path pluginsDirectory;
    public static Path pluginConfigDirectory;
    public static Path dubiousConfigPath;
    //Anything stored here will ultimately be deleted.
    public static Path voidDataPath;
    public static void initializePluginDependents(){
        pluginsDirectory = FileUtil.createDirectoryIfDNE(LibKitDirectories.rootDir() + "/plugins");
        pluginConfigDirectory = FileUtil.createDirectoryIfDNE(pluginsDirectory.toAbsolutePath() + "/config");
        dubiousConfigPath = Paths.get(pluginConfigDirectory.toAbsolutePath() + "/dubiousConfigPath.json");
    }
}
