package jp.archesporeadventure.main.menus;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class InventoryMenu {

	public abstract void populateInventory(Player player, Inventory inventory);
	
	public abstract void clickActions(Player player, Material material);
	
	protected ItemStack createMenuItem(Material iconMaterial, String iconName, List<String> iconLore) {
		return createMenuItem(iconMaterial, iconName, false, iconLore);
	}
	
	protected ItemStack createMenuItem(Material iconMaterial, String iconName, boolean enchanted, List<String> iconLore) {
		ItemStack inventoryItem = new ItemStack(iconMaterial, 1);
		ItemMeta inventoryItemMeta = inventoryItem.getItemMeta();
		inventoryItemMeta.setDisplayName(iconName);
		inventoryItemMeta.setLore(iconLore);
		inventoryItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
		if (enchanted) { inventoryItemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false); }
		inventoryItem.setItemMeta(inventoryItemMeta);
		return inventoryItem;
	}
}
