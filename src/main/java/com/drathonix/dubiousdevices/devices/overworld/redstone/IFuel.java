package com.drathonix.dubiousdevices.devices.overworld.redstone;

public interface IFuel<T> extends ILeveled<T>{
    int getFuelRemaining();
    int getFuelMax();
    T getFuelType();
    default double fuelProportion(){
        return (double)getFuelRemaining()/getFuelMax();
    }
    default double fuelPercent(){
        return 100*fuelProportion();
    }

    @Override
    default double getLevel(){
        return fuelProportion();
    }
}
