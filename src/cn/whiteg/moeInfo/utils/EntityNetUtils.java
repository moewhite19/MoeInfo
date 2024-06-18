package cn.whiteg.moeInfo.utils;

import cn.whiteg.mmocore.reflection.ReflectUtil;
import io.netty.channel.Channel;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class EntityNetUtils {
    static Field entity_counter;
    static Field connect_network;
    static Field network_channel;


    static {
        try{
            entity_counter = ReflectUtil.getFieldFormType(ServerPlayer.class,ServerGamePacketListenerImpl.class);
            connect_network = ReflectUtil.getFieldFormType(ServerGamePacketListenerImpl.class,Connection.class);
            network_channel = ReflectUtil.getFieldFormType(Connection.class,Channel.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ServerGamePacketListenerImpl getServerGamePacketListenerImpl(ServerPlayer player) {
        try{
            return (ServerGamePacketListenerImpl) entity_counter.get(player);
        }catch (IllegalAccessException e){
            throw new RuntimeException(e);
        }
    }

    public static Connection getNetWork(ServerGamePacketListenerImpl playerConnection) {
        try{
            return (Connection) connect_network.get(playerConnection);
        }catch (IllegalAccessException e){
            throw new RuntimeException(e);
        }
    }

    //获取玩家网络通道
    public static Channel getChannel(Connection networkManager) {
        try{
            return (Channel) network_channel.get(networkManager);
        }catch (IllegalAccessException e){
            throw new RuntimeException(e);
        }
    }


    public static ServerPlayer getNmsPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    public static void sendPacket(Player player,Packet<?> packet) {
        getServerGamePacketListenerImpl(getNmsPlayer(player)).sendPacket(packet);
    }
}
