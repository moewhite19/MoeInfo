package cn.whiteg.moeInfo.api;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class MessageProvider {
    final JavaPlugin plugin;

    public MessageProvider(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isEnable() {
        return plugin != null && plugin.isEnabled();
    }
}
