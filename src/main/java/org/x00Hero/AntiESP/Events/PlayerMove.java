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

import static org.x00Hero.AntiESP.Main.Debug;
import static org.x00Hero.AntiESP.VisionDetection.CanSee;
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
        if (excludedFromCheck.contains(hider.getUniqueId())) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (hider == player) continue;
            visibilityCheck(hider, player);
            visibilityCheck(player, hider);
        }
        long completed = System.currentTimeMillis();
        Debug("Took " + (completed - start) + "ms to complete.");
    }
    private void visibilityCheck(Player viewer, Player target) {
        if (!isInitialized(viewer)) InitializeToList(viewer);
        String reason = CanSee(viewer, target);
        if (reason != null) hidePlayer(target, viewer, reason);
        else if (isHidden(target, viewer)) showPlayer(target, viewer);
    }
}
