
package ru.winlocker.wfurnace.tools;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.winlocker.wfurnace.tools.Utils;

public class ItemBuilder {

    /* Leaked by https://t.me/leak_mine
       Сурцы плагина были слиты телеграмм каналом https://t.me/leak_mine
    */	
	
    private ItemStack item;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public ItemBuilder setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder setDurability(short durability) {
        this.item.setDurability(durability);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        ItemMeta meta = this.item.getItemMeta();
        meta.addEnchant(enchantment, level, true);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder enchantall(int level) {
        for (Enchantment enchantment : Enchantment.values()) {
            this.enchant(enchantment, level);
        }
        return this;
    }

    public ItemBuilder flag(ItemFlag flag) {
        ItemMeta meta = this.item.getItemMeta();
        meta.addItemFlags(new ItemFlag[]{flag});
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder flagall() {
        for (ItemFlag flag : ItemFlag.values()) {
            this.flag(flag);
        }
        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(Utils.color(name));
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setLore(Utils.color(lore));
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLore(String line) {
        final ItemMeta meta = this.item.getItemMeta();
        List<String> list;
        if (meta.hasLore()) {
            list = (List<String>)meta.getLore();
        }
        else {
            list = new ArrayList<String>();
        }
        list.add(Utils.color(line));
        meta.setLore(list);
        this.item.setItemMeta(meta);
        return this;
    }
    
    public ItemBuilder removeLore(int page) {
        final ItemMeta meta = this.item.getItemMeta();
        if (!meta.hasLore()) {
            return this;
        }
        final List<String> list = (List<String>)meta.getLore();
        if (page > list.size()) {
            return this;
        }
        list.remove(page);
        meta.setLore(Utils.color(list));
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        return this.item;
    }
}

