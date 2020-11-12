package cn.whiteg.moeInfo.nms;

import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutCustomPayload;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SendBrand_1_16_R1 implements SendBrand.SendBrandHander {
    @Override
    public void send(Player player,String brand) {
        //brand = PAPIHook.getPAPIString(player, brand);
        //Validate.notNull(player,"Player is null!");
        //Validate.notNull(brand,"Server brand is null!");
        if(player == null )return;
        if(brand == null) return;
        CraftPlayer cp = (CraftPlayer) player;
        cp.getHandle().playerConnection.sendPacket(new PacketPlayOutCustomPayload(PacketPlayOutCustomPayload.a,new PacketDataSerializer(Unpooled.buffer()).a(brand)));
    }

}
