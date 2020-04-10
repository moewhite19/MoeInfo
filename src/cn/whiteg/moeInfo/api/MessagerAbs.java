package cn.whiteg.moeInfo.api;

import cn.whiteg.mmocore.DataCon;
import org.bukkit.command.CommandSender;

public abstract class MessagerAbs {
    public abstract String getMsg(CommandSender player ,DataCon dataCon);
}
