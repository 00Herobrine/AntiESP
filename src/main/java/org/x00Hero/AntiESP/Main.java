package org.x00Hero.AntiESP;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.x00Hero.AntiESP.Events.CommandManager;
import org.x00Hero.AntiESP.Events.PlayerMove;

import static org.x00Hero.AntiESP.VisionDetection.visibilityCheck;

public final class Main extends JavaPlugin {
    public static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        RegisterEvents();
        RegisterCommands();
        Config.Load();
        VisionCheck();
    }

    public static void Debug(String... message) {
        if(!Config.debug) return;
        for(String msg : message) Bukkit.getLogger().info(msg);
    }
    private void RegisterEvents() { Bukkit.getPluginManager().registerEvents(new PlayerMove(), plugin); }
    public void RegisterCommands() { getCommand("atp").setExecutor(new CommandManager()); }

    public static int visionTaskID;
    public static void VisionCheck() {
        if(Config.updateRate == -1) return;
        visionTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            long start = System.currentTimeMillis();
            for(Player player : Bukkit.getOnlinePlayers()) {
                for(Entity entity : player.getNearbyEntities(Config.viewDistance, Config.viewDistance, Config.viewDistance)) if(entity instanceof Player near)
                visibilityCheck(player, near);
            }
            long finish = System.currentTimeMillis();
            Debug("Took " + (finish - start) + "ms to complete");
        },1, Math.abs(Config.updateRate));
    }
}
