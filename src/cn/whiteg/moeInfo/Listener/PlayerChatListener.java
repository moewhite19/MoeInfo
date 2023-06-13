package cn.whiteg.moeInfo.Listener;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.util.CommonUtils;
import cn.whiteg.moeInfo.MoeInfo;
import cn.whiteg.moeInfo.utils.LocationUtil;
import cn.whiteg.moeInfo.utils.PlayerDisplayNameManage;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.sun.jna.platform.win32.WinDef;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.event.player.ChatEvent;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
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

    public void onCha(AsyncChatEvent event) {
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        final String msg = event.getMessage();
        if (msg.startsWith("/")) return;
        final Player sender = event.getPlayer();
        final DataCon dc = MMOCore.getPlayerData(sender);
        //额外对象
//        final String worldname = LocationUtil.getWorldDisplayName(event.getPlayer().getWorld());
//        final ClaimedResidence res = LocationUtil.getRes(loc);
        ComponentBuilder cb = new ComponentBuilder("<");
        cb.append(PlayerDisplayNameManage.getDisplayName(sender,dc));
        //世界名字
//        final ComponentBuilder cb = new ComponentBuilder("§7[" + worldname + "§7]");
//        cb.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("§f点我发送传送指令\n").append("§b所在世界§3:" + worldname).create()));
//        cb.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/tpa " + sender.getName()));
//        if (res != null){
//            cb.append(new StringBuilder().append("§7[§6").append(res.getName()).append("§7]").toString());
//            cb.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/res tp " + res.getName()));
//            if (res.getEnterMessage() != null){
//                cb.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(new StringBuilder().append("§b领地§6\n").append(res.getName()).append("\n\n§f").append(ChatColor.translateAlternateColorCodes('&',res.getEnterMessage()).replace("%residence",res.getName()).replace("%owner",res.getOwner())).append("§f\n点我传送至领地").toString()).create()));
//            }
//        }
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
//        cb.append(PlayerDisplayNameManage.getDisplayName(sender,dc));
//        cb.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text("发起私聊")));
//        cb.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/tell " + sender.getName() + " "));
//        cb.append("§r: " + msg);
        cb.append("§r>").reset();

        //伴侣
        if (MoeInfo.plugin.externalManage.marriageMaster != null){
            final String mar = MoeInfo.plugin.externalManage.marriageMaster.DB.GetPartner(sender.getName());
            if (mar != null){
                cb.append("§l§c❤")
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text(mar)));
            } else {
                cb.append("§l§8❤")
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text("§b无伴侣，点我请求成为伴侣")))
                        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/marry " + sender.getName()));
            }
        }

        cb.append(" ").reset();

        //消息内容
        cb.append(msg).reset();
        cb.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,msg));
        cb.event((HoverEvent) null);


        final BaseComponent[] finalMessage = cb.create();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.spigot().sendMessage(ChatMessageType.CHAT,sender.getUniqueId(),finalMessage);
        }

        final StringBuilder sb = new StringBuilder();
        for (BaseComponent baseComponent : finalMessage) sb.append(baseComponent.toLegacyText());
        final String messageString = sb.toString();
        final ChatRecordListener recordListener = MoeInfo.plugin.getChatRecordListener();
        //向控制台发送消息
        MoeInfo.plugin.console.sendMessage(messageString);
        //记录消息
        if (recordListener != null){
            recordListener.putMessage(Component.text(messageString));
        }
        event.setCancelled(true);
    }

//    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
//    public void onChatCancelled(AsyncPlayerChatEvent event) {
//        event.setCancelled(true);
//    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMuteCheck(AsyncPlayerChatEvent event) {
        DataCon dc = MMOCore.getPlayerData(event.getPlayer());
        long mute = MoeInfo.plugin.getMute(dc);
        if (mute > 0){
            event.setCancelled(true);
            event.getPlayer().sendMessage("§b阁下当前还在禁言状态哦,剩余时间§f" + CommonUtils.tanMintoh(mute));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMuteCommand(PlayerCommandPreprocessEvent event) {
        DataCon dc = MMOCore.getPlayerData(event.getPlayer());
        final long mute = MoeInfo.plugin.getMute(dc);
        if (mute > 0){
            String cmd = event.getMessage().substring(1).toLowerCase();
            for (String muteCommand : MoeInfo.settin.MuteCommands) {
                if (cmd.startsWith(muteCommand)){
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("§b阁下当前还在禁言状态哦,剩余时间§f" + CommonUtils.tanMintoh(mute));
                    return;
                }
            }
        }
    }
}
