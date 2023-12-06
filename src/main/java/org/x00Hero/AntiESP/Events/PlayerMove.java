package org.x00Hero.AntiESP.Events;

import org.bukkit.Location;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

import static org.x00Hero.AntiESP.Events.VisionDetection.CanSee;
import static org.x00Hero.AntiESP.PlayerFunctions.*;

public class PlayerMove implements Listener {
    public ArrayList<UUID> excludedFromCheck = new ArrayList<>();
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        InitializeToList(e.getPlayer());
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        removeFromList(e.getPlayer());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        long start = System.currentTimeMillis();
        Player hider = e.getPlayer();
        if(excludedFromCheck.contains(hider.getUniqueId())) return;
        if(!isInitialized(hider)) InitializeToList(hider);
        Location location1 = hider.getLocation();
        for(Player player : Bukkit.getOnlinePlayers()) {
            Location location2 = player.getLocation();
            if(hider == player) continue;
            if(player.getWorld() != hider.getWorld()) hidePlayer(player, hider, "!= World");
            else if(location2.distance(location1) > Bukkit.getViewDistance() * 16) hidePlayer(player, hider, "View-Distance");
            else if(!CanSee(hider, player)) hidePlayer(player, hider, "Vision Obscured");
            else if(isHidden(player, hider)) showPlayer(player, hider);
        }
        long completed = System.currentTimeMillis();
        //Bukkit.getLogger().info("Took " + (completed - start) + "ms to complete.");
    }
}
