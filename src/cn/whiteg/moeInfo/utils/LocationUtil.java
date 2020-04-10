package cn.whiteg.moeInfo.utils;

import cn.whiteg.moeInfo.MoeInfo;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.Location;
import org.bukkit.World;

import static cn.whiteg.moeInfo.MoeInfo.plugin;

public class LocationUtil {
    public static String getWorldDisplayName(World world) {
        if (plugin.externalManage.multiverseCore != null){
            MultiverseWorld mvWolrd = MoeInfo.plugin.externalManage.multiverseCore.getMVWorldManager().getMVWorld(world);
            if (mvWolrd != null){
                return mvWolrd.getAlias();
            }
        }
        return world.getName();
    }

    public static String getResName(Location loc) {
        if (MoeInfo.plugin.externalManage.multiverseCore != null){
            ClaimedResidence res = plugin.externalManage.residence.getResidenceManager().getByLoc(loc);
            if (res == null) return "";
            return res.getName();
        }
        return "";
    }

    public static ClaimedResidence getRes(Location loc) {
        if (plugin.externalManage.residence != null){
            ClaimedResidence res = plugin.externalManage.residence.getResidenceManager().getByLoc(loc);
            if (res == null) return null;
            return res;
        }
        return null;
    }
}
