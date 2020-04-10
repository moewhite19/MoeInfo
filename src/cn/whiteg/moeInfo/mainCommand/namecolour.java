package cn.whiteg.moeInfo.mainCommand;

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

public class namecolour extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (!sender.hasPermission("mmo.namecolour")) return false;
        if (args.length == 2){
            if (!(sender instanceof Player)){
                sender.sendMessage("§b只有玩家才能使用这条指令");
                return true;
            }
            String v1 = args[1];
            Player p = (Player) sender;
            DataCon dc = MMOCore.getPlayerData(p);
            if (v1.length() + sender.getName().length() + PlayerDisplayNameManage.getPrefix(dc).length() + PlayerDisplayNameManage.getSuffix(dc).length() > MoeInfo.settin.MAX_NAME_LENGTH){
                sender.sendMessage("长度不符合");
                return true;
            }
            if (v1.toLowerCase().contains("&k")){
                sender.sendMessage("包含违规字符");
                return true;
            }
            Player player = (Player) sender;
            PlayerDisplayNameManage.setNameColour(player,v1);
            PlayerDisplayNameManage.upView(player);
            sender.sendMessage("§b名字颜色设置为r§r " + player.getDisplayName());
            return true;
        } else if (args.length == 3){
            if (!sender.hasPermission("whiteg.test")){
                sender.sendMessage("阁下没有权限");
                return false;
            }
            String v1 = args[1];
            Player player = Bukkit.getPlayer(args[2]);
            if (player == null){
                sender.sendMessage("玩家不在线");
                return true;
            }
            player.setDisplayName(v1);
            PlayerDisplayNameManage.setNameColour(player,v1);
            PlayerDisplayNameManage.upView(player);
            sender.sendMessage("§b成功将" + args[1] + "的名字颜色设置为为§r " + player.getDisplayName());
            return true;
        } /*else if (args.length == 4){
            if (!sender.hasPermission("whiteg.test")){
                sender.sendMessage("阁下没有权限");
                return true;
            }
            String v1 = args[1];
            String v2 = args[2];
            Player player = Bukkit.getPlayer(args[3]);
            if (player == null){
                sender.sendMessage("玩家不在线");
                return true;
            }
            PlayerDisplayNameManage.setPrefix(player,v1);
            PlayerDisplayNameManage.setNamecore(player,v2);
            PlayerDisplayNameManage.upView(player);
            sender.sendMessage("§b成功将" + args[1] + "的设置昵称为§r " + player.getDisplayName());
            return true;
        } else if (args.length == 5){
            if (!sender.hasPermission("whiteg.test")){
                sender.sendMessage("阁下没有权限");
                return true;
            }
            String v1 = args[1];
            String v2 = args[2];
            String v3 = args[3];
            Player player = Bukkit.getPlayer(args[4]);
            if (player == null){
                sender.sendMessage("玩家不在线");
                return true;
            }
            PlayerDisplayNameManage.setPrefix(player,v1);
            PlayerDisplayNameManage.setNamecore(player,v2);
            PlayerDisplayNameManage.setSuffix(player,v3);

            //PlayerDisplayNameManage.setPrefix(player,v1,v2,v3);
            sender.sendMessage("§b成功将" + args[1] + "的设置昵称为§r " + player.getDisplayName());
            return true;
        }*/
        sender.sendMessage("/namecolour <颜色代码> 来设置名字颜色");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (!sender.hasPermission("mmo.namecolour")) return null;
        if (args.length == 2){
            if (sender instanceof Player){
                String v1 = args[1];
                if (v1.length() + sender.getName().length() > 20){
                    return Collections.singletonList("长度不符合");
                } else if (v1.toLowerCase().contains("&k")){
                    return Collections.singletonList("包含违规字符");
                } else {
                    String nn = PlayerDisplayNameManage.getNameColour((Player) sender);
                    if (nn == null) return null;
                    nn = nn.replace('§','&');
                    return Collections.singletonList(nn);
                }
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
}
