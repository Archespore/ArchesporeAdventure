package jp.archesporeadventure.main.utils;

import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class ItemStackUtil {

	/**
	 * Damages an ItemStack by a specified amount of durability.
	 * @param item The item to remove durability from.
	 * @param durability the amount of durability to remove.
	 * @return returns a boolean on whether or not the item was destroyed after taking damage.
	 */
	public static boolean damageItem(ItemStack item, int durability){
		if (item != null && EnchantmentTarget.BREAKABLE.includes(item)) {
			item.setDurability((short)(durability + item.getDurability()));
			if (item.getDurability() > item.getType().getMaxDurability()) {
				item.setAmount(0);
				return true;
			}
		}
		return false;
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
