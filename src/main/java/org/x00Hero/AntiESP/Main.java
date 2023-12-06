package org.x00Hero.AntiESP;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.x00Hero.AntiESP.Events.PlayerMove;

public final class Main extends JavaPlugin {

    public static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;
        RegisterEvents();
    }

    private void RegisterEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerMove(), plugin);
    }

    private BukkitTask runTask(Runnable runnable, boolean isAsync) {
        if(isAsync) return Bukkit.getScheduler().runTaskAsynchronously(this, runnable);
        return Bukkit.getScheduler().runTask(this, runnable);
    }
}
