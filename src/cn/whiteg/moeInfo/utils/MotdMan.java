package cn.whiteg.moeInfo.utils;

import cn.whiteg.moeInfo.MoeInfo;
import net.minecraft.SharedConstants;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.lang.reflect.Field;

public class MotdMan {
    public static void setMotdName(String name) {
        Server ser = Bukkit.getServer();
        try{
            Field console_f = ser.getClass().getDeclaredField("console");
            console_f.setAccessible(true);
            DedicatedServer con = (DedicatedServer) console_f.get(ser);
            con.setMotd(name);
            MoeInfo.logger.info("设置MOTD为" + name);
        }catch (Exception e){
            MoeInfo.logger.info("设置MOTD失败" + e.getMessage());
        }
    }

    public static void setMotdName() {
        var str = MoeInfo.settin.getConfig().getString("MOTD.Motd","NONE");
        var version = SharedConstants.getGameVersion().getName();
        str = str.replaceAll("%version%",version);
        setMotdName(str);
    }
}
