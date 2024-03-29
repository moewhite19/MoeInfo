package cn.whiteg.moeInfo.utils;

import cn.whiteg.mmocore.reflection.ReflectUtil;
import io.netty.channel.Channel;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class EntityNetUtils {
    static Field entity_counter;
    static Field connect_network;
    static Field network_channel;


    static {
        try{
            entity_counter = ReflectUtil.getFieldFormType(EntityPlayer.class,PlayerConnection.class);
            connect_network = ReflectUtil.getFieldFormType(PlayerConnection.class,NetworkManager.class);
            network_channel = ReflectUtil.getFieldFormType(NetworkManager.class,Channel.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static PlayerConnection getPlayerConnection(EntityPlayer player) {
        try{
            return (PlayerConnection) entity_counter.get(player);
        }catch (IllegalAccessException e){
            throw new RuntimeException(e);
        }
    }

    public static NetworkManager getNetWork(PlayerConnection playerConnection) {
        try{
            return (NetworkManager) connect_network.get(playerConnection);
        }catch (IllegalAccessException e){
            throw new RuntimeException(e);
        }
    }

    //获取玩家网络通道
    public static Channel getChannel(NetworkManager networkManager) {
        try{
            return (Channel) network_channel.get(networkManager);
        }catch (IllegalAccessException e){
            throw new RuntimeException(e);
        }
    }

    //根据id获取实体
    public static Entity getEntityById(World world,int id) {
        try{
            WorldServer nmsWorld = (WorldServer) world.getClass().getMethod("getHandle").invoke(world);
            var entity = nmsWorld.a(id);
            return entity == null ? null : entity.getBukkitEntity();
        }catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public static net.minecraft.world.entity.Entity getNmsEntity(Entity bukkitEntity) {
        try{
            //noinspection ResultOfMethodCallIgnored
            return (net.minecraft.world.entity.Entity) bukkitEntity.getClass().getMethod("getHandle").invoke(bukkitEntity);
        }catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e){
            throw new RuntimeException(e);
        }
    }

    public static EntityPlayer getNmsPlayer(Player player) {
        return (EntityPlayer) getNmsEntity(player);
    }

    public static void sendPacket(Player player,Packet<?> packet){
        getPlayerConnection(getNmsPlayer(player)).b(packet);
    }
}
