package cn.whiteg.moeInfo;

import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.mmocore.util.PluginUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainCommand extends CommandInterface {
    public String[] allCommands = new String[]{"reload","prefix","suffix","namecolour","whois","mute"};
    public Map<String, CommandInterface> commandMap = new HashMap<>(allCommands.length);
    public SubCommand subCommand = new SubCommand();

    public MainCommand() {
        for (int i = 0; i < allCommands.length; i++) {
            String cmd = allCommands[i];
            try{
                Class c = Class.forName("cn.whiteg.moeInfo.mainCommand." + cmd);
                CommandInterface ci = (CommandInterface) c.newInstance();
                regCommand(cmd,ci);
                PluginCommand pc = PluginUtil.getPluginCommand(MoeInfo.plugin,cmd);
                if (pc != null){
                    pc.setExecutor(subCommand);
                    pc.setTabCompleter(subCommand);
                }
            }catch (ClassNotFoundException | InstantiationException | IllegalAccessException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 0){
            sender.sendMessage("§b梦式消息插件");
            return true;
        }
        if (commandMap.containsKey(args[0])){
            return commandMap.get(args[0]).onCommand(sender,cmd,label,args);
        } else {
            sender.sendMessage("未知子指令");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length > 1){
            List<String> ls = null;
            if (commandMap.containsKey(args[0])) ls = commandMap.get(args[0]).onTabComplete(sender,cmd,label,args);
            if (ls != null){
                return getMatches(ls,args);
            }
        }
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].toLowerCase();
        }
        if (args.length == 1){
            return getMatches(args[0],Arrays.asList(allCommands));
        }
        return null;
    }

    public void regCommand(String var1,CommandInterface cmd) {
        commandMap.put(var1,cmd);
    }

    public class SubCommand extends CommandInterface {
        @Override
        public boolean onCommand(CommandSender commandSender,Command command,String s,String[] strings) {
            CommandInterface ci = MoeInfo.plugin.mainCmd.commandMap.get(command.getName());
            if (ci == null) return false;
            String[] args = new String[strings.length + 1];
            args[0] = command.getName();
    /*        for(int i = 0 ; i < args.length ; i++){
                args[i + 1] = strings[i] ;
            }*/
            System.arraycopy(strings,0,args,1,strings.length);
            ci.onCommand(commandSender,command,s,args);
            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender commandSender,Command command,String s,String[] strings) {
            CommandInterface ci = MoeInfo.plugin.mainCmd.commandMap.get(command.getName());
            if (ci == null) return null;
            String[] args = new String[strings.length + 1];
            args[0] = command.getName();
    /*        for(int i = 0 ; i < args.length ; i++){
                args[i + 1] = strings[i] ;
            }*/

            System.arraycopy(strings,0,args,1,strings.length);
            return ci.onTabComplete(commandSender,command,s,args);
        }
    }
}
