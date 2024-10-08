package ru.winlocker.wfurnace;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.winlocker.wfurnace.FurnaceInfo;
import ru.winlocker.wfurnace.Furnaces;
import ru.winlocker.wfurnace.tools.Utils;

public class Execute implements CommandExecutor, TabCompleter {


    /* Leaked by https://t.me/leak_mine
       Сурцы плагина были слиты телеграмм каналом https://t.me/leak_mine
    */				 
	
    @SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            Utils.sendMessage(sender, Utils.getMessage("give.usage"));
            Utils.sendMessage(sender, Utils.getMessage("list.usage"));
            return true;
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (!Utils.has(sender, "wfurnace.give")) {
                return true;
            }
            if (args.length < 3) {
                Utils.sendMessage(sender, Utils.getMessage("give.usage"));
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                Utils.sendMessage(sender, Utils.getMessage("give.player-null"));
                return true;
            }
            FurnaceInfo info = Furnaces.getFurnace(args[2]);
            if (info == null) {
                Utils.sendMessage(sender, Utils.getMessage("give.furnace-null"));
                return true;
            }
            player.getInventory().addItem(new ItemStack[]{info.getItem()});
            Utils.sendMessage(sender, Utils.getMessage("give.complete").replace("%player%", player.getName()));
            return true;
        }
        if (args[0].equalsIgnoreCase("list")) {
            if (!Utils.has(sender, "wfurnace.list")) {
                return true;
            }
            Utils.sendMessage(sender, Utils.getMessage("list.title"));
            int i = 0;
            for (FurnaceInfo info : Furnaces.getFurnaces()) {
                String format = Utils.getMessage("list.format");
                format = format.replace("%step%", "" + ++i);
                format = format.replace("%name%", info.getName());
                Utils.sendMessage(sender, format);
            }
            return true;
        }
        Utils.sendMessage(sender, Utils.getMessage("command-notfound"));
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args[0].equalsIgnoreCase("give")) {
            if (args.length == 2) {
                ArrayList<String> players = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    players.add(player.getName().toLowerCase());
                }
                return this.filter(players, args);
            }
            if (args.length == 3) {
                ArrayList<String> furnaces = new ArrayList<>();
                for (FurnaceInfo info : Furnaces.getFurnaces()) {
                    furnaces.add(info.getName());
                }
                return this.filter(furnaces, args);
            }
        }
        return Lists.newArrayList();
    }

    public List<String> filter(List<String> list, String[] args) {
        String last = args[args.length - 1].toLowerCase();
        ArrayList<String> result = new ArrayList<>();
        for (String s : list) {
            if (!s.startsWith(last)) continue;
            result.add(s);
        }
        return result;
    }
}

