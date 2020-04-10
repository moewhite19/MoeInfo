package cn.whiteg.moeInfo.utils;

import cn.whiteg.mmocore.util.NMSUtils;
import cn.whiteg.moeInfo.MoeInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minecraft.SharedConstants;
import net.minecraft.WorldVersion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.lang.reflect.Field;

public class MotdUtil {
    private static String SERVER_VER;
    private static Field motdField;
    private static Field cacheMotd;

    static {
        try{
            Field field = NMSUtils.getFieldFormType(SharedConstants.class,WorldVersion.class);
            field.setAccessible(true);
            WorldVersion ver = (WorldVersion) field.get(null);
            SERVER_VER = ver == null ? "NULL" : ver.getName();
        }catch (IllegalAccessException | NoSuchFieldException e){
            e.printStackTrace();
            SERVER_VER = e.getMessage();
        }

        try{
            motdField = NMSUtils.getFieldFormStructure(MinecraftServer.class,String.class,int.class,long[].class)[0];
            motdField.setAccessible(true);
        }catch (NoSuchFieldException e){
            try{
                cacheMotd = NMSUtils.getFieldFormStructure(MinecraftServer.class,Component.class,int.class,long[].class)[0];
                cacheMotd.setAccessible(true);
            }catch (NoSuchFieldException ex){
                throw new RuntimeException(ex);
            }
        }
    }

    public static void setMotdName(String name) {
        Server ser = Bukkit.getServer();
        try{
            Field console_f = ser.getClass().getDeclaredField("console");
            console_f.setAccessible(true);
            DedicatedServer con = (DedicatedServer) console_f.get(ser);
            if (motdField != null){
                motdField.set(con,name);
            } else {
                final TextComponent text = Component.text(name);
//                        .hoverEvent(Component.text(ser.getName())); //设置的悬浮字体根本看不到
                cacheMotd.set(con,text);
            }
            MoeInfo.logger.info("设置MOTD为" + name);
        }catch (Exception e){
            MoeInfo.logger.info("设置MOTD失败" + e.getMessage());
        }
    }

    public static void setMotdName() {
        var str = MoeInfo.settin.getConfig().getString("MOTD.Motd","NONE");
        str = str.replaceAll("%version%",SERVER_VER);
        setMotdName(str);
    }
}
