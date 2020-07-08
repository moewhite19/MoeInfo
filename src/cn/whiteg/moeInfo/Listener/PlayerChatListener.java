package cn.whiteg.moeInfo.Listener;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.util.CommonUtils;
import cn.whiteg.moeInfo.MoeInfo;
import cn.whiteg.moeInfo.utils.LocationUtil;
import cn.whiteg.moeInfo.utils.PlayerDisplayNameManage;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerChatListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        final String msg = event.getMessage();
        if (msg.startsWith("/")) return;
        final Player sender = event.getPlayer();
        final DataCon dc = MMOCore.getPlayerData(sender);
        final Location loc = sender.getLocation();
        final String worldname = LocationUtil.getWorldDisplayName(event.getPlayer().getWorld());
        final ClaimedResidence res = LocationUtil.getRes(loc);
        //世界名字
        final ComponentBuilder cb = new ComponentBuilder("§7[" + worldname + "§7]");
        cb.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("§f点我发送传送指令\n").append("§b所在世界§3:" + worldname).create()));
        cb.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/tpa " + sender.getName()));
        if (res != null){
            cb.append(new StringBuilder().append("§7[§6").append(res.getName()).append("§7]").toString());
            cb.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/res tp " + res.getName()));
            if (res.getEnterMessage() != null){
                cb.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(new StringBuilder().append("§b领地§6\n").append(res.getName()).append("\n\n§f").append(ChatColor.translateAlternateColorCodes('&',res.getEnterMessage()).replace("%residence",res.getName()).replace("%owner",res.getOwner())).append("§f\n点我传送至领地").toString()).create()));
            }
        }
/*        //教会
        if (MoeInfo.plugin.moeChurch != null){
            final Churoch cur = MoeInfo.plugin.moeChurch.getChurchManage().getPlayerChurch(sender.getName());
            if (cur != null){
                cb.append("§r" + event.getPlayer().getDisplayName() + ".");
                cb.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(cur.getId()).create()));
                cb.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/tell " + sender.getName() + " "));
            }
        }*/
        //昵称
        cb.append(PlayerDisplayNameManage.getDisplayName(sender,dc));
        cb.append("§r: " + msg);
        //消息内容
        cb.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,msg));
        cb.event((HoverEvent) null);
        final BaseComponent[] bc = cb.create();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.spigot().sendMessage(ChatMessageType.CHAT,bc);
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bc.length; i++) {
            sb.append(bc[i].toLegacyText());
        }
        MoeInfo.plugin.console.sendMessage(sb.toString());
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMuteCheck(AsyncPlayerChatEvent event) {
        DataCon dc = MMOCore.getPlayerData(event.getPlayer());
        long mute = dc.getConfig().getLong(MoeInfo.settin.mutePath,0L);
        if (mute != 0){
            long now = System.currentTimeMillis();
            if (mute > now){
                event.setCancelled(true);
                event.getPlayer().sendMessage("§b阁下当前还在禁言状态哦,剩余时间§f" + CommonUtils.tanMintoh(mute - now));
            } else {
                dc.set(MoeInfo.settin.mutePath,null);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMuteCommand(PlayerCommandPreprocessEvent event) {
        DataCon dc = MMOCore.getPlayerData(event.getPlayer());
        long mute = dc.getConfig().getLong(MoeInfo.settin.mutePath,0L);
        if (mute != 0){
            long now = System.currentTimeMillis();
            if (mute > now){
                String cmd = event.getMessage().substring(1).toLowerCase();
                for (String muteCommand : MoeInfo.settin.MuteCommands) {
                    if (cmd.startsWith(muteCommand)){
                        event.setCancelled(true);
                        event.getPlayer().sendMessage("§b阁下当前还在禁言状态哦,剩余时间§f" + CommonUtils.tanMintoh(mute - now));
                        return;
                    }
                }
            } else {
                dc.set(MoeInfo.settin.mutePath,null);
            }
        }

    }
}
