package cn.whiteg.moeInfo;

import at.pcgamingfreaks.MarriageMaster.Bukkit.MarriageMaster;
import com.bekvon.bukkit.residence.Residence;
import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class ExternalManage implements Listener {
    public Residence residence;
    public MultiverseCore multiverseCore;
    public MarriageMaster marriageMaster;
//    public Economy Economy;
//    private VaultChatHandler VaultChatHandler;

    public ExternalManage() {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            load(plugin);
        }

//        for (Class<?> knownService : Bukkit.getServicesManager().getKnownServices()) {
//            RegisteredServiceProvider<?> p = Bukkit.getServicesManager().getRegistration(knownService);
//            if (p == null) continue;
////            load(p.getProvider());
//            MoeInfo.logger.info("检查代理" + knownService);
//        }
    }

    @EventHandler
    public void onPluginLoad(PluginEnableEvent event) {
        Plugin plugin = event.getPlugin();
        load(plugin);
    }

    @EventHandler
    public void onPluginUnload(PluginDisableEvent event) {
        Plugin plugin = event.getPlugin();
        unload(plugin);
    }

//    @EventHandler
//    public void onEnableServer(ServiceRegisterEvent event) {
//        load(event.getProvider());
//    }
//
//    @EventHandler
//    public void onEnableServer(ServiceUnregisterEvent event) {
//        load(event.getProvider());
//    }

    public void load(Plugin plugin) {
        try{
            switch (plugin.getName()) {
                case "Residence": {
                    residence = (Residence) plugin;
                    break;
                }
                case "MarriageMaster": {
                    marriageMaster = (MarriageMaster) plugin;
                    break;
                }
                case "Multiverse-Core": {
                    multiverseCore = (MultiverseCore) plugin;
                    break;
                }
            }
        }catch (ClassCastException e){
            MoeInfo.logger.warning("无法Hook重载的插件: " + plugin.getName());
        }catch (Exception e){
            e.printStackTrace();
        }

//        Field[] fs = getClass().getFields();
//        for (Field f : fs) {
//            try{
//                if (f.getType().equals(plugin.getClass())){
//                    f.set(this,plugin);
//                    MoeInfo.logger.info("已设置Field" + f);
//                }
//            }catch (Throwable e){
//                MoeInfo.logger.info("无效Field" + f);
//            }
//        }
    }

    public void unload(Plugin plugin) {
        switch (plugin.getName()) {
            case "Residence": {
                residence = null;
                break;
            }
            case "MarriageMaster": {
                marriageMaster = null;
                break;
            }
            case "Multiverse-Core": {
                multiverseCore = null;
                break;
            }
        }
//
//        for (Field field : getClass().getFields()) {
//            try{
//                Object p = field.get(this);
//                if (plugin == p) field.set(this,null);
//            }catch (IllegalAccessException e){
//                e.printStackTrace();
//            }
//        }
    }
}
