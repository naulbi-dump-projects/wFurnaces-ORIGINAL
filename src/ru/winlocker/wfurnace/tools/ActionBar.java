package ru.winlocker.wfurnace.tools;

import java.lang.reflect.Constructor;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ActionBar {
	
    public static void sendActionBar(Player player, String text) {
        text = ChatColor.translateAlternateColorCodes('&', text);
        try {
            Object chat = Objects.requireNonNull(ActionBar.getNMS("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + text + "\"}");
            Object chattype = Objects.requireNonNull(ActionBar.getNMS("ChatMessageType")).getField("GAME_INFO").get(null);
            Constructor<?> actionConstructor = Objects.requireNonNull(ActionBar.getNMS("PacketPlayOutChat")).getConstructor(ActionBar.getNMS("IChatBaseComponent"), ActionBar.getNMS("ChatMessageType"));
            Object actionpacket = actionConstructor.newInstance(chat, chattype);
            ActionBar.sendPacket(player, actionpacket);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke((Object)player, new Object[0]);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", ActionBar.getNMS("Packet")).invoke(playerConnection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Class<?> getNMS(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

