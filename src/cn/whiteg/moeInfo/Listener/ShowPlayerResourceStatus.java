package cn.whiteg.moeInfo.Listener;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.moeInfo.api.WhoisMessageProvider;
import cn.whiteg.moeInfo.commands.whois;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ShowPlayerResourceStatus extends WhoisMessageProvider implements Listener {
    final static String path = "Player.ResourcePackStatus";

    public ShowPlayerResourceStatus(JavaPlugin plugin) {
        super(plugin);
        whois.regMessager(this);
    }

    @EventHandler
    public void onStatus(PlayerResourcePackStatusEvent event) {
        PlayerResourcePackStatusEvent.Status status = event.getStatus();
        String msg = statusMsg(status);
        DataCon dc = MMOCore.getPlayerData(event.getPlayer());
        dc.set(path,msg);
    }

    String statusMsg(PlayerResourcePackStatusEvent.Status status) {
        switch (status) {
            case ACCEPTED:
                return "接受";
            case DECLINED:
                return "拒绝";
            case FAILED_DOWNLOAD:
                return "下载失败";
            case SUCCESSFULLY_LOADED:
                return "已加载";
        }
        return status.name();
    }

    @Override
    public String getMsg(CommandSender player,DataCon dataCon) {
        String msg = dataCon.getString(path);
        if (msg == null) return null;
        return "§b资源包状态:§f " + msg;
    }
}
