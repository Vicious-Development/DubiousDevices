package com.drathonix.dubiousdevices.loader;

import com.vicious.viciouslib.persistence.storage.aunotamations.LoadOnly;
import com.vicious.viciouslib.persistence.storage.aunotamations.PersistentPath;
import com.vicious.viciouslib.util.FileUtil;
import com.vicious.viciouslibkit.ViciousLibKit;
import org.bukkit.NamespacedKey;

@LoadOnly
public class Recipe {
    @PersistentPath
    public String path;
    
    public Recipe(NamespacedKey key){
        String folder = FileUtil.createDirectoryIfDNE(ViciousLibKit.defaultRecipesPath + "/" + key.getNamespace()).toAbsolutePath().toString();
        path = folder + "/" + key.toString().replaceAll(":","_") + ".txt";
    }
}
