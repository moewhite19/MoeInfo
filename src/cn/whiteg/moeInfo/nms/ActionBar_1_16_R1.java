package cn.whiteg.moeInfo.nms;

import net.minecraft.server.v1_16_R1.ChatComponentText;
import net.minecraft.server.v1_16_R1.ChatMessageType;
import net.minecraft.server.v1_16_R1.Packet;
import net.minecraft.server.v1_16_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;


public class ActionBar_1_16_R1 implements ActionBar.AcitonBarHander {

    public void send(Player player,String message) {
        if (!player.isOnline()){
            return; // Player may have logged out
        }
        try{
            CraftPlayer craftPlayer = (CraftPlayer) player;
            Packet packet;
            ChatComponentText cct = new ChatComponentText(message);
            packet = new PacketPlayOutChat(cct,ChatMessageType.GAME_INFO,UUID.randomUUID());
            (craftPlayer).getHandle().playerConnection.sendPacket(packet);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
