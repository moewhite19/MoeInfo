package cn.whiteg.moeInfo.api;

import cn.whiteg.mmocore.DataCon;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class WhoisMessageProvider {
    final JavaPlugin plugin;

    public WhoisMessageProvider(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isEnable() {
        if (plugin == null) return false;
        return plugin.isEnabled();
    }

    public abstract String getMsg(CommandSender player,DataCon dataCon);
}
