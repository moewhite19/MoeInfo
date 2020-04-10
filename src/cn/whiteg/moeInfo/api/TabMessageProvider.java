package cn.whiteg.moeInfo.api;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.moeInfo.MoeInfo;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class TabMessageProvider extends MessageProvider {
    public TabMessageProvider(JavaPlugin plugin) {
        super(plugin);
    }

    public abstract String getMsg(Player sender,DataCon dataCon);

    public void register() {
        MoeInfo.plugin.tabPlayerlistsTimer.regMeger(this);
    }
}
