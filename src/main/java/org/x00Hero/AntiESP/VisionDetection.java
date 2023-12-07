package org.x00Hero.AntiESP;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static org.x00Hero.AntiESP.Main.Debug;
import static org.x00Hero.AntiESP.PlayerFunctions.*;
import static org.x00Hero.AntiESP.PlayerFunctions.showPlayer;

public class VisionDetection {
    public static String CanSee(Player viewer, Player viewing) {
        if(viewer.getWorld() != viewing.getWorld()) return "Different Worlds";
        else if(viewer.getLocation().distance(viewing.getLocation()) > Config.viewDistance) return "View-Distance";
        Location location = viewer.getEyeLocation();
        Location[] viewPoints = getEquallySpacedPoints(viewing.getLocation(), viewing.getEyeLocation(), Config.viewPoints);
        int pointsVisible = 0;
        List<String> occlusions = new ArrayList<>();
        for(Location viewPoint : viewPoints) {
            String result = visionOccluded(location, viewPoint);
            if(result == null) pointsVisible++;
            else occlusions.add(result);
        }
        return pointsVisible >= Config.pointsVisible ? null : "pointsVisible: " + pointsVisible + "/" + Config.pointsVisible;
    }
    public static Location[] getEquallySpacedPoints(Location startLocation, Location endLocation, int numPoints) {
        World world = startLocation.getWorld();
        double xStep = (endLocation.getX() - startLocation.getX()) / (numPoints);
        double yStep = (endLocation.getY() - startLocation.getY()) / (numPoints);
        double zStep = (endLocation.getZ() - startLocation.getZ()) / (numPoints);
        Location[] result = new Location[numPoints];
        for (int i = 0; i < numPoints; i++) {
            double x = startLocation.getX() + i * xStep;
            double y = startLocation.getY() + i * yStep;
            double z = startLocation.getZ() + i * zStep;
            result[i] = new Location(world, x, y, z);
        }
        return result;
    }
    public static void visibilityCheck(Player viewer, Player target) {
        if (!isInitialized(viewer)) InitializeToList(viewer);
        String reason = CanSee(viewer, target);
        if (reason != null && !isHidden(target, viewer)) hidePlayer(target, viewer, reason);
        else if (reason == null && isHidden(target, viewer)) showPlayer(target, viewer);
    }
    public static String visionOccluded(Location location, Location location2) {
        //Debug("Occlusion Check " + location + " | " + location2);
        double distance = location.distance(location2);
        Vector direction = location2.toVector().subtract(location.toVector()).normalize();
        double dot = location.getDirection().dot(direction);
        if(dot < Config.dotThreshold) return "Dot Threshold";
        for(double d = 0; d < distance; d += Config.occlusionStep) {
            Location checkLocation = location.clone().add(direction.clone().multiply(d));
            Block block = checkLocation.getBlock();
            if(!canSeeThroughBlock(block)) { /*Debug("Occluded @ " + checkLocation);*/ return block.getType() + " is occluding." ; } // Bukkit.getLogger().info("Occluded @ " + checkLocation);
        }
        //Debug("Visible @ " + location2);
        return null;
    }
    private static boolean canSeeThroughBlock(Block block) { return !block.getType().isOccluding(); }
}