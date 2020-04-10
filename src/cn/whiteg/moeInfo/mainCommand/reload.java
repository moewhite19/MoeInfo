package cn.whiteg.moeInfo.mainCommand;

import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.moeInfo.MoeInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class reload extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (sender.hasPermission("moeinfo.reload")){
            MoeInfo.plugin.onReload();
            sender.sendMessage("重载完成");
            return true;
        } else {
            sender.sendMessage("§b权限不足");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        return null;
    }
}
