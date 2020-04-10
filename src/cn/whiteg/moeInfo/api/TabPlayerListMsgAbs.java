package cn.whiteg.moeInfo.api;

import cn.whiteg.mmocore.DataCon;
import org.bukkit.entity.Player;

public abstract class TabPlayerListMsgAbs {
    public abstract String getMsg(Player player,DataCon dataCon);
}
