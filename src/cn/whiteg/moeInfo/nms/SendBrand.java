package cn.whiteg.moeInfo.nms;

import cn.whiteg.moeInfo.utils.EntityNetUtils;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.PacketPlayOutCustomPayload;
import org.bukkit.entity.Player;

public class SendBrand {
    public static void send(Player player,String brand) {
        //brand = PAPIHook.getPAPIString(player, brand);
        //Validate.notNull(player,"Player is null!");
        //Validate.notNull(brand,"Server brand is null!");
        if (player == null) return;
        if (brand == null) return;
        var np = EntityNetUtils.getNmsPlayer(player);
        EntityNetUtils.getPlayerConnection(np).sendPacket(new PacketPlayOutCustomPayload(PacketPlayOutCustomPayload.a,new PacketDataSerializer(Unpooled.buffer()).a(brand)));
    }
}
