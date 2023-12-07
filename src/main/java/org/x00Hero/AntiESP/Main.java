package org.x00Hero.AntiESP;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.x00Hero.AntiESP.Events.CommandManager;
import org.x00Hero.AntiESP.Events.PlayerMove;

public final class Main extends JavaPlugin {

    public static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        RegisterEvents();
        RegisterCommands();
    }

    public static void Debug(String... message) {
        if(!plugin.getConfig().getBoolean("debug")) return;
        for(String msg : message) Bukkit.getLogger().info(msg);
    }
    private void RegisterEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerMove(), plugin);
    }
    public void RegisterCommands() { getCommand("atp").setExecutor(new CommandManager()); }

    private BukkitTask runTask(Runnable runnable, boolean isAsync) {
        if(isAsync) return Bukkit.getScheduler().runTaskAsynchronously(this, runnable);
        return Bukkit.getScheduler().runTask(this, runnable);
    }
}
