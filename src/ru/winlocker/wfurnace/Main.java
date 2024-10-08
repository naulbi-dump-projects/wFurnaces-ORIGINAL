package ru.winlocker.wfurnace;

import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ru.winlocker.wfurnace.Execute;
import ru.winlocker.wfurnace.FurnaceInfo;
import ru.winlocker.wfurnace.Furnaces;

public class Main extends JavaPlugin implements Listener {

    /* Leaked by https://t.me/leak_mine
       Сурцы плагина были слиты телеграмм каналом https://t.me/leak_mine
    */	
	
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        this.getServer().getPluginManager().registerEvents(this, this);
        Furnaces.loadFurnaces();
        this.getCommand("wfurnace").setExecutor(new Execute());
        this.getCommand("wfurnace").setTabCompleter(new Execute());
    }

    @EventHandler
    public void onMove(InventoryClickEvent e) {
        Furnace furnace;
        FurnaceInfo info;
        if (e.getInventory().getType() == InventoryType.FURNACE && (info = Furnaces.getFurnace((furnace = (Furnace)e.getInventory().getHolder()).getBlock())) != null && info.getCooktime() > 0) {
            Furnaces.startCookTime(furnace.getBlock(), info);
        }
    }

    @EventHandler
    public void onSpawn(ItemSpawnEvent e) {
        FurnaceInfo info = Furnaces.getFurnace(e.getEntity().getItemStack());
        if (info != null) {
            e.getEntity().setItemStack(info.getItem());
        }
    }

    @EventHandler
    public void onBurn(FurnaceBurnEvent e) {
        if (!(e.getBlock().getState() instanceof Furnace)) {
            return;
        }
        FurnaceInfo info = Furnaces.getFurnace(e.getBlock());
        if (info != null) {
            if (info.getBurnTime() != 0) {
                e.setBurnTime(e.getBurnTime() * info.getBurnTime());
            }
            if (info.getCooktime() > 0) {
                Furnaces.startCookTime(e.getBlock(), info);
            }
        }
    }

    @EventHandler
    public void onSmelt(FurnaceSmeltEvent e) {
        if (!(e.getBlock().getState() instanceof Furnace)) {
            return;
        }
        FurnaceInfo info = Furnaces.getFurnace(e.getBlock());
        if (info != null) {
            if (info.getCooktime() > 0) {
                Furnaces.startCookTime(e.getBlock(), info);
            }
            if (info.getResult() != 0) {
                ItemStack item = e.getResult();
                item.setAmount(item.getAmount() * info.getResult());
                e.setResult(item);
            }
        }
    }
}

