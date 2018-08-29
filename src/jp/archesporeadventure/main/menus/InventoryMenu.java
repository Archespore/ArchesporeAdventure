package jp.archesporeadventure.main.menus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class InventoryMenu {
	
	protected Inventory inventoryMenu;
	
	public InventoryMenu(Inventory inventory) {
		inventoryMenu = inventory;
	}
	
	public abstract void populateInventory(Player player);

	public abstract void clickActions(Inventory inventory, Player player, ItemStack itemStack);
	
	public void openInventoryMenu(Player player) {
		player.openInventory(inventoryMenu);
	}
	
	public ItemStack createMenuItem(Material iconMaterial, String iconName) {
		return createMenuItem(iconMaterial, 1, iconName, false, new ArrayList<>());
	}
	
	public ItemStack createMenuItem(Material iconMaterial, String iconName, List<String> iconLore) {
		return createMenuItem(iconMaterial, 1, iconName, false, iconLore);
	}
	
	public ItemStack createMenuItem(Material iconMaterial, String iconName, boolean enchanted, List<String> iconLore) {
		return createMenuItem(iconMaterial, 1, iconName, enchanted, iconLore);
	}
	
	public ItemStack createMenuItem(Material iconMaterial, int itemAmount, String iconName, boolean enchanted, List<String> iconLore) {
		ItemStack inventoryItem = new ItemStack(iconMaterial, itemAmount);
		ItemMeta inventoryItemMeta = inventoryItem.getItemMeta();
		inventoryItemMeta.setDisplayName(iconName);
		inventoryItemMeta.setLore(iconLore);
		inventoryItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
		if (enchanted) { inventoryItemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false); }
		inventoryItem.setItemMeta(inventoryItemMeta);
		return inventoryItem;
	}
}
