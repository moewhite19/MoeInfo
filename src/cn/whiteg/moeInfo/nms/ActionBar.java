package cn.whiteg.moeInfo.nms;

import org.bukkit.entity.Player;

public class ActionBar {
    private final static AcitonBarHander hander;
    static {
        hander = new ActionBar_1_16_R2();
    }
    public static void sendActionBar(Player player , String msg){
        hander.send(player , msg);
    }

    public interface AcitonBarHander {
        void send(Player player,String msg);
    }
}

