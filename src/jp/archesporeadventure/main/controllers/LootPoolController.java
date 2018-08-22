package jp.archesporeadventure.main.controllers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.generation.itempools.LootPool;
import net.md_5.bungee.api.ChatColor;
import jp.archesporeadventure.main.generation.itempools.DefaultPoolFiles;

public class LootPoolController {
	
	private ArchesporeAdventureMain plugin;
	
	private Map<String, LootPool> lootPoolMap = new HashMap<>();
	private Map<DefaultPoolFiles, File> defaultFilesMap = new HashMap<>();
	private Map<DefaultPoolFiles, FileConfiguration> defaultFileConfigurationMap = new HashMap<>();
	
	/**
	 * Default constructor.
	 */
	public LootPoolController(ArchesporeAdventureMain plugin) {
		this.plugin = plugin;
		loadItemFiles();
		if (loadLootPools()) {
			System.out.println("Loaded the following loot pools: " + lootPoolMap.keySet().toString());
		}
	}
	
	/**
	 * Registers and item pool with the specified name, will not overwrite an existing pool with the same name.
	 * @param poolName name of the item pool.
	 * @param pool Item pool to register
	 * @return true if the item pool was added, false if a pool with the name already exists.
	 */
	public boolean registerLootPool(String poolName, LootPool pool){
		
		if (!doesLootPoolExist(poolName)) {
			lootPoolMap.put(poolName, pool);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the item pool with the specified name exists in this controller.
	 * @param itemPoolName the item pool to get.
	 * @return true/false
	 */
	public boolean doesLootPoolExist(String itemPoolName) {
		return lootPoolMap.containsKey(itemPoolName);
	}
	
	/**
	 * Gets the registered item pool with the specified name.
	 * @param itemPoolName the item pool to get.
	 * @return the registered item pool, or null if it doesn't exist.
	 */
	public LootPool getRegisteredLootPool(String itemPoolName) {
		
		if (doesLootPoolExist(itemPoolName)) {
			return lootPoolMap.get(itemPoolName);
		}
		return null;
	}
	
	/**
	 * Gets the Map of all LootPool listings for this controller.
	 * @param poolType default pool type to get config for.
	 * @return the YML file for the specified pool type.
	 */
	public Map<String, LootPool> getLootPoolMap() {
		return lootPoolMap;
	}
	
	/**
	 * Gets the YML file for the specified default pool type.
	 * @param poolType default pool type to get config for.
	 * @return the YML file for the specified pool type.
	 */
	public FileConfiguration getYMLConfig(DefaultPoolFiles poolType) {
		return defaultFileConfigurationMap.get(poolType);
	}
	
	/**
	 * Saves the default ItemPools to a file.
	 */
	public void saveItems(){
		
		try {
			for (Entry<DefaultPoolFiles, File> defaultEntry : defaultFilesMap.entrySet()) {
				if (defaultEntry.getKey().equals(DefaultPoolFiles.WORLD) || defaultEntry.getKey().equals(DefaultPoolFiles.CUSTOM)) {
					getYMLConfig(defaultEntry.getKey()).save(defaultEntry.getValue());
				}
			}
		} 
		catch (Exception e) { e.printStackTrace(); }
	}
	
	/**
	 * Loads the default item pools and the configs for them.
	 */
	private boolean loadItemFiles(){
		
		boolean loadedAllFiles = true;
		for (DefaultPoolFiles file : DefaultPoolFiles.values()) {
			
			File loadedFile = new File(plugin.getDataFolder(), file.getFileName());
			if (!loadedFile.exists()) {
				loadedFile.getParentFile().mkdirs();
				plugin.saveResource(loadedFile.getName(), true);
			}
			
			if (!defaultFilesMap.containsKey(file)) { defaultFilesMap.put(file, loadedFile); }
			else { loadedAllFiles = false; }
			
			FileConfiguration loadedConfig = new YamlConfiguration();
			try { loadedConfig.load(loadedFile); }
			catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
			
			if (!defaultFileConfigurationMap.containsKey(file)) { defaultFileConfigurationMap.put(file, loadedConfig); }
			else { loadedAllFiles = false; }
			
		}
		return loadedAllFiles;
	}
	
	@SuppressWarnings("unchecked")
	private boolean loadLootPools() {
		
		boolean loadedAllPools = true;
		FileConfiguration configYML = getYMLConfig(DefaultPoolFiles.WORLD);
		for (String defaultItemPool : configYML.getConfigurationSection("ItemPools").getKeys(false)) {
			
			List<ItemStack> defaultPoolItems = (List<ItemStack>) configYML.getList("ItemPools." + defaultItemPool + ".Items");
			LootPool newDefaultPool = new LootPool(defaultItemPool, ChatColor.translateAlternateColorCodes('&', configYML.getString("ItemPools." + defaultItemPool + ".DisplayName")), defaultPoolItems.toArray(new ItemStack[defaultPoolItems.size()]));
			loadedAllPools = registerLootPool(newDefaultPool.getPoolName(), newDefaultPool);
		}
		return loadedAllPools;
	}
}
