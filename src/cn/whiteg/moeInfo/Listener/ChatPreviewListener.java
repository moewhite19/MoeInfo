package cn.whiteg.moeInfo.Listener;

import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.util.NMSUtils;
import cn.whiteg.moeInfo.MoeInfo;
import cn.whiteg.moepacketapi.api.event.PacketReceiveEvent;
import cn.whiteg.moepacketapi.utils.EntityNetUtils;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundChatPreviewPacket;
import net.minecraft.network.protocol.game.ServerboundChatPreviewPacket;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;

public class ChatPreviewListener implements Listener {

    private static Field getChat;

    static {
        try{
            getChat = NMSUtils.getFieldFormType(ServerboundChatPreviewPacket.class,String.class);
            getChat.setAccessible(true);

        }catch (NoSuchFieldException e){
            throw new RuntimeException(e);
        }
    }

    //聊天预览
    @EventHandler(ignoreCancelled = true)
    public void onPacket(PacketReceiveEvent event) {
        if (event.getPacket() instanceof ServerboundChatPreviewPacket packet){
            final Player sender = event.getPlayer();
            if (sender == null || MoeInfo.plugin.getMute(MMOCore.getPlayerData(sender)) > 0) return;//找不到玩家 或者玩家被禁言跳出

            String chat;
            try{
                chat = (String) getChat.get(packet);
            }catch (IllegalAccessException e){
                throw new RuntimeException(e);
            }
            Bukkit.getScheduler().runTaskAsynchronously(MoeInfo.plugin,() -> {
                final ClientboundChatPreviewPacket chatPreviewPacket = new ClientboundChatPreviewPacket(0,IChatBaseComponent.a(chat));
                for (Player player : Bukkit.getOnlinePlayers()) {
                    final PlayerConnection connection = EntityNetUtils.getPlayerConnection(EntityNetUtils.getNmsPlayer(player));
                    connection.a(chatPreviewPacket);
                }
            });
        }
    }
}
