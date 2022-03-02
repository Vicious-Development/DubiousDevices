package com.drathonix.dubiousdevices.inventory.gui;

import com.vicious.viciouslibkit.event.Ticker;
import com.vicious.viciouslibkit.interfaces.ITickable;
import org.bukkit.inventory.ItemStack;

public class TickableGUIElement extends GUIElement<TickableGUIElement> implements ITickable {
    protected Runnable onTick;
    public TickableGUIElement(ItemStack stack) {
        super(stack);
        Ticker.add(this);
    }

    public TickableGUIElement(ItemStack stack, boolean cancelEvent) {
        super(stack, cancelEvent);
        Ticker.add(this);
    }

    public TickableGUIElement onTick(Runnable tickExecutable){
        this.onTick=tickExecutable;
        return this;
    }
    @Override
    public void tick() {
        onTick.run();
    }
}
