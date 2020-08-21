package cn.whiteg.moeInfo.external;

import cn.whiteg.moeInfo.utils.PlayerDisplayNameManage;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class PlaceholdersHook extends PlaceholderExpansion {

    public PlaceholdersHook() {

    }

    public String getIdentifier() {
        return "moeinfo";
    }

    public String getAuthor() {
        return "MoeWhite";
    }

    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    public String onRequest(final OfflinePlayer player,String identifier) {
        switch (identifier) {
            case "prefix": { //获取前缀
                return noNull(PlayerDisplayNameManage.getPrefix(player));
            }
            case "suffix": { //获取后缀
                return noNull(PlayerDisplayNameManage.getSuffix(player));
            }
        }
//        Player p = player.getPlayer();
//
//        if (p != null){
//            return p.getDisplayName();
//        }
        return null;
    }

    public String noNull(String str) {
        return str == null ? "" : str;
    }
}
