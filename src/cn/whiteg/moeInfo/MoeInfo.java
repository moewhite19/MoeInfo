package cn.whiteg.moeInfo;

import cn.whiteg.mmocore.common.CommandManage;
import cn.whiteg.mmocore.common.PluginBase;
import cn.whiteg.moeInfo.Listener.PlayerChatListener;
import cn.whiteg.moeInfo.Listener.PlayerJoinMessage;
import cn.whiteg.moeInfo.external.PlaceholdersHook;
import cn.whiteg.moeInfo.external.VaultChatHandler;
import cn.whiteg.moeInfo.external.VaultPermisson;
import cn.whiteg.moeInfo.nms.SendBrand;
import cn.whiteg.moeInfo.utils.MotdMan;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

import java.util.logging.Logger;


public class MoeInfo extends PluginBase {
    public static Logger logger;
    public static MoeInfo plugin;
    public static Settin settin;
    public CommandManage mainCmd;
    public ConsoleCommandSender console;
    public TabPlayerlistsTimer tabPlayerlistsTimer;
    public boolean memfree = false;
    public boolean moetp;
    public Economy economy;
    public ExternalManage externalManage;
    public PlaceholdersHook placeholdersHook;
    private VaultChatHandler chatHandler;


    public void onLoad() {
        saveDefaultConfig();
    }

    public void onEnable() {
        logger = getLogger();
        console = Bukkit.getConsoleSender();
        plugin = this;
        settin = new Settin();
        if (settin.SETMOTD){
            MotdMan.setMotdName();
        }
        memfree = Bukkit.getPluginManager().isPluginEnabled("MemFree");
        moetp = Bukkit.getPluginManager().getPlugin("MoeTP") != null;
        logger.info("开始注册事件");
        //regEven(new TabListListener());
        regListener(new PlayerJoinMessage());
        if (settin.REPACECHAT){
            regListener(new PlayerChatListener());
        }

        mainCmd = new CommandManage(this);
        mainCmd.setExecutor();

        updatePlayerList();

        if (settin.PLAYERTAB) tabPlayerlistsTimer = new TabPlayerlistsTimer(this);

        externalManage = new ExternalManage();
        regListener(externalManage);

        String p;
        p = "Vault";
        if (Bukkit.getPluginManager().isPluginEnabled(p)){
            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
            if (economyProvider != null){
                this.economy = economyProvider.getProvider();
            }
            if (settin.ExternalVault){
                chatHandler = new VaultChatHandler(new VaultPermisson());
                this.getServer().getServicesManager().register(Chat.class,chatHandler,this,ServicePriority.Highest);
                logger.info("注册Vault聊天服务");
            }

        }
        p = "PlaceholderAPI";
        if (Bukkit.getPluginManager().isPluginEnabled(p)){
            placeholdersHook = new PlaceholdersHook();
            if (placeholdersHook.register()){
                logger.info("PlaceholdersAPI Hook成功");
            } else {
                logger.info("PlaceholdersAPI Hook失败");
            }
        }
    }

    public void updatePlayerList() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            SendBrand.send(p,MoeInfo.settin.f3info);
        }
    }

    public void onDisable() {
        //注销事件
        unregListener();
        logger.info("插件已关闭");
        if (chatHandler != null){
            this.getServer().getServicesManager().unregister(chatHandler);
            logger.info("注销Vault聊天前后缀服务");
        }
        if (tabPlayerlistsTimer != null){
            tabPlayerlistsTimer.close();
            tabPlayerlistsTimer = null;
        }
        if (placeholdersHook != null){
            placeholdersHook.unregister();
        }
    }

    public void onReload() {
        settin.reload();
        if (settin.SETMOTD){
            MotdMan.setMotdName();
        }
        if (tabPlayerlistsTimer != null){
            tabPlayerlistsTimer.close();
            tabPlayerlistsTimer = null;
        }
        if (settin.PLAYERTAB) tabPlayerlistsTimer = new TabPlayerlistsTimer(this);
        updatePlayerList();
    }

    public boolean hasPlaceholder() {
        return placeholdersHook != null;
    }
}
