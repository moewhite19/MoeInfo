package cn.whiteg.moeInfo.Listener;

import cn.whiteg.moeInfo.MoeInfo;
import cn.whiteg.moeInfo.nms.SendBrand;
import cn.whiteg.moeInfo.utils.PlayerDisplayNameManage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class PlayerJoinMessage implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (MoeInfo.settin.NICKNAME) PlayerDisplayNameManage.upView(event.getPlayer());
//        event.joinMessage(ComponentUtils.ofString(MoeInfo.settin.PLAYER_JOIN_MESSAGE.replace("%player%",event.getPlayer().getDisplayName()))); //Paper方法
        event.setJoinMessage(MoeInfo.settin.PLAYER_JOIN_MESSAGE.replace("%player%",event.getPlayer().getDisplayName()));
        SendBrand.send(event.getPlayer(),MoeInfo.settin.f3info);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(MoeInfo.settin.PLAYER_QUIT_MESSAGE.replace("%player%",event.getPlayer().getDisplayName()));
//        event.quitMessage(ComponentUtils.ofString(MoeInfo.settin.PLAYER_QUIT_MESSAGE.replace("%player%",event.getPlayer().getDisplayName())));//Paper方法
//        if (MoeInfo.plugin.tabPlayerlistsTimer != null) MoeInfo.plugin.tabPlayerlistsTimer.getLocMap().remove(event.getPlayer().getName());
    }
}
