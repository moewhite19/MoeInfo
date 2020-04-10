package cn.whiteg.moeInfo.nms;

import cn.whiteg.moeInfo.utils.EntityNetUtils;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.entity.Player;

public class ActionBar {
    public static void sendActionBar(Player player,String msg) {
        EntityPlayer np = EntityNetUtils.getNmsPlayer(player);
        EntityNetUtils.getPlayerConnection(np).a(new ClientboundSetActionBarTextPacket(IChatBaseComponent.a(msg)));
//        player.sendActionBar(msg);
    }
}

