package cn.whiteg.moeInfo.api;

import cn.whiteg.mmocore.DataCon;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class WhoisMessageProvider extends MessageProvider{
    public WhoisMessageProvider(JavaPlugin plugin) {
        super(plugin);
    }

    public abstract String getMsg(CommandSender sender,DataCon dataCon);
}
