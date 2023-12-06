package org.x00Hero.AntiESP.Events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static org.x00Hero.AntiESP.Main.plugin;

public class VisionDetection {
    public static boolean CanSee(Player viewer, Player viewing) {
        Location location = viewer.getEyeLocation();
        Location[] viewPoints = getEquallySpacedPoints(viewing.getLocation(), viewing.getEyeLocation(), plugin.getConfig().getInt("viewPoints"));
        int pointsVisible = 0;
        for(Location viewPoint : viewPoints)
            if(!visionOccluded(location, viewPoint)) pointsVisible++;
        return pointsVisible >= plugin.getConfig().getInt("pointsVisible");
    }
    public static Location[] getEquallySpacedPoints(Location startLocation, Location endLocation, int numPoints) {
        World world = startLocation.getWorld();
        double xStep = (endLocation.getX() - startLocation.getX()) / (numPoints - 1);
        double yStep = (endLocation.getY() - startLocation.getY()) / (numPoints - 1);
        double zStep = (endLocation.getZ() - startLocation.getZ()) / (numPoints - 1);
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
        //Bukkit.getLogger().info("Occlusion Check " + location + " | " + location2);
        double distance = location.distance(location2);
        Vector direction = location2.toVector().subtract(location.toVector()).normalize();
        for(double d = 0; d < distance; d += 0.5) {
            Location checkLocation = location.clone().add(direction.clone().multiply(d));
            Block block = checkLocation.getBlock();
            if(!canSeeThroughBlock(block)) return true; // Bukkit.getLogger().info("Occluded @ " + checkLocation);
        }
        //Bukkit.getLogger().info("Visible");
        return false;
    }
    private static boolean canSeeThroughBlock(Block block) {
        return !block.getType().isOccluding();
    }
}
