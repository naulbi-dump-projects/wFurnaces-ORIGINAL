package ru.winlocker.wfurnace;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import ru.winlocker.wfurnace.FurnaceInfo;
import ru.winlocker.wfurnace.Main;
import ru.winlocker.wfurnace.tools.ItemBuilder;
import ru.winlocker.wfurnace.tools.Utils;

public class Furnaces {

    /* Leaked by https://t.me/leak_mine
       Сурцы плагина были слиты телеграмм каналом https://t.me/leak_mine
    */	
	
    private static List<FurnaceInfo> furnaces = new ArrayList<>();

    public static List<FurnaceInfo> getFurnaces() {
        return furnaces;
    }

    public static FurnaceInfo getFurnace(ItemStack item) {
        String name = item.getItemMeta().getDisplayName();
        if (name == null) {
            return null;
        }
        for (FurnaceInfo info : Furnaces.getFurnaces()) {
            if (!info.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(name)) continue;
            return info;
        }
        return null;
    }

    public static FurnaceInfo getFurnace(Block block) {
        Furnace furnace = (Furnace)block.getState();
        String name = furnace.getInventory().getTitle();
        for (FurnaceInfo info : Furnaces.getFurnaces()) {
            if (info.getItem().getItemMeta().getDisplayName() == null || !info.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(name)) continue;
            return info;
        }
        return null;
    }

    public static FurnaceInfo getFurnace(String name) {
        for (FurnaceInfo info : Furnaces.getFurnaces()) {
            if (!info.getName().equalsIgnoreCase(name)) continue;
            return info;
        }
        return null;
    }

    public static void loadFurnaces() {
        for (String furnaces : Utils.getConfig().getConfigurationSection("furnaces").getKeys(false)) {
            ItemBuilder builder = new ItemBuilder(Material.FURNACE);
            builder.setDisplayName(String.valueOf(Utils.getConfig().getString("furnaces." + furnaces + ".name")) + "&1&4&0&5");
            builder.setLore(Utils.getConfig().getStringList("furnaces." + furnaces + ".lore"));
            if (Utils.getConfig().getBoolean("furnaces." + furnaces + ".enchant")) {
                builder.enchant(Enchantment.FIRE_ASPECT, 1);
                builder.flag(ItemFlag.HIDE_ENCHANTS);
            }
            FurnaceInfo info = new FurnaceInfo();
            info.setName(furnaces);
            info.setItem(builder.build());
            info.setBurnTime(Utils.getConfig().getInt("furnaces." + furnaces + ".burntime"));
            info.setResult(Utils.getConfig().getInt("furnaces." + furnaces + ".result"));
            info.setCooktime(Utils.getConfig().getInt("furnaces." + furnaces + ".cooktime"));
            Furnaces.getFurnaces().add(info);
        }
    }

    public static void startCookTime(final Block block, final FurnaceInfo info) {
        final Furnace furnace = (Furnace)block.getState();
        if (info.getCooktime() != 0) {
            new BukkitRunnable(){

                public void run() {
                    if (furnace.getInventory().getSmelting() == null) {
                        this.cancel();
                        return;
                    }
                    if (furnace.getCookTime() < 0) {
                        furnace.setCookTime((short)0);
                        furnace.update();
                    }
                    Furnaces.setCookTime(block, Furnaces.getCappedTicks(200, info.getCooktime(), 0.5));
                }
            }.runTask(Main.getInstance());
        }
    }

    public static void setCookTime(Block block, int duration) {
        try {
            String version = Main.getInstance().getServer().getClass().getPackage().getName().split("\\.")[3];
            Class<?> CraftFurnace = Class.forName("org.bukkit.craftbukkit." + version + ".block.CraftFurnace");
            Method CraftFurnaceGetTileEntity = null;
            Class<?> TileEntityFurnace = Class.forName("net.minecraft.server." + version + ".TileEntityFurnace");
            Field TileEntityFurnace_cook_time_total = TileEntityFurnace.getDeclaredField("cookTimeTotal");
            TileEntityFurnace_cook_time_total.setAccessible(true);
            for (Class<?> clazz = CraftFurnace; clazz != null; clazz = clazz.getSuperclass()) {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (!method.getName().equals("getTileEntity") || method.getParameterTypes().length != 0) continue;
                    CraftFurnaceGetTileEntity = method;
                    CraftFurnaceGetTileEntity.setAccessible(true);
                }
            }
            if (!CraftFurnace.isAssignableFrom(block.getState().getClass())) {
                return;
            }
            Object tileEntityFurnace = CraftFurnaceGetTileEntity.invoke(block.getState());
            if (tileEntityFurnace == null || !TileEntityFurnace.isAssignableFrom(tileEntityFurnace.getClass())) {
                return;
            }
            TileEntityFurnace_cook_time_total.set(tileEntityFurnace, Math.max(0, duration));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getCappedTicks(int baseTicks, int baseModifier, double fractionModifier) {
        return Math.max(1, Math.min(32767, Furnaces.getModifiedTicks(baseTicks, baseModifier, fractionModifier)));
    }

    public static int getModifiedTicks(int baseTicks, int baseModifier, double fractionModifier) {
        if (baseModifier == 0) {
            return baseTicks;
        }
        if (baseModifier > 0) {
            return (int)((double)baseTicks / (1.0 + (double)baseModifier * fractionModifier));
        }
        return (int)((double)baseTicks * (1.0 + (double)(-baseModifier) * fractionModifier));
    }
}

