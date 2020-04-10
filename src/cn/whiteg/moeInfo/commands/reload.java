package cn.whiteg.moeInfo.commands;

import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.moeInfo.MoeInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class reload extends HasCommandInterface {

    @Override
    public boolean executo(CommandSender sender,Command cmd,String label,String[] args) {
        MoeInfo.plugin.onReload();
        sender.sendMessage("重载完成");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        return null;
    }


    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("whiteg.test");
    }
}
