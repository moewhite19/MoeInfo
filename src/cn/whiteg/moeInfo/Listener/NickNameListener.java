package cn.whiteg.moeInfo.Listener;

import cn.whiteg.moeInfo.utils.PlayerDisplayNameManage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NickNameListener implements Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerDisplayNameManage.upView(event.getPlayer());
    }
}
