package cn.whiteg.moeInfo;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

public class Settin {
    public final String mutePath = "Player.mute";
    public final DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###.##");
    public boolean DEBUG;
    public String LISTHEAD;
    public String PLAYER_JOIN_MESSAGE;
    public String PLAYER_QUIT_MESSAGE;
    public int TAB_TIMER_INTERVAL;
    public int MAX_NAME_LENGTH;
    public short CHAT_RECORD;
    public String f3info;
    public boolean SETMOTD;
    public boolean NICKNAME;
    public boolean REPACECHAT;
    public boolean PLAYERTAB;
    public boolean ExternalVault;
    public boolean AntiPopup;
    public List<String> MuteCommands;
    private FileConfiguration config;

    public Settin() {
        reload();
    }

    @SuppressWarnings("all")
    public void reload() {
        config = YamlConfiguration.loadConfiguration(new File(MoeInfo.plugin.getDataFolder(),"config.yml"));
        DEBUG = config.getBoolean("debug");
        LISTHEAD = ChatColor.translateAlternateColorCodes('&',config.getString("PlayerListHeader","")).replace("\\n","\n");
        PLAYER_JOIN_MESSAGE = config.getString("PlayerJoinMessage","%player%&b突然出现");
        PLAYER_QUIT_MESSAGE = config.getString("PlayerQuitMessage","%player%&3又消失啦");
        TAB_TIMER_INTERVAL = config.getInt("TAB_TIMER_INTERVAL",2500);
        MAX_NAME_LENGTH = config.getInt("MaxNameLength",20);
        CHAT_RECORD = (short) Math.min(100,config.getInt("ChatRecord",0));
        f3info = config.getString("F3info","");
        SETMOTD = config.getBoolean("MOTD.Enable",false);
        NICKNAME = config.getBoolean("NickName",false);
        REPACECHAT = config.getBoolean("RepaceChat",false);
        PLAYERTAB = config.getBoolean("PlayerTabList",false);
        ExternalVault = config.getBoolean("ExternalVault",false);
        AntiPopup = config.getBoolean("AntiPopup" , true);
        MuteCommands = config.getStringList("MuteCommands");
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
