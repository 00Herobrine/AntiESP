package org.x00Hero.AntiESP;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.x00Hero.AntiESP.Events.VisionDetection.CanSee;

public class PlayerFunctions {
    private static HashMap<Player, List<Player>> hiddenPlayers = new HashMap<>();
    public static boolean isHidden(Player player, Player hider) { return isInitialized(hider) && hiddenPlayers.get(hider).contains(player); }
    public static boolean isInitialized(Player player) { return hiddenPlayers.containsKey(player); }
    public static void InitializeToList(Player player) {
        hiddenPlayers.put(player, new ArrayList<>());
    }
    public static void removeFromList(Player player) {
        hiddenPlayers.remove(player);
    }
    public static void hidePlayer(Player player, Player hider, String reason) {
        hidePlayerPacket(player, hider, reason);
        if(!CanSee(hider, player))
            if(!isHidden(hider, player)) hidePlayerPacket(hider, player, reason);
    }
    public static void showPlayer(Player player, Player hider) {
        showPlayerPacket(player, hider);
        if(CanSee(hider, player))
            if(isHidden(hider, player)) showPlayerPacket(hider, player);
    }
    public static void hidePlayerPacket(Player player, Player hider, String reason) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        CraftPlayer craftHider = (CraftPlayer) hider;
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(craftPlayer.getEntityId());
        craftHider.getHandle().b.a(packet);
        hiddenPlayers.get(hider).add(player);
        //Bukkit.getLogger().info("Hid " + player.getName() + " for " + hider.getName() + " Why? " + reason);
    }
    public static void showPlayerPacket(Player player, Player hider) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        EntityPlayer entityHider = ((CraftPlayer) hider).getHandle();
        PacketPlayOutNamedEntitySpawn spawnPacket = new PacketPlayOutNamedEntitySpawn(entityPlayer);
        entityHider.b.a(spawnPacket);
        PacketPlayOutEntity.PacketPlayOutEntityLook lookPacket = new PacketPlayOutEntity.PacketPlayOutEntityLook(entityPlayer.getBukkitEntity().getEntityId(), toByte(player.getLocation().getYaw()), toByte(player.getLocation().getPitch()), true);
        entityHider.b.a(lookPacket);
        PacketPlayOutEntityHeadRotation headPacket = new PacketPlayOutEntityHeadRotation(entityPlayer, toByte(player.getLocation().getYaw()));
        entityHider.b.a(headPacket);
        List<Pair<EnumItemSlot, ItemStack>> equipment = new ArrayList<>();
        equipment.add(Pair.of(EnumItemSlot.a, CraftItemStack.asNMSCopy(player.getInventory().getItemInMainHand())));
        equipment.add(Pair.of(EnumItemSlot.b, CraftItemStack.asNMSCopy(player.getInventory().getItemInOffHand())));
        equipment.add(Pair.of(EnumItemSlot.e, CraftItemStack.asNMSCopy(player.getInventory().getChestplate())));
        equipment.add(Pair.of(EnumItemSlot.f, CraftItemStack.asNMSCopy(player.getInventory().getHelmet())));
        equipment.add(Pair.of(EnumItemSlot.d, CraftItemStack.asNMSCopy(player.getInventory().getLeggings())));
        equipment.add(Pair.of(EnumItemSlot.c, CraftItemStack.asNMSCopy(player.getInventory().getBoots())));
        PacketPlayOutEntityEquipment equipmentPacket = new PacketPlayOutEntityEquipment(entityPlayer.getBukkitEntity().getEntityId(), equipment);
        entityHider.b.a(equipmentPacket);
        PacketPlayOutPlayerInfo infoPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, entityPlayer);
        entityHider.b.a(infoPacket);
        hiddenPlayers.get(hider).remove(player);
        //Bukkit.getLogger().info("Showing " + player.getName() + " for " + hider.getName());
    }
    protected static byte toByte(float yaw_pitch) { return (byte)(int)(yaw_pitch * 256.0F / 360.0F); }
}
