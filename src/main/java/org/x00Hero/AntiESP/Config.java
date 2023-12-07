package org.x00Hero.AntiESP;

import org.bukkit.Bukkit;

import static org.x00Hero.AntiESP.Main.plugin;

public class Config {
    public static int updateRate;
    public static int visionRange;
    public static int viewPoints;
    public static int pointsVisible;
    public static double dotThreshold;
    public static double occlusionStep;
    public static double viewDistance;
    public static boolean asyncCheck;
    public static boolean debug;

    public static void Load() {
        viewDistance = Bukkit.spigot().getConfig().getInt("world-settings.default.entity-tracking-range.players");
        updateRate = plugin.getConfig().getInt("update-rate");
        visionRange = plugin.getConfig().getInt("vision-range");
        viewPoints = plugin.getConfig().getInt("view-points");
        pointsVisible = plugin.getConfig().getInt("visible-points");
        dotThreshold = plugin.getConfig().getDouble("dot-threshold");
        occlusionStep = plugin.getConfig().getDouble("occlusion-step");
        asyncCheck = plugin.getConfig().getBoolean("asyncCheck");
        debug = plugin.getConfig().getBoolean("debug");
    }
    public static void Reload() {
        plugin.reloadConfig();
        Load();
    }

    public static int getVisionRange() { return visionRange; }
    public static int getViewPoints() { return viewPoints; }
    public static int getUpdateRate() { return updateRate; }
    public static int getPointsVisible() { return pointsVisible; }
    public static boolean isAsyncCheck() { return asyncCheck; }
    private boolean isDebug() { return debug; }

}
