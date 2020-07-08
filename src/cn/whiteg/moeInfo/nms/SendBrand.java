package cn.whiteg.moeInfo.nms;

import cn.whiteg.moeInfo.MoeInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SendBrand {
    static SendBrandHander hander;

    static {
        String ver = Bukkit.getBukkitVersion();
        try{
            hander = new SendBrand_1_16_R1();
        }catch (Exception e){

        }
        if (hander == null){
            MoeInfo.logger.info("没有适配的版本: " + ver);
        }
    }

    public static void send(Player player,String string) {
        if (hander != null) hander.send(player,string);
    }

    public static SendBrandHander getHandle() {
        return hander;
    }

    public interface SendBrandHander {
        void send(Player player,String msg);
    }
}
