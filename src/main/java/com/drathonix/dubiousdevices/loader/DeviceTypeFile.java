package com.drathonix.dubiousdevices.loader;

import com.vicious.viciouslib.aunotamation.Aunotamation;
import com.vicious.viciouslib.persistence.json.JSONFile;
import com.vicious.viciouslib.persistence.storage.PersistentAttribute;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;

public class DeviceTypeFile extends JSONFile {

    @Save
    public PersistentAttribute<JSONMap>

    public DeviceTypeFile(String path) {
        super(path);
        Aunotamation.processObject(this);
        load();
        save();
    }
}
