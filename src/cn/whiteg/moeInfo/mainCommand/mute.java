package cn.whiteg.moeInfo.mainCommand;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.mmocore.util.CommonUtils;
import cn.whiteg.moeInfo.MoeInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mute extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (!sender.hasPermission("mmo.mute")) return false;
        if (args.length > 1){
            DataCon dc = MMOCore.getPlayerData(args[1]);
            if (dc == null){
                sender.sendMessage("找不到玩家");
                return false;
            }
            long time = args.length > 2 ? CommonUtils.getTime(args[2]) : 0;
            if (time < 0){
                sender.sendMessage("无效时间");
                return false;
            }
            if (time == 0){
                dc.set(MoeInfo.settin.mutePath,null);
                sender.sendMessage(dc.getName() + "§b已被解除禁言");
                Player p = dc.getPlayer();
                if (p != null){
                    p.sendMessage("§b阁下已被解除禁言");
                }
                return true;
            } else {
                String ts = CommonUtils.tanMintoh(time);
                time = time + System.currentTimeMillis();
                dc.set(MoeInfo.settin.mutePath,time);
                Player p = dc.getPlayer();
                if (p != null){
                    p.sendMessage("§b阁下已被禁言§f" + ts);
                }
                sender.sendMessage(dc.getName() + "§b已被禁言§f" + ts);
                return true;

            }
        } else {
            sender.sendMessage("/mute <玩家ID> [时长] 来禁言玩家");
        }
        return false;
    }
}
