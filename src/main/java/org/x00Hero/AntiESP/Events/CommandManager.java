package org.x00Hero.AntiESP.Events;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.x00Hero.AntiESP.Main.plugin;

public class CommandManager implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("atp")) {
            if(args.length == 0) return false;
            switch(args[0]) {
                case "reload", "rl" -> { plugin.reloadConfig(); sender.sendMessage("[Anti-ESP] Reloaded."); return true; }
            }
        }
        return false;
    }
}
