package cn.whiteg.moeInfo.commands;

import at.pcgamingfreaks.MarriageMaster.Bukkit.Databases.Database;
import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.moeEco.VaultHandler;
import cn.whiteg.moeInfo.MoeInfo;
import cn.whiteg.moeInfo.api.WhoisMessageProvider;
import cn.whiteg.moeInfo.utils.PlayerDisplayNameManage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

import static cn.whiteg.moeInfo.MoeInfo.plugin;
import static cn.whiteg.moeInfo.MoeInfo.settin;

public class whois extends CommandInterface {

    private static Set<WhoisMessageProvider> messagers = new LinkedHashSet<>();
    final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //时间格式

    public whois() {
        regMessager(new WhoisMessageProvider(plugin) {
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
        regMessager(new WhoisMessageProvider(plugin) {
            @Override
            public String getMsg(CommandSender p,DataCon dc) {
                return "§bUUID: §f" + dc.getUUID().toString();
            }
        });
        regMessager(new WhoisMessageProvider(plugin) {
            @Override
            public String getMsg(CommandSender p,DataCon dc) {
                String str = PlayerDisplayNameManage.getPrefix(dc);
                return str.isEmpty() ? null : ("§b前缀: §f" + ChatColor.translateAlternateColorCodes('&',str));
            }
        });
        regMessager(new WhoisMessageProvider(plugin) {
            @Override
            public String getMsg(CommandSender p,DataCon dc) {
                String str = PlayerDisplayNameManage.getSuffix(dc);
                return str.isEmpty() ? null : ("§b后缀: §f" + ChatColor.translateAlternateColorCodes('&',str));
            }
        });
        regMessager(new WhoisMessageProvider(plugin) {
            @Override
            public String getMsg(CommandSender p,DataCon dc) {
                String str = dc.getString("NameOnceUsed");
                if (str == null || str.isEmpty()) return null;
                return "§b曾用名: §f" + str;
            }
        });
        regMessager(new WhoisMessageProvider(plugin) {
            @Override
            public String getMsg(CommandSender p,DataCon dc) {
                long l = Long.parseLong(dc.getString("Player.join_time","0"));
                if (l == 0) return null;
                Date date = new Date(l);
                return ("§b加入时间: §f" + timeFormat.format(date));
            }
        });
        regMessager(new WhoisMessageProvider(plugin) {
            @Override
            public String getMsg(CommandSender p,DataCon dc) {
                long l = Long.parseLong(dc.getString("Player.login_time","0"));
                if (l == 0) return null;
                Date date = new Date(l);
                return "§b最后登录时间: §f" + timeFormat.format(date);
            }
        });


        regMessager(new WhoisMessageProvider(plugin) {
            @Override
            public String getMsg(CommandSender p,DataCon dc) {
                if (plugin.economy != null){
                    if (plugin.economy instanceof VaultHandler){
                        return new StringBuilder().append("§b").append(plugin.economy.currencyNameSingular()).append(":§f").append(((VaultHandler) plugin.economy).getFormatBalance(dc)).toString();
                    } else {
                        return new StringBuilder().append("§b").append(plugin.economy.currencyNameSingular()).append(":§f").append(String.format("%.1s",settin.decimalFormat.format(plugin.economy.getBalance(p.getName())))).toString();
                    }
                }
                return null;
            }
        });

        regMessager(new WhoisMessageProvider(plugin) {
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

        regMessager(new WhoisMessageProvider(plugin) {
            @Override
            public String getMsg(CommandSender sender,DataCon dc) {
                String pat = "Player.qqid";
                long id = dc.getConfig().getLong(pat,0);
                if (id == 0) return null;
                return "§bQQ:§f " + id;
            }
        });

        regMessager(new WhoisMessageProvider(plugin) {
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
        regMessager(new WhoisMessageProvider(plugin) {
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
//        regMessager(new MessagerAbs(plugin) {
//            @Override
//            public String getMsg(Player p,DataCon dc) {
//                double d = dc.getConfig().getDouble("zz",0);
//                if (d == 0) return null;
//                return "§b赞助:§f " + d;
//            }
//        });
    }

    public static void regMessager(WhoisMessageProvider msg) {
        messagers.add(msg);
    }

    public static void unregMessager(WhoisMessageProvider msg) {
        messagers.remove(msg);
    }

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 0){
            sendWhois(sender,MMOCore.getPlayerData(sender));
            return true;
        } else if (args.length == 1){
            if (sender.hasPermission("moeinfo.whois")){
                sendWhois(sender,MMOCore.getPlayerData(args[0],false));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender,Command command,String s,String[] args) {
        return getMatches(MMOCore.getLatelyPlayerList(),args);
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
        Iterator<WhoisMessageProvider> it = messagers.iterator();
        while (it.hasNext()) {
            final WhoisMessageProvider mr = it.next();
            if (!mr.isEnable()){
                it.remove();
                continue;
            }
            String s;
            try{
                s = mr.getMsg(sender,dataCon);
            }catch (Exception e){
                e.printStackTrace();
                s = e.getMessage();
            }
            if (s != null){
                sb.append(s).append("\n");
            }
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}
