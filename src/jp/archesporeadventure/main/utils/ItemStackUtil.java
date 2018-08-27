package jp.archesporeadventure.main.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackUtil {

	/**
	 * Damages an ItemStack by a specified amount of durability.
	 * @param item The item to remove durability from.
	 * @param durability the amount of durability to remove.
	 * @return returns a boolean on whether or not the item was destroyed after taking damage.
	 */
	public static boolean damageItem(ItemStack item, int durability) {
		
		ItemMeta itemMeta = item.getItemMeta();
		if (itemMeta instanceof Damageable) {
			Damageable itemDamage = (Damageable) itemMeta;
			itemDamage.setDamage(durability + itemDamage.getDamage());
			item.setItemMeta(itemMeta);
			if (itemDamage.getDamage() > item.getType().getMaxDurability()) {
				item.setAmount(0);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the durability of an item
	 * @param item item to get durability for
	 * @return durability on item
	 */
	public static int getDurability(ItemStack item) {
		
		ItemMeta itemMeta = item.getItemMeta();
		if (itemMeta instanceof Damageable) {
			Damageable itemDamage = (Damageable) itemMeta;
			return itemDamage.getDamage();
		}
		return 0;
	}
	
	/**
	 * Removes an amount of item from an itemstack.
	 * @param item The item to remove from.
	 * @param amount the amount to remove.
	 */
	public static void removeAmount(ItemStack item, int amount){
		item.setAmount(Math.max(0, item.getAmount() - amount));
	}
	
	/**
	 * Checks if the specified item is a spawn egg.
	 * @param item item to check
	 * @return true or false
	 */
	public static boolean isSpawnEgg(ItemStack item) {
		return item.getType().toString().toUpperCase().matches(".*SPAWN_EGG");
	}
}
