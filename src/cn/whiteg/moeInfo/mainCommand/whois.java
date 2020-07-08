package cn.whiteg.moeInfo.mainCommand;

import at.pcgamingfreaks.MarriageMaster.Bukkit.Databases.Database;
import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.moeEco.VaultHandler;
import cn.whiteg.moeInfo.MoeInfo;
import cn.whiteg.moeInfo.api.MessagerAbs;
import cn.whiteg.moeInfo.utils.PlayerDisplayNameManage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

import static cn.whiteg.moeInfo.MoeInfo.plugin;

public class whois extends CommandInterface {

    private static Set<MessagerAbs> msgers = new LinkedHashSet<>();
    final SimpleDateFormat timeform = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public whois() {
        regMessager(new MessagerAbs() {
            @Override
            public String getMsg(CommandSender p,DataCon dc) {
                StringBuilder sb = new StringBuilder("§bID: §f");
                sb.append(dc.getName());
                if (dc.getConfig().getBoolean("Authenticate.Success",false)){
                    sb.append("    §b§l已正版验证");
                }
                return sb.toString();
            }
        });
        regMessager(new MessagerAbs() {
            @Override
            public String getMsg(CommandSender p,DataCon dc) {
                return "§bUUID: §f" + dc.getUUID().toString();
            }
        });
        regMessager(new MessagerAbs() {
            @Override
            public String getMsg(CommandSender p,DataCon dc) {
                String str = PlayerDisplayNameManage.getPrefix(dc);
                return str.isEmpty() ? null : ("§b前缀: §f" + ChatColor.translateAlternateColorCodes('&',str));
            }
        });
        regMessager(new MessagerAbs() {
            @Override
            public String getMsg(CommandSender p,DataCon dc) {
                String str = PlayerDisplayNameManage.getSuffix(dc);
                return str.isEmpty() ? null : ("§b后缀: §f" + ChatColor.translateAlternateColorCodes('&',str));
            }
        });
        regMessager(new MessagerAbs() {
            @Override
            public String getMsg(CommandSender p,DataCon dc) {
                long l = Long.valueOf(dc.getString("Player.join_time","0"));
                if (l == 0) return null;
                Date date = new Date(l);
                return ("§b加入时间: §f" + timeform.format(date));
            }
        });
        regMessager(new MessagerAbs() {
            @Override
            public String getMsg(CommandSender p,DataCon dc) {
                if (p == null){
                    long l = Long.valueOf(dc.getString("Player.login_time","0"));
                    if (l == 0) return null;
                    Date date = new Date(l);
                    return "§b最后登录时间: §f" + timeform.format(date);
                }
                return null;
            }
        });

        regMessager(new MessagerAbs() {
            @Override
            public String getMsg(CommandSender p,DataCon dc) {
                if (plugin.economy != null){
                    if (plugin.economy instanceof VaultHandler){
                        return new StringBuilder().append("§b").append(plugin.economy.currencyNameSingular()).append(":§f").append(String.format("%.1f",((VaultHandler) plugin.economy).getBalance(dc))).toString();
                    } else {
                        return new StringBuilder().append("§b").append(plugin.economy.currencyNameSingular()).append(":§f").append(String.format("%.1f",plugin.economy.getBalance(p.getName()))).toString();
                    }
                }
                return null;
            }
        });

        regMessager(new MessagerAbs() {
            @Override
            public String getMsg(CommandSender sender,DataCon dc) {
                if (plugin.externalManage.marriageMaster == null){
                    return null;
                }
                Database db = MoeInfo.plugin.externalManage.marriageMaster.DB;
                final String mar = db.GetPartner(dc.getName());
                return mar == null ? null : new StringBuilder("§b伴侣:§f").append(mar).toString();
            }
        });

        regMessager(new MessagerAbs() {

            @Override
            public String getMsg(CommandSender sender,DataCon dc) {
                String pat = "Player.qqid";
                long id = dc.getConfig().getLong(pat,0);
                if (id == 0) return null;
                return "§bQQ:§f " + id;
//                Plugin plugin = Bukkit.getPluginManager().getPlugin("MoeQbot");
//                if (plugin != null){
//                    MoeQbot qbot = (MoeQbot) plugin;
//                    Long qqid = qbot.getBindingManage().getQQID(dc.getName());
//                    if (qqid == null) return null;
//                    return "§bQQ:§f " + qqid;
//                }
//                return null;
            }
        });

        regMessager(new MessagerAbs() {
            @Override
            public String getMsg(CommandSender p,DataCon dc) {
                if (!p.hasPermission("whiteg.test")) return null;
                String ip = dc.getString("Player.latest_login_ip");
                if (ip != null){
                    return "§b最后登录IP:§f " + ip;
                }
                return null;
            }
        });
        regMessager(new MessagerAbs() {
            @Override
            public String getMsg(CommandSender sender,DataCon dc) {
                if (!sender.hasPermission("whiteg.test")) return null;
                Player p = dc.getPlayer();
                if (p != null){
                    Location loc = p.getLocation();
                    StringBuilder sb = new StringBuilder();
                    sb.append("§b位置:§f " + (plugin.externalManage.multiverseCore == null ? p.getWorld().getName() : plugin.externalManage.multiverseCore.getMVWorldManager().getMVWorld(p.getWorld()).getAlias()) + "§fX:" + loc.getBlockX() + " Y:" + loc.getBlockY() + " Z:" + loc.getBlockZ());
                    sb.append("\n§b生命值:§f " + p.getHealth());
                    return sb.toString();
                }
                return null;
            }
        });
//        regMessager(new MessagerAbs() {
//            @Override
//            public String getMsg(Player p,DataCon dc) {
//                double d = dc.getConfig().getDouble("zz",0);
//                if (d == 0) return null;
//                return "§b赞助:§f " + d;
//            }
//        });
    }

    public static void regMessager(MessagerAbs msg) {
        msgers.add(msg);
    }

    public static void unregMessager(MessagerAbs msg) {
        msgers.remove(msg);
    }

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (sender instanceof Player && args.length == 1){
            sendWhois(sender,MMOCore.getPlayerData((Player) sender));
            return true;
        }
        if (args.length == 2){
            if (sender.hasPermission("moeinfo.whois")){
                sendWhois(sender,MMOCore.getPlayerData(args[1],false));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        return getMatches(args,MMOCore.getLatelyPlayerList());
    }

    public boolean sendWhois(CommandSender sender,DataCon dc) {
        if (dc == null){
            sender.sendMessage("找不到玩家");
            return false;
        }
        sender.sendMessage(summon(sender,dc));
        return true;
    }

    public String summon(CommandSender sender,DataCon dataCon) {
        final StringBuilder sb = new StringBuilder();
        Iterator<MessagerAbs> it = msgers.iterator();
        while (it.hasNext()) {
            final MessagerAbs mr = it.next();
            String s = null;
            try{
                s = mr.getMsg(sender,dataCon);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (s != null){
                sb.append(s).append("\n");
            }
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}
