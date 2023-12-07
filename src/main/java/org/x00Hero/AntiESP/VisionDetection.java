package org.x00Hero.AntiESP;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static org.x00Hero.AntiESP.Main.Debug;
import static org.x00Hero.AntiESP.Main.plugin;

public class VisionDetection {
    public static String CanSee(Player viewer, Player viewing) {
        int distance = Bukkit.spigot().getConfig().getInt("world-settings.default.entity-tracking-range.players");
        if(viewer.getWorld() != viewing.getWorld()) return "Different Worlds";
        else if(viewer.getLocation().distance(viewing.getLocation()) > distance) return "View-Distance";
        Location location = viewer.getEyeLocation();
        Location[] viewPoints = getEquallySpacedPoints(viewing.getLocation(), viewing.getEyeLocation(), plugin.getConfig().getInt("viewPoints"));
        int pointsVisible = 0;
        for(Location viewPoint : viewPoints)
            if(!visionOccluded(location, viewPoint)) pointsVisible++;
        int requiredPointsVisible = plugin.getConfig().getInt("pointsVisible");
        return pointsVisible >= requiredPointsVisible ? null : "pointsVisible: " + pointsVisible + "/" + requiredPointsVisible;
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
    public static boolean visionOccluded(Location location, Location location2) {
        //Debug("Occlusion Check " + location + " | " + location2);
        double distance = location.distance(location2);
        Vector direction = location2.toVector().subtract(location.toVector()).normalize();
        double occlusionStep = 0.5;
        if(location.getDirection().dot(direction) < 0) return true;
        for(double d = 0; d < distance; d += occlusionStep) {
            Location checkLocation = location.clone().add(direction.clone().multiply(d));
            Block block = checkLocation.getBlock();
            if(!canSeeThroughBlock(block)) { /*Debug("Occluded @ " + checkLocation);*/ return true; } // Bukkit.getLogger().info("Occluded @ " + checkLocation);
        }
        //Debug("Visible @ " + location2);
        return false;
    }
    private static boolean canSeeThroughBlock(Block block) {
        return !block.getType().isOccluding();
    }
}
