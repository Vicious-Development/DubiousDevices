package com.drathonix.dubiousdevices.devices.overworld.compacter;

import com.drathonix.dubiousdevices.DDBlockInstances;
import com.drathonix.dubiousdevices.DubiousCFG;
import com.drathonix.dubiousdevices.DubiousDevices;
import com.drathonix.dubiousdevices.devices.overworld.machine.DeviceItemIO;
import com.drathonix.dubiousdevices.devices.overworld.machine.DeviceVectors;
import com.drathonix.dubiousdevices.devices.overworld.machine.MaterialValue;
import com.drathonix.dubiousdevices.recipe.RecipeHandler;
import com.drathonix.dubiousdevices.registry.RecipeHandlers;
import com.vicious.viciouslib.database.objectTypes.SQLVector3i;
import com.vicious.viciouslibkit.block.BlockTemplate;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstance;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstanceSolid;
import com.vicious.viciouslibkit.data.provided.multiblock.MultiBlockChunkDataHandler;
import com.vicious.viciouslibkit.data.provided.multiblock.MultiBlockInstance;
import com.vicious.viciouslibkit.event.piston.PistonController;
import com.vicious.viciouslibkit.event.piston.PistonWatcher;
import com.vicious.viciouslibkit.util.LibKitUtil;
import com.vicious.viciouslibkit.util.vector.ChunkPos;
import com.vicious.viciouslibkit.util.vector.WorldPosI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

import java.util.UUID;

public class Compactor extends DeviceItemIO<CompactorRecipe> {
    public int materialValue = 0;
    private PistonController pistonController;
    public Compactor(Class<? extends MultiBlockInstance> mbType, World w, Location l, BlockFace dir, boolean flipped, boolean upsidedown, UUID id) {
        super(mbType, w, l, dir, flipped, upsidedown,id);
        defaultProcessTime=160;
    }

    public Compactor(Class<? extends MultiBlockInstance> type, World w, UUID id, ChunkPos cpos) {
        super(type, w, id, cpos);
        defaultProcessTime=160;
    }

    @Override
    protected RecipeHandler<CompactorRecipe> getRecipeHandler() {
        return RecipeHandlers.COMPACTOR;
    }

    @Override
    public void initInputInvs(){
        if(inputs.isEmpty()){
            IOAutoSetup(inputs,outputs,getRelativePosition(DeviceVectors.iolCompactor),getRelativePosition(LibKitUtil.flipX(DeviceVectors.iolCompactor)));
        }
        else{
            resetInputs();
        }
    }

    @Override
    public void validate() {
        super.validate();
        if(!DubiousCFG.getInstance().compactorEnabled.value()) return;
        SQLVector3i v = xyz.value().add(0,-2,0);
        materialValue = MaterialValue.getMaterialValue(world.getBlockAt(v.x,v.y,v.z).getType());
        pistonController = new PistonController(new WorldPosI(world,xyz.value().x,xyz.value().y,xyz.value().z));
        PistonWatcher.listenRetract(pistonController.getPosition(),pistonController);
        Bukkit.getScheduler().scheduleSyncDelayedTask(DubiousDevices.INSTANCE, this::initInputInvs,1);
    }

    @Override
    protected void invalidate(MultiBlockChunkDataHandler dat) {
        super.invalidate(dat);
        PistonWatcher.stopListeningRetract(pistonController.getPosition(),pistonController);
    }

    @Override
    public boolean tickOnInit() {
        return DubiousCFG.getInstance().compactorEnabled.value();
    }

    @Override
    protected void processStart() throws Exception {
        super.processStart();
        if(recipe.accelerate) processTime = defaultProcessTime/(1+materialValue);
        else processTime = defaultProcessTime;
        pistonController.keepExtended();
        input();
    }

    @Override
    protected void processEnd() throws Exception {
        if(output()){
            super.processEnd();
            pistonController.attemptRetract();
        }
    }

    public static BlockTemplate template(){
        BlockInstance n = null;
        BlockInstance a = BlockInstance.AIR;
        BlockInstance b = DDBlockInstances.ALLDEEPSLATE;
        BlockInstance s = DDBlockInstances.ALLDEEPSLATESTAIRSIGNORE;
        BlockInstance i = DDBlockInstances.IOBLOCKS;
        BlockInstance p = new BlockInstance(Material.PISTON).facing(BlockFace.DOWN);
        BlockInstanceSolid k = new BlockInstanceSolid();

        return BlockTemplate.start()
                .x(n,b,b,b,n).z()
                .x(i,b,k,b,i).z()
                .x(n,s,s,s,n).y()

                .x(n,s,b,s,n).z()
                .x(n,a,a,a,n).z()
                .x(n,n,n,n,n).y()

                .x(n,n,b,n,n).z()
                .x(n,n,p,n,n).z()
                .x(n,n,n,n,n).y()

                .x(n,n,s,n,n).z()
                .x(n,n,s,n,n).z()
                .x(n,n,n,n,n).y()
                .finish(2,2,1)
                ;
    }
}
