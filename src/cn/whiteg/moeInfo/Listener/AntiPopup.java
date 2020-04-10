package cn.whiteg.moeInfo.Listener;

import cn.whiteg.mmocore.util.NMSUtils;
import cn.whiteg.moepacketapi.api.event.PacketSendEvent;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.chat.SignedMessageHeader;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerChatHeaderPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
import net.minecraft.network.protocol.game.ClientboundServerDataPacket;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;
import java.util.UUID;

//放置弹出服务器签名
public class AntiPopup implements Listener {
    static Field secureChatEnforced;

    static {
        try{
            final Field[] structure = NMSUtils.getFieldFormStructure(ClientboundServerDataPacket.class,boolean.class,boolean.class);
            secureChatEnforced = structure[1];
            secureChatEnforced.setAccessible(true);


        }catch (NoSuchFieldException e){
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onPackSend(PacketSendEvent event) {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof ClientboundServerDataPacket dataPacket){
            try{
                secureChatEnforced.set(dataPacket,true);
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }
        } else if (packet instanceof ClientboundPlayerChatHeaderPacket){
            //阻止聊天头，不然说话会导致其他人掉线
            event.setCancelled(true);
        } else if (packet instanceof ClientboundPlayerChatPacket chatPacket){
            //去除聊天签名，不然也会导致其他玩家掉线
            PlayerChatMessage chatMessage = chatPacket.b();
            final MessageSignature signature = new MessageSignature(new byte[0]);
            final SignedMessageHeader signedMessageHeader = new SignedMessageHeader(signature,new UUID(0,0));
            chatMessage = new PlayerChatMessage(signedMessageHeader,signature,chatMessage.k(),chatMessage.l(),chatMessage.m());
            event.setPacket(new ClientboundPlayerChatPacket(chatMessage,chatPacket.c()));
        }
    }
}
