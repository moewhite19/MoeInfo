package cn.whiteg.moeInfo;

import cn.whiteg.memfree.MemFree;
import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.util.CoolDownUtil;
import cn.whiteg.moeEco.VaultHandler;
import cn.whiteg.moeInfo.api.TabPlayerListMsgAbs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

import static cn.whiteg.moeInfo.MoeInfo.settin;

public class TabPlayerlistsTimer extends Thread {
    final private MoeInfo plugin;
    private Set<TabPlayerListMsgAbs> msgers = new LinkedHashSet<>();
    private boolean run;

    public TabPlayerlistsTimer(MoeInfo moeInfo) {
        plugin = moeInfo;
        setName("TabListTimer");
        setPriority(9);
        setDaemon(true);
        run = true;
        super.start();
        regMeger(new TabPlayerListMsgAbs() {
            @Override
            public String getMsg(Player p,DataCon dc) {
                if (plugin.economy != null){
                    if (plugin.economy instanceof VaultHandler){
                        return new StringBuilder().append(" §b").append(plugin.economy.currencyNameSingular()).append(":§f").append(String.format("%.1f",((VaultHandler) plugin.economy).getBalance(dc))).toString();
                    } else {
                        return new StringBuilder().append(" §b").append(plugin.economy.currencyNameSingular()).append(":§f").append(String.format("%.1f",plugin.economy.getBalance(p.getName()))).toString();
                    }
                }
                return null;
            }
        });

        regMeger(new TabPlayerListMsgAbs() {
            @Override
            public String getMsg(Player p,DataCon dc) {
                return new StringBuilder().append(" §b延迟:§f").append(p.spigot().getPing()).toString();
            }
        });

        regMeger(new TabPlayerListMsgAbs() {
            @Override
            public String getMsg(Player p,DataCon dc) {
                final String mar = plugin.externalManage.marriageMaster == null ? null : plugin.externalManage.marriageMaster.DB.GetPartner(p.getName());
                if (mar != null){
                    return new StringBuilder("§b伴侣:§f").append(mar).toString();
                }
                return null;
            }
        });
    }

    public void regMeger(TabPlayerListMsgAbs msger) {
        msgers.add(msger);
    }

    public void unreg(TabPlayerListMsgAbs msger) {
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
        while (run) {
            try{
                Collection<? extends Player> players = Bukkit.getOnlinePlayers();
                if (players.isEmpty()){
                    sleep(settin.TAB_TIMER_INTERVAL);
                    continue;
                }
                int sleptime = settin.TAB_TIMER_INTERVAL / players.size();
                if (sleptime <= 0) sleptime = 1;
                StringBuilder sb = new StringBuilder().append(settin.LISTHEAD);
                if (plugin.memfree)
                    sb.append("\n§7T:§f").append(String.format("%.2f",MemFree.plugin.timer.tps)).append(" §7- §f").append(players.size()).append("/").append(Bukkit.getServer().getMaxPlayers()).append("§7 -").append(" §7M:§f").append(String.format("%.2f",(MemFree.plugin.timer.use / (double) MemFree.plugin.timer.max) * 100)).append("%");
                else
                    sb.append("\n§7- §f").append(players.size()).append("/").append(Bukkit.getServer().getMaxPlayers()).append("§7 -");
                byte maxcont = (byte) (players.size() > 20 ? 3 : 2);
                final String hd = sb.toString();
                final Iterator<? extends Player> iterator = players.iterator();
                while (iterator.hasNext()) {
                    Player p = iterator.next();
                    if (!p.isOnline()) continue;
                    List<String> infos = new ArrayList<>();
                    final DataCon dc = MMOCore.getPlayerData(p);
                    Iterator<TabPlayerListMsgAbs> it = msgers.iterator();
                    while (it.hasNext()) {
                        TabPlayerListMsgAbs mr = it.next();
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

                    final CoolDownUtil.PlayerCd cds = CoolDownUtil.get(p.getName());
                    if (cds != null){
                        for (String ck : cds.getKeys()) {
                            if (!ck.startsWith("§")) continue;
                            int i = cds.getCds(ck);
                            if (i > 0){
                                infos.add(ck + "§7#" + i);
                            }
                        }
                    }

                    sb = new StringBuilder();
                    for (short i = 0; i < infos.size(); i++) {
                        if (i % maxcont == 0) sb.append("\n");
                        sb.append(infos.get(i));
                        sb.append(" ");
                    }
                    p.setPlayerListHeaderFooter(hd,sb.toString());
//                    p.setPlayerListName(p.getDisplayName() + "§r §7" + updateLoc(p));
                    sleep(sleptime);
                }
            }catch (Throwable e){
                MoeInfo.logger.info("玩家Tab列表刷新错误");
                sleep(settin.TAB_TIMER_INTERVAL);
            }
        }
    }

    public void remove() {
        run = false;
        stop();
    }
}
