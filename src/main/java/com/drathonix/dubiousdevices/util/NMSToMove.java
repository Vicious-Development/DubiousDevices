package com.drathonix.dubiousdevices.util;

import com.vicious.viciouslib.util.reflect.Reflection;
import com.vicious.viciouslib.util.reflect.deep.DeepReflection;
import com.vicious.viciouslib.util.reflect.deep.MethodSearchContext;
import com.vicious.viciouslib.util.reflect.deep.TotalFailureException;
import com.vicious.viciouslib.util.reflect.wrapper.ReflectiveConstructor;
import com.vicious.viciouslib.util.reflect.wrapper.ReflectiveField;
import com.vicious.viciouslib.util.reflect.wrapper.ReflectiveMethod;
import com.vicious.viciouslibkit.util.NMSHelper;

import java.lang.reflect.Field;
import java.util.function.Function;

public class NMSToMove {
    public static ReflectiveConstructor MinecraftKey$new = new ReflectiveConstructor(Reflection.getConstructor(NMSHelper.MinecraftKey,String.class));
    public static Class<?> TileEntityComparator = DeepReflection.get("TileEntityComparator","net.minecraft");
    public static Class<?> CraftBlockEntityState = DeepReflection.get("CraftBlockEntityState","org.bukkit");
    public static ReflectiveMethod CraftBlockEntity$refreshSnapshot;
    public static ReflectiveField CraftBlockEntity$tileEntity = new ReflectiveField("tileEntity");
    /**
     * Fuck you bukkit. Can't set a comparator's signal without this bullshit.
     */
    public static ReflectiveMethod TileEntityComparator$setOutputSignal;

    static {
        try {
            CraftBlockEntity$refreshSnapshot = DeepReflection.getMethod(CraftBlockEntityState,new MethodSearchContext().name("refreshSnapshot"));
            TileEntityComparator$setOutputSignal = DeepReflection.getMethod(TileEntityComparator,new MethodSearchContext().accepts(int.class).returns(void.class).exceptions());
        } catch (TotalFailureException e) {
            e.printStackTrace();
        }
    }

    public static <T> T forAllFields(Class<?> target, Function<Field,T> func) throws TotalFailureException {
        for (Field declaredField : target.getDeclaredFields()) {
            T ret = func.apply(declaredField);
            if(ret != null) return ret;
        }
        throw new TotalFailureException("No object found to return using a custom search function.");
    }

}
