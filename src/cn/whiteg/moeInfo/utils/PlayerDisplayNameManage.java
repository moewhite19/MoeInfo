package cn.whiteg.moeInfo.utils;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.moeInfo.MoeInfo;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Entity;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerDisplayNameManage {
    private static final String empty = "";

    public static void setPrefix(OfflinePlayer player,String perfix) {
        setPrefix(MMOCore.getPlayerData(player),perfix);
    }

    public static void setPrefix(DataCon dc,String perfix) {
        if (perfix == null || perfix.isEmpty()) perfix = "";
        perfix = ChatColor.translateAlternateColorCodes('&',perfix);
        dc.setString("Player.prefix",isEmpty(perfix) ? null : perfix);
    }

    public static String getPrefix(OfflinePlayer player) {
        return getPrefix(MMOCore.getPlayerData(player));
    }

    public static String getPrefix(DataCon dc) {
        return dc.getConfig().getString("Player.prefix",empty);
    }

    public static void setSuffix(DataCon pd,String suffix) {
        if (suffix == null || suffix.isEmpty()) suffix = "";
        suffix = ChatColor.translateAlternateColorCodes('&',suffix);
        pd.setString("Player.suffix",isEmpty(suffix) ? null : suffix);
    }

    public static void setSuffix(OfflinePlayer player,String suffix) {
        setSuffix(MMOCore.getPlayerData(player),suffix);
    }

    public static String getSuffix(OfflinePlayer player) {
        return getSuffix(MMOCore.getPlayerData(player));
    }

    public static String getSuffix(DataCon dc) {
        return dc.getConfig().getString("Player.suffix",empty);
    }

    public static void setNameColour(Player player,String namecore) {
        setNameColour(MMOCore.getPlayerData(player),namecore);
    }

    public static void setNameColour(DataCon dc,String namecore) {
        if (namecore == null || namecore.isEmpty()) namecore = "";
        namecore = ChatColor.translateAlternateColorCodes('&',namecore.replace("&k",""));
        dc.setString("Player.namecolour",namecore.isEmpty() ? null : namecore);
    }

    public static boolean isEmpty(String str) {
        return ChatColor.stripColor(str).isEmpty();
    }

    public static String getNameColour(Player player) {
        return getNameColour(MMOCore.getPlayerData(player));
    }

    public static String getNameColour(DataCon dc) {
        return dc.getConfig().getString("Player.namecolour",empty);
    }

    public static String upView(Player player) {
        DataCon dc = MMOCore.getPlayerData(player);
        return upView(player,dc);
    }

    public static String upView(Player player,DataCon dc) {
        if (player == null || !player.isOnline()) return "";
        final String prefix = getPrefix(dc);
        final String namecore = getNameColour(dc);
        final String suff = getSuffix(dc);
        final StringBuilder sb = new StringBuilder();
        if (!prefix.isEmpty()) sb.append(prefix);
        sb.append(namecore.isEmpty() ? "§r" : namecore);
        sb.append(player.getName());
        if (!suff.isEmpty()) sb.append(suff).append("§r");
        final String str = ChatColor.translateAlternateColorCodes('&',sb.toString());
        player.setDisplayName(str);
        player.setPlayerListName(str);
        return str;
    }

    public static BaseComponent[] getDisplayName(Player player,DataCon dc) {
        ComponentBuilder cb = new ComponentBuilder(player.getDisplayName());
//        cb.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text(player.getDisplayName() + (dc.getBoolean("Authenticate.Success") ? "\n§b§l已正版认证√" : "") + "\n§f点我发起私聊")));
        cb.event(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new Entity(player.getType().getKey().asString(), player.getUniqueId().toString(), new TextComponent(player.getName()))));
        cb.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/tell " + player.getName() + " "));
        return cb.create();
    }
}
