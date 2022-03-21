package com.drathonix.dubiousdevices;

import com.drathonix.dubiousdevices.util.DubiousDirectories;
import com.vicious.viciouslib.configuration.JSONConfig;
import com.vicious.viciouslib.database.tracking.values.TrackableObject;


public class DubiousCFG extends JSONConfig {
    private static DubiousCFG instance;
    public static DubiousCFG getInstance() {
        if(instance == null) instance = new DubiousCFG();
        return instance;
    }

    public TrackableObject<Boolean> addRecipesToFront = add(new TrackableObject<>("AddCustomRecipesToTheFront", ()->true,this));
    public TrackableObject<Boolean> crusherEnabled = add(new TrackableObject<>("CrusherEnabled", ()->true,this));
    public TrackableObject<Boolean> compactorEnabled = add(new TrackableObject<>("CompactorEnabled", ()->true,this));
    public TrackableObject<Boolean> heavyFurnaceEnabled = add(new TrackableObject<>("HeavyFurnaceEnabled", ()->true,this));
    public TrackableObject<Boolean> heatMeterEnabled = add(new TrackableObject<>("HeatMeterEnabled", ()->true,this));
    public TrackableObject<Boolean> autoMinerEnabled = add(new TrackableObject<>("AutoMinerEnabled", ()->true,this));
    public DubiousCFG() {
        super(DubiousDirectories.dubiousConfigPath);
    }
}
