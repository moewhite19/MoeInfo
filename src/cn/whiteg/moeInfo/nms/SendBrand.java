package cn.whiteg.moeInfo.nms;

import cn.whiteg.moeInfo.utils.EntityNetUtils;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.BrandPayload;
import org.bukkit.entity.Player;

public class SendBrand {

    public static void send(Player player,String brand) {
        //brand = PAPIHook.getPAPIString(player, brand);
        //Validate.notNull(player,"Player is null!");
        //Validate.notNull(brand,"Server brand is null!");
        if (player == null) return;
        if (brand == null) return;
//        final FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
//        final BrandPayload customPacketPayload = new BrandPayload(friendlyByteBuf.a(brand));
        final BrandPayload customPacketPayload = new BrandPayload(brand);
        final ClientboundCustomPayloadPacket packet = new ClientboundCustomPayloadPacket(customPacketPayload);
        EntityNetUtils.sendPacket(player,packet);
    }
}
