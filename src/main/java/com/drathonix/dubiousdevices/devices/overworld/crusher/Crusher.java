package com.drathonix.dubiousdevices.devices.overworld.crusher;

import com.drathonix.dubiousdevices.DubiousDevices;
import com.drathonix.dubiousdevices.devices.overworld.machine.IOTypes;
import com.drathonix.dubiousdevices.devices.overworld.machine.MaterialValue;
import com.drathonix.dubiousdevices.registry.RecipeHandlers;
import com.google.common.collect.Lists;
import com.vicious.viciouslib.database.objectTypes.SQLVector3i;
import com.vicious.viciouslibkit.block.BlockTemplate;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstance;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstanceMaterialOnly;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstanceMultiple;
import com.vicious.viciouslibkit.block.blockinstance.BlockInstanceSolid;
import com.vicious.viciouslibkit.data.provided.multiblock.MultiBlockChunkDataHandler;
import com.vicious.viciouslibkit.data.provided.multiblock.MultiBlockInstance;
import com.vicious.viciouslibkit.data.worldstorage.PluginWorldData;
import com.vicious.viciouslibkit.inventory.InventoryHelper;
import com.vicious.viciouslibkit.inventory.wrapper.EInventoryUpdateStatus;
import com.vicious.viciouslibkit.inventory.wrapper.InventoryWrapper;
import com.vicious.viciouslibkit.inventory.wrapper.InventoryWrapperChunkHandler;
import com.vicious.viciouslibkit.services.multiblock.TickableMultiBlock;
import com.vicious.viciouslibkit.util.ChunkPos;
import com.vicious.viciouslibkit.util.LibKitUtil;
import com.vicious.viciouslibkit.util.NMSHelper;
import com.vicious.viciouslibkit.util.interfaces.INotifiable;
import com.vicious.viciouslibkit.util.interfaces.INotifier;
import com.vicious.viciouslibkit.util.map.ItemStackMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.Slab;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/*
TODO: Create a generic machine multiblock.
 */
public class Crusher extends TickableMultiBlock implements INotifiable<EInventoryUpdateStatus> {
    private List<ItemStack> storedOutputs = new ArrayList<>();
    private List<ItemStack> storedInputs;
    private int maxExtraDrops = 0;
    //Northfacing
    private static final SQLVector3i iol = new SQLVector3i(-2,-3,0);
    private SQLVector3i io1;
    private SQLVector3i io2;

    public int timer = 0;
    public int processTime = 20;

    public CrusherRecipe recipe = null;
    public Inventory inputs;
    public Inventory outputs;

    @Override
    public void save() {
        super.save();
    }

    public Crusher(Class<? extends MultiBlockInstance> mbType, World w, Location l, BlockFace dir, boolean flipped, UUID id) {
        super(mbType, w, l, dir, flipped, id);
    }

    public Crusher(Class<? extends MultiBlockInstance> type, World w, UUID id, ChunkPos cpos) {
        super(type, w, id, cpos);
    }


    @Override
    protected void invalidate(MultiBlockChunkDataHandler dat) {
        InventoryWrapper iw = getInvWrapper(io1);
        InventoryWrapperChunkHandler ch = PluginWorldData.getChunkDataHandler(world,ChunkPos.fromBlockPos(xyz.value()),InventoryWrapperChunkHandler.class);
        if(iw != null){
            iw.stopListening(this);
            ch.removeWrapper(io1);
        }
        iw = getInvWrapper(io2);
        if(iw != null){
            iw.stopListening(this);
            ch.removeWrapper(io2);
        }
        super.invalidate(dat);
    }
    @Override
    public void validate() {
        io1 = LibKitUtil.orientate(iol,facing.value(),flipped.value());
        io2 = LibKitUtil.orientate(iol,facing.value(),!flipped.value());
        io1 = new SQLVector3i(xyz.value().x + io1.x,xyz.value().y + io1.y,xyz.value().z + io1.z);
        io2 = new SQLVector3i(xyz.value().x + io2.x,xyz.value().y + io2.y,xyz.value().z + io2.z);
        Block b = world.getBlockAt(xyz.value().x,xyz.value().y-1,xyz.value().z);
        maxExtraDrops = MaterialValue.getMaterialValue(b.getType());
        Bukkit.getScheduler().scheduleSyncDelayedTask(DubiousDevices.INSTANCE,()->{
            getInvWrapper(io1).listen(this);
            getInvWrapper(io2).listen(this);
        },1);
        super.validate();
    }

    public InventoryWrapper getInvWrapper(SQLVector3i p){
        InventoryWrapperChunkHandler handler = PluginWorldData.getChunkDataHandler(world,ChunkPos.fromBlockPos(p), InventoryWrapperChunkHandler.class);
        return handler.getOrCreateWrapper(world,p);
    }
    @Override
    public void tick() {
        super.tick();
        //Logic. No recipe, check the input inventory. Still no recipe, stop ticking.
        if(timer == 0){
            initInputInv();
            if(inputs != null) {
                if (!checkRecipe(mapInventory(inputs))) {
                    removeFromTicker();
                    postTick();
                    return;
                }
            }
            else {
                postTick();
                return;
            }
        }
        process();
        postTick();
    }

    public void postTick(){
    }

    private void process() {
        if(timer == 0){
            try {
                SQLVector3i vec = xyz.value();
                Block piston = world.getBlockAt(vec.x, vec.y, vec.z);
                Piston pdat = (Piston) piston.getBlockData();
                NMSHelper.setExtended(piston,true);
                pdat.setExtended(true);
                piston.setBlockData(pdat);
                input();
            } catch (Exception e){
                DubiousDevices.LOGGER.warning(e.getMessage());
                e.printStackTrace();
            }
        }
        if(timer >= processTime){
            if(output()){
                timer = 0;
                SQLVector3i vec = xyz.value();
                Block piston = world.getBlockAt(vec.x,vec.y,vec.z);
                Piston pdat = (Piston) piston.getBlockData();
                pdat.setExtended(false);
                piston.setBlockData(pdat);
            }
        }
        else timer++;
    }
    private void input(){
        initInputInv();
        if(!recipe.ignoresNBT()) {
            for (ItemStack input : recipe.getInputs()) {
                inputs.removeItem(input);
            }
        }
        else {
            for (ItemStack input : recipe.getInputs()) {
                extractIgnoreNBT(input);
            }
        }
        storedInputs = recipe.cloneInputs();
    }
    private void extractIgnoreNBT(ItemStack stack){
        ItemStack[] contents = inputs.getContents();
        int count = stack.getAmount();
        for (int i = 0; i < contents.length; i++) {
            if(contents[i] == null) continue;
            if(contents[i].getType() == stack.getType()){
                int fcount = count - contents[i].getAmount();
                contents[i].setAmount(Math.max(0,contents[i].getAmount()-count));
                count = fcount;
            }
        }
        if(count > 0){
            DubiousDevices.LOGGER.severe("DUPLICATION HAS OCCURED!!! PLEASE REPORT TO THE GIT IMMEDIATELY");
            DubiousDevices.LOGGER.severe("CAUSE OF DUPE: " + recipe);
        }
    }
    private boolean output(){
        initOutputInv();
        if(storedOutputs.size() == 0){
            if(recipe.doExtraOutputs){
                storedOutputs = recipe.cloneOutputs();
                if(maxExtraDrops > 0) {
                    storedOutputs.forEach((s) -> {
                        s.setAmount(s.getAmount() + DubiousDevices.random.nextInt(maxExtraDrops + 1));
                    });
                }
            }
            else storedOutputs = recipe.cloneOutputs();
        }
        InventoryHelper.moveFrom(outputs,storedOutputs);
        return storedOutputs.size() == 0;
    }

    public boolean checkRecipe(ItemStackMap inputs){
        if(recipe != null && !recipe.matches(inputs)) recipe = null;
        if (!getRecipe(inputs)) {
            return false;
        } else {
            return true;
        }
    }
    public boolean getRecipe(ItemStackMap inputs){
        recipe = RecipeHandlers.CRUSHER.getRecipe(inputs);
        return recipe != null;
    }

    @Override
    public boolean tickOnInit() {
        return true;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return ((Crusher) o).ID.equals(this.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
    private void initInputInv(){
        if(inputs == null){
            Block b1 = world.getBlockAt(io1.x,io1.y,io1.z);
            if(IOTypes.isInput(b1.getType())){
                inputs = getInventory(io1);
                outputs = getInventory(io2);
            }
            else {
                inputs = getInventory(io2);
                outputs = getInventory(io1);
            }
        }
        else{
            inputs = getInventory(inputs.getLocation());
        }
    }
    private void initOutputInv() {
        if(outputs == null){
            initInputInv();
        }
        else{
            outputs = getInventory(outputs.getLocation());
        }
    }
    private static ItemStackMap mapInventory(Inventory inv){
        ItemStackMap map = new ItemStackMap();
        for (ItemStack stack : inv.getStorageContents()) {
            if(stack == null) continue;
            map.add(stack);
        }
        return map;
    }
    private Inventory getInventory(SQLVector3i p) {
        Block b = world.getBlockAt(p.x,p.y,p.z);
        if(b.getState() instanceof Container){
            return ((Container) b.getState()).getInventory();
        } else return null;
    }
    private Inventory getInventory(Location l) {
        Block b = world.getBlockAt(l.getBlockX(),l.getBlockY(),l.getBlockZ());
        if(b.getState() instanceof Container){
            return ((Container) b.getState()).getInventory();
        } else return null;
    }

    public static void addRecipe(ItemStack[] inputs, ItemStack[] outputs){
        RecipeHandlers.CRUSHER.addRecipe(new CrusherRecipe(Lists.newArrayList(inputs),Lists.newArrayList(outputs)));
    }
    public static void removeRecipe(ItemStack[] inputs){
        RecipeHandlers.CRUSHER.removeRecipe(Lists.newArrayList(inputs));
    }

    public static BlockTemplate template(){
        BlockInstance n = null;
        BlockInstance a = BlockInstance.AIR;
        BlockInstance b = new BlockInstanceMultiple()
                .add(new BlockInstanceMaterialOnly(Material.DEEPSLATE))
                .add(new BlockInstanceMaterialOnly(Material.DEEPSLATE_BRICKS))
                .add(new BlockInstanceMaterialOnly(Material.DEEPSLATE_TILES))
                .add(new BlockInstanceMaterialOnly(Material.COBBLED_DEEPSLATE))
                .add(new BlockInstanceMaterialOnly(Material.CHISELED_DEEPSLATE))
                .add(new BlockInstanceMaterialOnly(Material.POLISHED_DEEPSLATE))
                .add(new BlockInstanceMaterialOnly(Material.CRACKED_DEEPSLATE_BRICKS))
                .add(new BlockInstanceMaterialOnly(Material.CRACKED_DEEPSLATE_TILES));
        BlockInstance c = new BlockInstanceMultiple()
                .add(new BlockInstance(Material.DEEPSLATE_BRICK_SLAB).slabType(Slab.Type.BOTTOM))
                .add(new BlockInstance(Material.DEEPSLATE_TILE_SLAB).slabType(Slab.Type.BOTTOM))
                .add(new BlockInstance(Material.POLISHED_DEEPSLATE_SLAB).slabType(Slab.Type.BOTTOM))
                .add(new BlockInstance(Material.COBBLED_DEEPSLATE_SLAB).slabType(Slab.Type.BOTTOM))
                ;
        BlockInstance d = new BlockInstanceMultiple()
                .add(new BlockInstance(Material.DEEPSLATE_BRICK_SLAB).slabType(Slab.Type.TOP))
                .add(new BlockInstance(Material.DEEPSLATE_TILE_SLAB).slabType(Slab.Type.TOP))
                .add(new BlockInstance(Material.POLISHED_DEEPSLATE_SLAB).slabType(Slab.Type.TOP))
                .add(new BlockInstance(Material.COBBLED_DEEPSLATE_SLAB).slabType(Slab.Type.TOP))
                ;
        BlockInstance i = new BlockInstanceMultiple()
                .add(new BlockInstance(Material.HOPPER))
                .add(new BlockInstance(Material.TRAPPED_CHEST))
                .add(new BlockInstance(Material.BARREL))
                .add(new BlockInstance(Material.CHEST))
                .add(new BlockInstance(Material.DISPENSER))
                .add(new BlockInstance(Material.DROPPER));
        BlockInstance f = new BlockInstanceMultiple()
                .add(new BlockInstance(Material.COPPER_BLOCK))
                .add(new BlockInstance(Material.WEATHERED_COPPER))
                .add(new BlockInstance(Material.EXPOSED_COPPER))
                .add(new BlockInstance(Material.OXIDIZED_COPPER))
                .add(new BlockInstance(Material.CUT_COPPER))
                .add(new BlockInstance(Material.WEATHERED_CUT_COPPER))
                .add(new BlockInstance(Material.EXPOSED_CUT_COPPER))
                .add(new BlockInstance(Material.OXIDIZED_CUT_COPPER))
                .add(new BlockInstance(Material.WAXED_COPPER_BLOCK))
                .add(new BlockInstance(Material.WAXED_WEATHERED_COPPER))
                .add(new BlockInstance(Material.WAXED_EXPOSED_COPPER))
                .add(new BlockInstance(Material.WAXED_OXIDIZED_COPPER))
                .add(new BlockInstance(Material.WAXED_CUT_COPPER))
                .add(new BlockInstance(Material.WAXED_WEATHERED_CUT_COPPER))
                .add(new BlockInstance(Material.WAXED_EXPOSED_CUT_COPPER))
                .add(new BlockInstance(Material.WAXED_OXIDIZED_CUT_COPPER))
                ;
        BlockInstance p = new BlockInstance(Material.STICKY_PISTON).facing(BlockFace.DOWN);
        BlockInstanceSolid k = new BlockInstanceSolid();
        return BlockTemplate.start()
                .x(n,b,c,b,n).z()
                .x(i,b,b,b,i).z()
                .x(n,b,c,b,n).y()

                .x(n,b,c,b,n).z()
                .x(n,a,a,a,n).z()
                .x(n,b,c,b,n).y()

                .x(n,b,d,b,n).z()
                .x(n,d,k,d,n).z()
                .x(n,b,d,b,n).y()

                .x(n,c,d,c,n).z()
                .x(n,c,p,c,n).z()
                .x(n,c,d,c,n).y()

                .x(n,n,n,n,n).z()
                .x(n,n,f,n,n).z()
                .x(n,n,n,n,n).finish(2,3,1)
                ;
    }


    @Override
    public void notify(INotifier<EInventoryUpdateStatus> sender, EInventoryUpdateStatus status) {
        addToTicker();
    }
}
