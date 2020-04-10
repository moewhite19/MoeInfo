package cn.whiteg.moeInfo.commands;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.moeInfo.MoeInfo;
import cn.whiteg.moeInfo.utils.PlayerDisplayNameManage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class namecolour extends HasCommandInterface {

    @Override
    public boolean executo(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            if (!(sender instanceof Player)){
                sender.sendMessage("§b只有玩家才能使用这条指令");
                return true;
            }
            String v1 = args[0];
            Player p = (Player) sender;
            DataCon dc = MMOCore.getPlayerData(p);
            if (v1.contains("&k") || v1.contains("&K")){
                sender.sendMessage("包含违规字符");
                return true;
            }
            if (v1.length() + sender.getName().length() + PlayerDisplayNameManage.getPrefix(dc).length() + PlayerDisplayNameManage.getSuffix(dc).length() > MoeInfo.settin.MAX_NAME_LENGTH){
                sender.sendMessage("长度不符合");
                return true;
            }
            Player player = (Player) sender;
            PlayerDisplayNameManage.setNameColour(player,v1);
            PlayerDisplayNameManage.upView(player);
            sender.sendMessage("§b名字颜色设置为r§r " + player.getDisplayName());
            return true;
        } else if (args.length == 2){
            if (!sender.hasPermission("whiteg.test")){
                sender.sendMessage("参数有误");
                return false;
            }
            String v1 = args[0];
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null){
                sender.sendMessage("玩家不在线");
                return true;
            }
            player.setDisplayName(v1);
            PlayerDisplayNameManage.setNameColour(player,v1);
            sender.sendMessage("§b成功将" + args[1] + "的名字颜色设置为为§r " + PlayerDisplayNameManage.upView(player));
            return true;
        }
        sender.sendMessage(getDescription());
        return true;
    }

    @Override
    public List<String> complete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            if (sender instanceof Player){
                String v1 = args[0];
                DataCon dc = MMOCore.getPlayerData(((Player) sender));
                if (v1.contains("&k") || v1.contains("&K")){
                    return Collections.singletonList("包含违规字符");
                }
                if (!sender.hasPermission("whiteg.cn") && v1.length() + sender.getName().length() + PlayerDisplayNameManage.getPrefix(dc).length() + PlayerDisplayNameManage.getSuffix(dc).length() > MoeInfo.settin.MAX_NAME_LENGTH){
                    return Collections.singletonList("长度不符合");
                }
                String nn = PlayerDisplayNameManage.getNameColour((Player) sender);
                if (nn == null) return null;
                nn = nn.replace('§','&');
                return Collections.singletonList(nn);
            }
        }
        return PlayersList(args);
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("mmo.namecolour");
    }

    @Override
    public String getDescription() {
        return "设置名字颜色:§7 <颜色代码>";
    }
}
