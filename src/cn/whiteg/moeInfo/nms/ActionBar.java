package cn.whiteg.moeInfo.nms;

import cn.whiteg.moeInfo.utils.EntityNetUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import org.bukkit.entity.Player;

public class ActionBar {
    public static void sendActionBar(Player player,String msg) {
        EntityNetUtils.sendPacket(player,new ClientboundSetActionBarTextPacket(Component.literal(msg)));
    }
}

