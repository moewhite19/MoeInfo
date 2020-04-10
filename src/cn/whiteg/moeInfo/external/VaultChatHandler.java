package cn.whiteg.moeInfo.external;

import cn.whiteg.moeInfo.utils.PlayerDisplayNameManage;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;

public class VaultChatHandler extends Chat {
    public VaultChatHandler(Permission perms) {
        super(perms);
    }

    @Override
    public String getName() {
        return "MoeInfo";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * @param s
     * @param s1
     * @deprecated
     */
    @Override
    public String getPlayerPrefix(String s,String s1) {
        return null;
    }

    /**
     * @param s
     * @param s1
     * @param s2
     * @deprecated
     */
    @Override
    public void setPlayerPrefix(String s,String s1,String s2) {

    }

    /**
     * @param s
     * @param s1
     * @deprecated
     */
    @Override
    public String getPlayerSuffix(String s,String s1) {
        return null;
    }

    /**
     * @param s
     * @param s1
     * @param s2
     * @deprecated
     */
    @Override
    public void setPlayerSuffix(String s,String s1,String s2) {

    }

    @Override
    public String getGroupPrefix(String s,String s1) {
        return null;
    }

    @Override
    public void setGroupPrefix(String s,String s1,String s2) {

    }

    @Override
    public String getGroupSuffix(String s,String s1) {
        return null;
    }

    @Override
    public void setGroupSuffix(String s,String s1,String s2) {

    }

    /**
     * @param s
     * @param s1
     * @param s2
     * @param i
     * @deprecated
     */
    @Override
    public int getPlayerInfoInteger(String s,String s1,String s2,int i) {
        return 0;
    }

    /**
     * @param s
     * @param s1
     * @param s2
     * @param i
     * @deprecated
     */
    @Override
    public void setPlayerInfoInteger(String s,String s1,String s2,int i) {

    }

    @Override
    public int getGroupInfoInteger(String s,String s1,String s2,int i) {
        return 0;
    }

    @Override
    public void setGroupInfoInteger(String s,String s1,String s2,int i) {

    }

    /**
     * @param s
     * @param s1
     * @param s2
     * @param v
     * @deprecated
     */
    @Override
    public double getPlayerInfoDouble(String s,String s1,String s2,double v) {
        return 0;
    }

    /**
     * @param s
     * @param s1
     * @param s2
     * @param v
     * @deprecated
     */
    @Override
    public void setPlayerInfoDouble(String s,String s1,String s2,double v) {

    }

    @Override
    public double getGroupInfoDouble(String s,String s1,String s2,double v) {
        return 0;
    }

    @Override
    public void setGroupInfoDouble(String s,String s1,String s2,double v) {

    }

    /**
     * @param s
     * @param s1
     * @param s2
     * @param b
     * @deprecated
     */
    @Override
    public boolean getPlayerInfoBoolean(String s,String s1,String s2,boolean b) {
        return false;
    }

    /**
     * @param s
     * @param s1
     * @param s2
     * @param b
     * @deprecated
     */
    @Override
    public void setPlayerInfoBoolean(String s,String s1,String s2,boolean b) {

    }

    @Override
    public boolean getGroupInfoBoolean(String s,String s1,String s2,boolean b) {
        return false;
    }

    @Override
    public void setGroupInfoBoolean(String s,String s1,String s2,boolean b) {

    }

    /**
     * @param s
     * @param s1
     * @param s2
     * @param s3
     * @deprecated
     */
    @Override
    public String getPlayerInfoString(String s,String s1,String s2,String s3) {
        return null;
    }

    /**
     * @param s
     * @param s1
     * @param s2
     * @param s3
     * @deprecated
     */
    @Override
    public void setPlayerInfoString(String s,String s1,String s2,String s3) {

    }

    @Override
    public String getGroupInfoString(String s,String s1,String s2,String s3) {
        return null;
    }

    @Override
    public void setGroupInfoString(String s,String s1,String s2,String s3) {

    }

    @Override
    public String getPlayerSuffix(Player player) {
        return PlayerDisplayNameManage.getSuffix(player);
    }

    @Override
    public void setPlayerSuffix(Player player,String suffix) {
        PlayerDisplayNameManage.setPrefix(player,suffix);
    }

    @Override
    public void setPlayerPrefix(Player player,String prefix) {
        PlayerDisplayNameManage.setPrefix(player,prefix);
    }
}
