package com.drathonix.dubiousdevices.util;

import com.vicious.viciouslib.database.objectTypes.SQLVector3i;
import com.vicious.viciouslibkit.util.LibKitUtil;
import org.bukkit.block.BlockFace;

public class DubiousUtil {
    public static SQLVector3i rotate(SQLVector3i vec, BlockFace face){
        if(face == BlockFace.NORTH) return vec;
        if(face == BlockFace.SOUTH) return LibKitUtil.rotateQuad2(LibKitUtil.rotateQuad2(vec));
        if(face == BlockFace.EAST) return LibKitUtil.rotateQuad4(vec);
        if(face == BlockFace.WEST) return LibKitUtil.rotateQuad2(vec);
        return vec;
    }
    public static SQLVector3i orientate(SQLVector3i vec, BlockFace face, boolean flipped){
        if(flipped) vec = flipX(vec);
        vec = rotate(vec,face);
        return vec;
    }
    public static SQLVector3i flipX(SQLVector3i vec){
        return new SQLVector3i(-vec.x, vec.y, vec.z);
    }
}
