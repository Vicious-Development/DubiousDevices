package com.drathonix.dubiousdevices.devices.overworld.redstone;

import com.drathonix.dubiousdevices.DDBlockInstances;
import com.drathonix.dubiousdevices.devices.overworld.machine.MachineStatus;
import com.drathonix.dubiousdevices.util.NMSToMove;
import com.vicious.viciouslib.database.objectTypes.SQLVector3i;
import com.vicious.viciouslib.util.reflect.deep.DeepReflection;
import com.vicious.viciouslibkit.block.BlockTemplate;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstance;
import com.vicious.viciouslibkit.data.provided.multiblock.MultiBlockChunkDataHandler;
import com.vicious.viciouslibkit.data.provided.multiblock.MultiBlockInstance;
import com.vicious.viciouslibkit.data.worldstorage.PluginWorldData;
import com.vicious.viciouslibkit.event.Ticker;
import com.vicious.viciouslibkit.interfaces.ITickable;
import com.vicious.viciouslibkit.util.ChunkPos;
import com.vicious.viciouslibkit.util.interfaces.INotifiable;
import com.vicious.viciouslibkit.util.interfaces.INotifier;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Used to measure the heat level or remaining burntime of a machine.
 * Applicable to:
 * IFurnaceFuel: Meter heat level changes based on the furnace's burntime. Assuming the interface is implemented in that way (which it should be)
 */

public class HeatMeter extends MultiBlockInstance implements ITickable, INotifiable<MachineStatus> {
    private static Map<Class<? extends ILeveled<?>>,Class<? extends ILeveled<?>>> supported = new HashMap<>();
    private SQLVector3i comparatorLocation = new SQLVector3i(xyz.value().x,xyz.value().y+1,xyz.value().z);
    private static int redstoneOut = 0;
    static {
        supported.put(IFurnaceFuel.class,IFurnaceFuel.class);
    }
    private ILeveled<?> machine;
    public HeatMeter(Class<? extends MultiBlockInstance> mbType, World w, Location l, BlockFace dir, boolean flipped, UUID id) {
        super(mbType, w, l, dir, flipped, id);
    }

    public HeatMeter(Class<? extends MultiBlockInstance> type, World w, UUID id, ChunkPos cpos) {
        super(type, w, id, cpos);
    }

    @Override
    public void validate() {
        super.validate();
        Block comparator = world.getBlockAt(xyz.value().x,xyz.value().y+1,xyz.value().z);
        Directional dat = (Directional) comparator.getBlockData();
        BlockFace facing = dat.getFacing().getOppositeFace();
        Vector targetBlock = new Vector(comparator.getX(),comparator.getY(),comparator.getZ());
        targetBlock = targetBlock.add(facing.getDirection());
        ChunkPos chunk = ChunkPos.fromBlockPos(new SQLVector3i(targetBlock.getBlockX(),targetBlock.getBlockY(),targetBlock.getBlockZ()));
        Vector finalTargetBlock = targetBlock;
        PluginWorldData.getWorldData(world).scheduleChunkLoadProcess((c)->{
            MultiBlockChunkDataHandler mbcdh = PluginWorldData.getChunkDataHandler(world, chunk, MultiBlockChunkDataHandler.class);
            MultiBlockInstance mb = mbcdh.getMultiblock(new SQLVector3i(finalTargetBlock.getBlockX(), finalTargetBlock.getBlockY(), finalTargetBlock.getBlockZ()));
            if(isSupported(mb)) machine = (ILeveled<?>) mb;
            Ticker.add(this);
        },chunk);
    }

    private boolean isSupported(MultiBlockInstance mb) {
        return DeepReflection.cycleAndExecute(mb.getClass(), (cls) -> {
            if (supported.containsKey(cls)) return true;
            else return null;
        }) != null;
    }

    public static BlockTemplate template(){
        BlockInstance c = DDBlockInstances.ALLCOPPERBLOCKS;
        return BlockTemplate.start()
                .x(c).y().x(new BlockInstance(Material.COMPARATOR)).finish(0,0,0)
                ;
    }

    @Override
    public void tick() {
        int prev = redstoneOut;
        redstoneOut = (int) (machine.getLevel()*16);
        if(prev != redstoneOut){
            Block comparator = world.getBlockAt(comparatorLocation.x,comparatorLocation.y,comparatorLocation.z);
            Comparator comp = (Comparator) comparator.getBlockData();
            NMSToMove.TileEntityComparator$setOutputSignal.invoke(NMSToMove.CraftBlockEntity$tileEntity.get(comp),redstoneOut);
            NMSToMove.CraftBlockEntity$refreshSnapshot.invoke(comp);
        }
    }

    @Override
    public void notify(INotifier<MachineStatus> iNotifier, MachineStatus status) {
        if(status == MachineStatus.INVALIDATED) Ticker.remove(this);
    }
}
