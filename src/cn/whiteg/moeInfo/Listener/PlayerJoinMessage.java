package cn.whiteg.moeInfo.Listener;

import cn.whiteg.moeInfo.MoeInfo;
import cn.whiteg.moeInfo.nms.SendBrand;
import cn.whiteg.moeInfo.utils.PlayerDisplayNameManage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.Random;


public class PlayerJoinMessage implements Listener {
    static Random RANDOM = new Random();
    static Component EMPTY = Component.text().asComponent();

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        //更新显示昵称
        if (MoeInfo.settin.NICKNAME) PlayerDisplayNameManage.upView(event.getPlayer());
        //加入广播
        final List<String> msgList = MoeInfo.settin.PLAYER_JOIN_MESSAGE;
        String msg = msgList.get(RANDOM.nextInt(msgList.size()));
        event.joinMessage(toComponent(msg,event.getPlayer(),NamedTextColor.AQUA));
        //发送F3调试信息
        SendBrand.send(event.getPlayer(),MoeInfo.settin.f3info);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        final List<String> msgList = MoeInfo.settin.PLAYER_QUIT_MESSAGE;
        String msg = msgList.get(RANDOM.nextInt(msgList.size()));
        event.quitMessage(toComponent(msg,event.getPlayer(),NamedTextColor.DARK_AQUA));
    }

    static Component toComponent(String msg,Player player,TextColor color) {
        int inxStart = msg.indexOf("%player%");
        if (inxStart < 0){
            return Component.text(msg);
        }
        int inxEnd = inxStart + 8;

        Component component;

        if (inxStart > 0){
            component = Component.text(msg.substring(0,inxStart)).color(color);
        }else component = EMPTY;
        component = component .append(player.displayName().hoverEvent(player).color(NamedTextColor.WHITE));

        if (msg.length() > inxEnd){
            component = component.append(Component.text(msg.substring(inxEnd)).color(color)).append(EMPTY);
        }else component = component.append(EMPTY);
        return component;
    }
}
