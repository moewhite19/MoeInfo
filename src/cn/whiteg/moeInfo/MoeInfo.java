package cn.whiteg.moeInfo;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.common.CommandManage;
import cn.whiteg.mmocore.common.PluginBase;
import cn.whiteg.moeInfo.Listener.ChatRecordListener;
import cn.whiteg.moeInfo.Listener.PlayerChatListener;
import cn.whiteg.moeInfo.Listener.PlayerJoinMessage;
import cn.whiteg.moeInfo.external.PlaceholdersHook;
import cn.whiteg.moeInfo.external.VaultChatHandler;
import cn.whiteg.moeInfo.external.VaultPermisson;
import cn.whiteg.moeInfo.nms.SendBrand;
import cn.whiteg.moeInfo.utils.MotdUtil;
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
    public TabPlayerListsTimer tabPlayerlistsTimer;
    public boolean memfree = false;
    public boolean moetp;
    public Economy economy;
    public ExternalManage externalManage;
    public PlaceholdersHook placeholdersHook;
    private VaultChatHandler chatHandler;
    private ChatRecordListener chatRecordListener;


    public void onLoad() {
        saveDefaultConfig();
    }

    public void onEnable() {
        logger = getLogger();
        console = Bukkit.getConsoleSender();
        plugin = this;
        settin = new Settin();
        memfree = Bukkit.getPluginManager().isPluginEnabled("MemFree");
        moetp = Bukkit.getPluginManager().getPlugin("MoeTP") != null;
        logger.info("开始注册事件");
        //regEven(new TabListListener());
        regListener(new PlayerJoinMessage());
        if (settin.REPACECHAT){
            regListener(new PlayerChatListener());
//            regListener(new ChatPreviewListener()); //聊天预览
        }
        //设置聊天记录事件
        if (settin.CHAT_RECORD > 0){
            if (chatRecordListener == null){
                chatRecordListener = new ChatRecordListener(settin.CHAT_RECORD);
                regListener(chatRecordListener);
            } else {
                chatRecordListener.setSize(settin.CHAT_RECORD);
            }
        } else if (chatRecordListener != null){
            unregListener(chatRecordListener);
        }

        mainCmd = new CommandManage(this);
        mainCmd.setExecutor();

        updatePlayerList();

        if (settin.PLAYERTAB) tabPlayerlistsTimer = new TabPlayerListsTimer(this);

        externalManage = new ExternalManage();
        regListener(externalManage);

        //去除进入服务器后无法验证聊天消息的弹窗
        if (settin.AntiPopup){
            try{
//                regListener(new AntiPopup());
            }catch (Exception e){
                e.printStackTrace();
            }
        }

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

        if (settin.SETMOTD){
            try{
                Bukkit.getScheduler().runTask(this,() -> {
                    MotdUtil.setMotdName();
                });
            }catch (Exception e){
                e.printStackTrace();
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
            MotdUtil.setMotdName();
        }
        if (tabPlayerlistsTimer != null){
            tabPlayerlistsTimer.close();
            tabPlayerlistsTimer = null;
        }
        if (settin.PLAYERTAB) tabPlayerlistsTimer = new TabPlayerListsTimer(this);
        updatePlayerList();
    }

    public long getMute(DataCon dc) {
        long mute = dc.getConfig().getLong(MoeInfo.settin.mutePath,0L);
        if (mute != 0){
            long now = System.currentTimeMillis();
            if (mute > now){
                return mute - now;
            } else {
                dc.set(MoeInfo.settin.mutePath,null);
            }
        }
        return 0;
    }

    public boolean hasPlaceholder() {
        return placeholdersHook != null;
    }

    public ChatRecordListener getChatRecordListener() {
        return chatRecordListener;
    }
}
