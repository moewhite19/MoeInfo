package cn.whiteg.moeInfo.commands;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.mmocore.util.CommonUtils;
import cn.whiteg.moeInfo.MoeInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class mute extends HasCommandInterface {

    @Override
    public boolean executo(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length > 0){
            DataCon dc = MMOCore.getPlayerData(args[0]);
            if (dc == null){
                sender.sendMessage("找不到玩家");
                return false;
            }
            long time = args.length > 1 ? CommonUtils.getTime(args[1]) : 0;
            if (time <= 0){
                dc.set(MoeInfo.settin.mutePath,null);
                sender.sendMessage(dc.getName() + "§b已被解除禁言");
                Player p = dc.getPlayer();
                if (p != null){
                    p.sendMessage("§b阁下已被解除禁言");
                }
            } else {
                String ts = CommonUtils.tanMintoh(time);
                time = time + System.currentTimeMillis();
                dc.set(MoeInfo.settin.mutePath,time);
                Player p = dc.getPlayer();
                if (p != null){
                    p.sendMessage("§b阁下已被禁言§f" + ts);
                }
                sender.sendMessage(dc.getName() + "§b已被禁言§f" + ts);

            }
            return true;
        } else {
            sender.sendMessage(getDescription());
        }
        return false;
    }

    @Override
    public List<String> complete(CommandSender sender,Command cmd,String str,String[] args) {
        return getMatches(MMOCore.getLatelyPlayerList(),args);
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("mmo.mute");
    }

    @Override
    public String getDescription() {
        return "禁言玩家: §7 <玩家ID> [时长] §r来禁言玩家";
    }
}
