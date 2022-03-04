package com.drathonix.dubiousdevices.util;

import com.vicious.viciouslib.util.FileUtil;
import com.vicious.viciouslibkit.util.LibKitDirectories;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DubiousDirectories {
    public static Path pluginsDirectory;
    public static Path pluginConfigDirectory;
    public static Path dubiousConfigPath;
    public static Path recipes;
    public static Path ddrecipes;
    //Anything stored here will ultimately be deleted.
    public static Path voidDataPath;
    public static void initializePluginDependents(){
        pluginsDirectory = FileUtil.createDirectoryIfDNE(LibKitDirectories.rootDir() + "/plugins");
        pluginConfigDirectory = FileUtil.createDirectoryIfDNE(pluginsDirectory.toAbsolutePath() + "/config");
        dubiousConfigPath = Paths.get(pluginConfigDirectory.toAbsolutePath() + "/dubiousdevices.json");
        recipes = FileUtil.createDirectoryIfDNE(pluginsDirectory.toAbsolutePath() + "/" + "recipes");
        ddrecipes = FileUtil.createDirectoryIfDNE(recipes.toAbsolutePath() + "/" + "dubiousdevices");

    }
}
