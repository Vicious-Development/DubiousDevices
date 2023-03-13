package com.drathonix.dubiousdevices.util;

/**
 * Used in machines to determine when to end processes.
 *
 *
 */
public class ProcessTimer {
    int time = 0;
    public ProcessTimer(){}
    public void tick(){
        time++;
    }
    public boolean hasPassedOrReached(int time){
        return this.time >= time;
    }
    public boolean isAt(int time){
        return this.time == time;
    }
    public void reset(){
        time = 0;
    }
}
