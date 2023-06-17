package cn.whiteg.moeInfo;

import cn.whiteg.memfree.MemFree;
import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.util.CoolDownUtil;
import cn.whiteg.moeEco.VaultHandler;
import cn.whiteg.moeInfo.api.TabMessageProvider;
import cn.whiteg.moeInfo.utils.CommonUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

import static cn.whiteg.moeInfo.MoeInfo.settin;

public class TabPlayerListsTimer extends Thread {
    final private MoeInfo plugin;
    private final Set<TabMessageProvider> msgers = new LinkedHashSet<>();
    private boolean isRun;

    public TabPlayerListsTimer(MoeInfo moeInfo) {
        plugin = moeInfo;
        setName("TabListTimer");
        setPriority(9);
        setDaemon(true);
        isRun = true;
        super.start();
        regMeger(new TabMessageProvider(plugin) {
            @Override
            public String getMsg(Player p,DataCon dc) {
                if (plugin.economy != null){
                    return new StringBuilder().append("§b").append(plugin.economy.currencyNameSingular()).append(":§f").append(((VaultHandler) plugin.economy).getFormatBalance(dc)).toString();
                }
                return null;
            }
        });

        regMeger(new TabMessageProvider(plugin) {
            @Override
            public String getMsg(Player p,DataCon dc) {
                return new StringBuilder().append(" §b延迟:§f").append(p.getPing()).toString();
            }
        });

        regMeger(new TabMessageProvider(plugin) {
            @Override
            public String getMsg(Player p,DataCon dc) {
                final String mar = plugin.externalManage.marriageMaster == null ? null : plugin.externalManage.marriageMaster.DB.GetPartner(p.getName());
                if (mar != null){
                    return new StringBuilder("§b伴侣:§f").append(mar).toString();
                }
                return null;
            }
        });

/*        regMeger(new TabMessageProvider(plugin) {
            @Override
            public String getMsg(Player player,DataCon dataCon) {
                World world = player.getWorld();
                if (world.getEnvironment() == World.Environment.NORMAL){
//                    return new StringBuilder("§b世界§f: ").append(CommonUtils.).toString();
                }
                return null;
            }
        });*/

        regMeger(new TabMessageProvider(plugin) {
            @Override
            public String getMsg(Player player,DataCon dataCon) {
                World world = player.getWorld();
                if (world.getEnvironment() == World.Environment.NORMAL){
                    return new StringBuilder("§b世界时间§f: ").append(CommonUtils.getWorldTime(world.getTime())).toString();
                }
                return null;
            }
        });


    }


    public void regMeger(TabMessageProvider msger) {
        msgers.add(msger);
    }

    public void unreg(TabMessageProvider msger) {
        msgers.remove(msger);
    }


    public void sleep(final int time) {
        try{
            Thread.sleep(time);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        while (isRun) {
            try{
                Collection<? extends Player> players = Bukkit.getOnlinePlayers();
                if (players.isEmpty()){
                    sleep(settin.TAB_TIMER_INTERVAL);
                    continue;
                }
                int sleptime = settin.TAB_TIMER_INTERVAL / players.size();
                if (sleptime <= 0) sleptime = 1;
                StringBuilder sb = new StringBuilder().append(settin.LISTHEAD);

                //如果有MemFree插件则显示服务器状态
                if (plugin.memfree){
                    long max = Runtime.getRuntime().maxMemory();
                    long use = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                    sb.append("\n§7T:§f").append(String.format("%.2f",MemFree.plugin.timer.tps)).append(" §7- §f").append(players.size()).append("/").append(Bukkit.getServer().getMaxPlayers()).append("§7 -").append(" §7M:§f").append(String.format("%.2f",(use / (double) max) * 100)).append("%");
                } else
                    sb.append("\n§7- §f").append(players.size()).append("/").append(Bukkit.getServer().getMaxPlayers()).append("§7 -");

                byte maxcont = (byte) (players.size() > 20 ? 3 : 2);
                final String hd = sb.toString();
                final Iterator<? extends Player> iterator = players.iterator();
                //异步更新玩家列表
                while (iterator.hasNext()) {
                    Player p = iterator.next();
                    if (!p.isOnline()) continue;
                    List<String> infos = new ArrayList<>();
                    final DataCon dc = MMOCore.getPlayerData(p);
                    Iterator<TabMessageProvider> it = msgers.iterator();
                    while (it.hasNext()) {
                        TabMessageProvider mr = it.next();

                        if (!mr.isEnable()){
                            it.remove();
                            continue;
                        }

                        String s = null;
                        try{
                            s = mr.getMsg(p,dc);
                        }catch (Throwable e){
                            e.printStackTrace();
                        }
                        if (s != null){
                            infos.add(s);
                        }
                    }

                    //显示玩家的剩余cd状态
                    final CoolDownUtil.PlayerCd cds = CoolDownUtil.get(p.getName());
                    if (cds != null){
                        for (String ck : cds.getKeys()) {
                            //只显示带§颜色的cd
                            if (!ck.startsWith("§")) continue;
                            int i = cds.getCds(ck);
                            if (i > 0){
                                infos.add(ck + "§7#" + i);
                            }
                        }
                    }

                    sb.setLength(0);
                    for (short i = 0; i < infos.size(); i++) {
                        if (i % maxcont == 0) sb.append("\n");
                        sb.append(infos.get(i));
                        sb.append(" ");
                    }

                    if (!sb.isEmpty()) sb.deleteCharAt(sb.length() - 1); //删除末尾空格

                    p.setPlayerListHeaderFooter(hd,sb.toString());
                    sleep(sleptime);
                }
            }catch (Throwable e){
                MoeInfo.logger.info("玩家Tab列表刷新错误: " + e);
                sleep(settin.TAB_TIMER_INTERVAL);
            }
        }
    }

    public void close() {
        isRun = false;
//        stop();
    }
}
