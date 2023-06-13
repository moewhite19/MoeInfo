package cn.whiteg.moeInfo.Listener;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.BroadcastMessageEvent;

import java.util.LinkedList;

public class ChatRecordListener implements Listener {
    short size;
    final LinkedList<Component> list;

    public ChatRecordListener(int size) {
        if (size > Short.MAX_VALUE) size = Short.MAX_VALUE;//最大100条
        else if (size <= 0) throw new IllegalArgumentException("size Cannot <=0");
        this.size = (short) size;
        list = new LinkedList<>();
    }

/*    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onChat(AsyncChatEvent event) {
        putMessage(event.message());
    }*/

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBroadcast(BroadcastMessageEvent event) {
        putMessage(event.message());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        putMessage(event.deathMessage());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        putMessage(event.quitMessage());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        //当玩家加入时发送聊天记录，并收集加入消息
        synchronized (list) {
            if (!list.isEmpty()){
                list.forEach(component -> {
                    event.getPlayer().sendMessage(component);
                });
                event.getPlayer().sendMessage("§8------------§7以上为历史消息§8------------");
            }
            putMessage(event.joinMessage());
        }
    }

    //收集消息
    public void putMessage(Component component) {
        synchronized (list) {
            list.addLast(component);
            while (list.size() > size) list.removeFirst(); //删除多余的消息
        }
    }

    public void setSize(int size) {
        if (size <= 0) HandlerList.unregisterAll(this);
        this.size = (short) Math.min(Short.MAX_VALUE,size);
    }
}
