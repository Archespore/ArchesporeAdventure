package jp.archesporeadventure.main.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.netty.util.internal.ThreadLocalRandom;
import jp.archesporeadventure.main.ArchesporeAdventureMain;
import net.md_5.bungee.api.ChatColor;

public class MagicalItemsController {
	
	private Map<String, ItemStack> magicalItemsMap = new HashMap<>();
	private Map<Integer, ItemStack> magicalScrollsMap = new HashMap<>();
	
	private File magicItemsFile;
	private FileConfiguration magicItemsYML;
	
	/**
	 * Registers a magic item
	 */
	public void registerMagicItem(String name, ItemStack magicItem){
		magicalItemsMap.put(name, magicItem);
		magicItemsYML.set("MagicItems." + name, magicItem);
	}
	
	/**
	 * Registers a magical scroll
	 */
	public void registerMagicScroll(Integer effectLevel, ItemStack scroll){
		magicalScrollsMap.put(effectLevel, scroll);
	}
	
	/**
	 * Checks if a magical item is registerd with the given string.
	 */
	public boolean doesItemExist(String name){
		return magicalItemsMap.containsKey(name);
	}
	
	/**
	 * Returns a set of all registered magical item names.
	 */
	public Set<String> magicalItemKeys(){
		return magicalItemsMap.keySet();
	}
	
	/**
	 * Returns the amount of registered magical scrolls
	 * @return the amount of magical scrolls registered
	 */
	public int getRegisteredScrollsAmount() {
		return magicalScrollsMap.size();
	}
	
	/**
	 * Generates a magic item with the registered name
	 */
	public ItemStack generateItem(String name, boolean stackable){
		if (doesItemExist(name)) {
			
			ItemStack newMagicItem = magicalItemsMap.get(name).clone();
			
			//If stackable is equal to false, we generate a random localized name so it can't stack with other items.
			if (stackable == false) {
				ItemMeta magicItemMeta = newMagicItem.getItemMeta();
				magicItemMeta.setLocalizedName(String.valueOf(ThreadLocalRandom.current().nextDouble(100)));
				newMagicItem.setItemMeta(magicItemMeta);
			}
			
			return newMagicItem;
		}
		return null;
	}
	
	/**
	 * Generates a magical scroll with the specified effect level
	 * @param effectLevel the effect level of the scroll
	 * @return the new magical scroll
	 */
	public ItemStack generateScroll(Integer effectLevel) {
		return generateScroll(effectLevel, 1);
	}
	
	/**
	 * Generates a magical scroll with the specified effect level
	 * @param effectLevel the effect level of the scroll
	 * @param amount the amount of the scroll to generate
	 * @return the new magical scroll
	 */
	public ItemStack generateScroll(Integer effectLevel, int amount) {
		ItemStack generatedScroll = magicalScrollsMap.get(effectLevel).clone();
		generatedScroll.setAmount(amount);
		generatedScroll.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, effectLevel);
		return generatedScroll;
	}
	
	/**
	 * Loads magic items from a file
	 */
	public void loadMagicItems(){
		magicItemsFile = new File(ArchesporeAdventureMain.getPlugin().getDataFolder(), "MagicalItems.yml");
		
		if (!magicItemsFile.exists()) {
			magicItemsFile.getParentFile().mkdirs();
			ArchesporeAdventureMain.getPlugin().saveResource("MagicalItems.yml", true);
		}
		
		magicItemsYML = new YamlConfiguration();
		try {
			magicItemsYML.load(magicItemsFile);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		for (String item : magicItemsYML.getConfigurationSection("MagicItems").getKeys(false)) {
			registerMagicItem(item, magicItemsYML.getItemStack("MagicItems." + item));
		}
		
		int effectLevel = 1;
		for (String item : magicItemsYML.getConfigurationSection("MagicScrolls").getKeys(false)) {
			ConfigurationSection loopScrollSection = magicItemsYML.getConfigurationSection("MagicScrolls." + item);
			boolean obfuscated = loopScrollSection.getBoolean("Obfuscated");
			ItemStack magicalScroll = new ItemStack(Material.PAPER, 1);
			ItemMeta magicalScrollMeta = magicalScroll.getItemMeta();
			List<String> magicalScrollLore = new ArrayList<>(Arrays.asList(""));
			magicalScrollLore.addAll(loopScrollSection.getStringList("Lore"));
			
			magicalScrollMeta.setDisplayName(ChatColor.DARK_AQUA + (obfuscated ? ChatColor.MAGIC.toString() : "") + item.replace('_', ' '));
			magicalScrollMeta.setLore(magicalScrollLore);
			magicalScrollMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			magicalScroll.setItemMeta(magicalScrollMeta);
			registerMagicScroll(effectLevel, magicalScroll);
			effectLevel++;
		}
	}
	
	/**
	 * Saves magic items to a file.
	 */
	public void saveMagicItems(){

		try {
			magicItemsYML.save(magicItemsFile);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

	}
}
