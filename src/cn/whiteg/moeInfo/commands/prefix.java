package cn.whiteg.moeInfo.commands;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.moeInfo.MoeInfo;
import cn.whiteg.moeInfo.utils.PlayerDisplayNameManage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class prefix extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (!sender.hasPermission("mmo.prefix")) return false;
        if (args.length == 1){
            if (!(sender instanceof Player)){
                sender.sendMessage("§b只有玩家才能使用这条指令");
                return true;
            }
            Player p = (Player) sender;
            DataCon dc = MMOCore.getPlayerData(p);
            String v1 = args[0];
            if (v1.contains("&k") || v1.contains("&K")){
                sender.sendMessage("包含违规字符");
                return true;
            }
            if (v1.length() + sender.getName().length() + PlayerDisplayNameManage.getSuffix(dc).length() + PlayerDisplayNameManage.getNameColour(dc).length() > MoeInfo.settin.MAX_NAME_LENGTH){
                sender.sendMessage("长度不符合");
                return true;
            }
            Player player = (Player) sender;
            PlayerDisplayNameManage.setPrefix(player,v1);
            //MMOCore.getPlayerData((Player) sender).set("Player.prefix",v1);
            PlayerDisplayNameManage.upView(player);
            sender.sendMessage("§b前缀设置为§r " + player.getDisplayName());
            return true;
        } else if (args.length == 2){
            if (!sender.hasPermission("whiteg.test")){
                sender.sendMessage("格式有误或者阁下没有权限");
                return false;
            }
            String v1 = args[0];
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null){
                sender.sendMessage("玩家不在线");
                return true;
            }
            player.setDisplayName(v1);
            PlayerDisplayNameManage.setPrefix(player,v1);
            PlayerDisplayNameManage.upView(player);
            sender.sendMessage("§b成功将" + args[0] + "的昵称设置为§r " + player.getDisplayName());
            return true;
        }
        sender.sendMessage(getDescription());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (!sender.hasPermission("mmo.prefix")) return null;
        if (args.length == 1){
            if (sender instanceof Player){
                String v1 = args[0];
                DataCon dc = MMOCore.getPlayerData(((Player) sender));
                if (v1.contains("&k") || v1.contains("&K")){
                    return Collections.singletonList("包含违规字符");
                }
                if (v1.length() + sender.getName().length() + PlayerDisplayNameManage.getSuffix(dc).length() + PlayerDisplayNameManage.getNameColour(dc).length() > MoeInfo.settin.MAX_NAME_LENGTH){
                    return Collections.singletonList("长度不符合");
                }
                String nn = PlayerDisplayNameManage.getPrefix((Player) sender);
                if (nn == null) return null;
                nn = nn.replace('§','&');
                return Collections.singletonList(nn);
            }
        }
//        if (args.length == 3){
//            Player player = Bukkit.getPlayerExact(args[2]);
//            if (player == null) return null;
//            String nn = PlayerDisplayNameManage.getPrefix(player);
//            if (nn != null) nn = nn.replace("§","&");
//            return Collections.singletonList(nn);
//        }
        return PlayersList(args);
    }

    @Override
    public String getDescription() {
        return "设置昵称前缀:§7 <前缀>";
    }
}
