package jp.archesporeadventure.main.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class TreasureMapUtil {
	
	/**
	 * Generates a treasure map with the specified map level
	 * @param treasureMapLevel the map level to generate
	 * @param world the world to generate the map for.
	 * @return the itemstack of the generated map
	 */
	public static ItemStack generateMap(World world, int treasureMapLevel) {
		ItemStack newTreasureMap = new ItemStack(Material.MAP, 1);
		ItemMeta newTeasureMapMeta = newTreasureMap.getItemMeta();
		//+1 is because the upper end is exclusive, meaning not included.
		int xPos = (ThreadLocalRandom.current().nextInt(-1280, 1280 + 1));
		int zPos = (ThreadLocalRandom.current().nextInt(-1280, 1280 + 1));
		int yPos = world.getHighestBlockYAt(xPos, zPos);
		newTeasureMapMeta.setLocalizedName(xPos + "," + yPos + "," + zPos);
		List<String> mapLoreList = new ArrayList<>();
		mapLoreList.add("");
		if (newTreasureMap.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) <= 1) {
			mapLoreList.add(ChatColor.GRAY.toString() + "X: " + xPos);
			mapLoreList.add(ChatColor.GRAY.toString() + "Z: " + zPos);
		}
		if (newTreasureMap.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) <= 2) {
			mapLoreList.add(Math.min(mapLoreList.size(), 2), ChatColor.GRAY.toString() + "Y: " + yPos);
			mapLoreList.add("");
			mapLoreList.add(ChatColor.GRAY.toString() + "Biome: " + world.getBlockAt(xPos, yPos, zPos).getBiome());
			mapLoreList.add("");
		}
		mapLoreList.add(ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "(Opened Map)");
		newTeasureMapMeta.setLore(mapLoreList);
		newTreasureMap.setItemMeta(newTeasureMapMeta);
		return newTreasureMap;
	}
}
