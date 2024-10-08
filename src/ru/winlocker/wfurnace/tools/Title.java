package ru.winlocker.wfurnace.tools;

import java.lang.reflect.Constructor;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Title {
    public static void sendTitle(Player player, String text) {
        Title.sendTitle(player, text, 15, 60, 15);
    }

    public static void sendTitle(Player player, String text, int fadein, int stay, int fadeout) {
        text = ChatColor.translateAlternateColorCodes('&', text);
        String[] args = text.split("%nl%");
        try {
            String title = args[0];
            Object e1 = Objects.requireNonNull(Title.getNMS("PacketPlayOutTitle")).getDeclaredClasses()[0].getField("TIMES").get(null);
            Object chatTitle = Objects.requireNonNull(Title.getNMS("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + title + "\"}");
            Constructor<?> titleConstructor = Objects.requireNonNull(Title.getNMS("PacketPlayOutTitle")).getConstructor(Objects.requireNonNull(Title.getNMS("PacketPlayOutTitle")).getDeclaredClasses()[0], Title.getNMS("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
            Object titlepacket = titleConstructor.newInstance(e1, chatTitle, fadein, stay, fadeout);
            Title.sendPacket(player, titlepacket);
            e1 = Objects.requireNonNull(Title.getNMS("PacketPlayOutTitle")).getDeclaredClasses()[0].getField("TITLE").get(null);
            chatTitle = Objects.requireNonNull(Title.getNMS("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + title + "\"}");
            titleConstructor = Objects.requireNonNull(Title.getNMS("PacketPlayOutTitle")).getConstructor(Objects.requireNonNull(Title.getNMS("PacketPlayOutTitle")).getDeclaredClasses()[0], Title.getNMS("IChatBaseComponent"));
            titlepacket = titleConstructor.newInstance(e1, chatTitle);
            Title.sendPacket(player, titlepacket);
            if (args.length == 2) {
                String subtitle = args[1];
                Object e2 = Objects.requireNonNull(Title.getNMS("PacketPlayOutTitle")).getDeclaredClasses()[0].getField("TIMES").get(null);
                Object chatSubtitle = Objects.requireNonNull(Title.getNMS("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + subtitle + "\"}");
                Constructor<?> subtitleConstructor = Objects.requireNonNull(Title.getNMS("PacketPlayOutTitle")).getConstructor(Objects.requireNonNull(Title.getNMS("PacketPlayOutTitle")).getDeclaredClasses()[0], Title.getNMS("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                Object subtitlepacket = subtitleConstructor.newInstance(e2, chatSubtitle, fadein, stay, fadeout);
                Title.sendPacket(player, subtitlepacket);
                e2 = Objects.requireNonNull(Title.getNMS("PacketPlayOutTitle")).getDeclaredClasses()[0].getField("SUBTITLE").get(null);
                chatSubtitle = Objects.requireNonNull(Title.getNMS("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + subtitle + "\"}");
                subtitleConstructor = Objects.requireNonNull(Title.getNMS("PacketPlayOutTitle")).getConstructor(Objects.requireNonNull(Title.getNMS("PacketPlayOutTitle")).getDeclaredClasses()[0], Title.getNMS("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                subtitlepacket = subtitleConstructor.newInstance(e2, chatSubtitle, fadein, stay, fadeout);
                Title.sendPacket(player, subtitlepacket);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke((Object)player, new Object[0]);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", Title.getNMS("Packet")).invoke(playerConnection, packet);
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

