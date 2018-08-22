package jp.archesporeadventure.main.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.enchantments.CustomEnchantment;
import jp.archesporeadventure.main.enchantments.SpecialEnchantment;

public class EnchantmentUtil {
	
	private enum EnchantmentTasks {
		
		CURSED_ENCHANT(100, CustomEnchantment.CURSED), HEAVY_ENCHANT(50, CustomEnchantment.HEAVY), REPAIR_ENCHANT(200, CustomEnchantment.REPAIR), CLOAKING_ENCHANT(50, CustomEnchantment.CLOAKING);
		
		//Number of ticks for each effect to activate.
		private int effectCycleTicks;
		private CustomEnchantment effectEnchantment;
		
		private EnchantmentTasks(int ticks, CustomEnchantment enchantment){
			effectCycleTicks = ticks;
			effectEnchantment = enchantment;
		}
		
		private int getCycleTicks() {
			return this.effectCycleTicks;
		}
		
		private CustomEnchantment getEffectEnchantment() {
			return this.effectEnchantment;
		}
	}

	/**
	 * Runs the effects for all special passive enchantments, excluding soulbound and magical.
	 * @param tickCount Used to determine which effects to run
	 */
	public static void passiveEnchantmentEffects(int tickCount){
		
		//First get a list of all enchantment effects that proc this tick
		List<SpecialEnchantment> enchantmentEffects = new ArrayList<>();
		for (EnchantmentTasks task : EnchantmentTasks.values()) {
			if (tickCount % (task.getCycleTicks()) == 0) {
				enchantmentEffects.add(task.getEffectEnchantment().getEnchant());
			}
		}
		
		//If we have at least 1 effect this tick, get all players, loop through items and find the highest enchant levels for the effects that active this tick.
		if (enchantmentEffects.size() > 0) {
			Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers().toArray(new Player[0]);
			for (Player player : onlinePlayers) {
				Map<SpecialEnchantment, Integer> enchantmentMap = findHighestEnchantMap(player.getInventory().getContents(), enchantmentEffects.toArray(new SpecialEnchantment[enchantmentEffects.size()]));
				for (SpecialEnchantment enchant : enchantmentEffects) {
					if (enchantmentMap.containsKey(enchant)) {
						enchant.enchantmentEffect(player, enchantmentMap.get(enchant));
					}
				}
			}
		}
	}
	
	/**
	 * Gets the enchantment levels of a specified enchantment for a group of items.
	 * @param items items to loop through.
	 * @param specialEnchantment enchantment to look for.
	 * @return the map of the items and their levels for the enchantment.
	 */
	public static Map<ItemStack, Integer> getEnchantmentLevels(ItemStack[] items, SpecialEnchantment specialEnchantment) {
		
		Map<ItemStack, Integer> enchantmentMap = new HashMap<>();
		
		//Loop through each item, and for each item, loop through the enchantment we want to check.
		for (ItemStack loopItem : items) {
			if (loopItem != null) {
				if (loopItem.getEnchantments().containsKey(specialEnchantment)) {
					enchantmentMap.put(loopItem, loopItem.getEnchantments().get(specialEnchantment));
				}
				else {
					enchantmentMap.put(loopItem, 0);
				}
			}
		}
		
		return enchantmentMap;
	}
	
	/**
	 * Finds the highest enchantment level for each specified enchantment within a group of items.
	 * @param items items to loop through.
	 * @param enchantment enchantments to look for.
	 * @return the map of enchants found and levels
	 */
	public static Map<SpecialEnchantment, Integer> findHighestEnchantMap(ItemStack[] items, SpecialEnchantment ... specialEnchantments){
		
		Map<SpecialEnchantment, Integer> enchantmentMap = new HashMap<>();
		
		//Loop through each item, and for each item, loop through the enchantments we want to check.
		for (ItemStack loopItem : items) {
			if (loopItem != null) {
				for (SpecialEnchantment enchant : specialEnchantments) {
					if (loopItem.getEnchantments().keySet().contains(enchant)) {
						
						int itemEnchantLevel = loopItem.getEnchantments().get(enchant);
						
						if (enchantmentMap.containsKey(enchant)) {
							if (itemEnchantLevel > enchantmentMap.get(enchant)) {
								enchantmentMap.put(enchant, itemEnchantLevel);
							}
						}
						else {
							enchantmentMap.put(enchant, itemEnchantLevel);
						}
					}
				}
			}
		}
		
		return enchantmentMap;
	}
	
	public static List<SpecialEnchantment> getUniqueArmorEnchantments(ItemStack[] items){
		
		List<SpecialEnchantment> enchantmentList = new ArrayList<>();
		
		//Loop through each item, and for each item, check if there is a new enchantment.
		for (ItemStack loopItem : items) {
			if (loopItem != null) {
				for (Enchantment enchant : loopItem.getEnchantments().keySet()) {
					if (enchant instanceof SpecialEnchantment) {
						SpecialEnchantment specialEnchantment = (SpecialEnchantment) enchant;
						if ( (!enchantmentList.contains(specialEnchantment)) && (specialEnchantment.getItemTarget().equals(EnchantmentTarget.WEARABLE)) ) {
							enchantmentList.add(specialEnchantment);
						}
					}
				}
			}
		}
		
		return enchantmentList;
	}
}
