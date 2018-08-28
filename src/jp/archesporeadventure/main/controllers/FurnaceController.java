package jp.archesporeadventure.main.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import jp.archesporeadventure.main.ArchesporeAdventureMain;

public class FurnaceController {

	private Map<String, ShapelessRecipe> registeredSmeltingRecipes = new HashMap<>();
	private Map<String, ShapelessRecipe> registeredCookingRecipes = new HashMap<>();
	
	private File configFile;
	private FileConfiguration configFileYML;
	
	private ArchesporeAdventureMain plugin;
	
	public FurnaceController(ArchesporeAdventureMain plugin) {
		this.plugin = plugin;
		loadFiles();
		loadFileConfigurations();
	}
	
	public ShapelessRecipe getSmeltingRecipe(String name) {
		return registeredSmeltingRecipes.get(name);
	}
	
	public ShapelessRecipe getCookingRecipe(String name) {
		return registeredCookingRecipes.get(name);
	}
	
	private void loadFiles() {
		
		configFile = new File(plugin.getDataFolder(), "FurnaceConfig.yml");
		if (!configFile.exists()) {
			plugin.getDataFolder().getParentFile().mkdirs();
			plugin.saveResource("FurnaceConfig.yml", true);
		}
		
		configFileYML = new YamlConfiguration();
		try { configFileYML.load(configFile); } 
		catch (Exception e) { e.printStackTrace(); }
	}
	
	private void loadFileConfigurations() {
		ConfigurationSection smeltingSection = configFileYML.getConfigurationSection("SmeltingRecipes");
		ConfigurationSection cookingSection = configFileYML.getConfigurationSection("CookingRecipes");
		for (String recipeName : smeltingSection.getKeys(false)) {
			ConfigurationSection recipeSection = smeltingSection.getConfigurationSection(recipeName);
			String[] stringResults = recipeSection.getString("ItemResults").split(":");
			List<String[]> recipeIngredients = new ArrayList<>();
			for (String ingredients : recipeSection.getStringList("Ingredients")) {
				recipeIngredients.add(ingredients.split(":"));
			}
			ShapelessRecipe furnaceRecipe = new ShapelessRecipe(new NamespacedKey(plugin, recipeName), new ItemStack(Material.valueOf(stringResults[0]), Integer.parseInt(stringResults[1])));
			for (String[] ingredients : recipeIngredients) {
				furnaceRecipe.addIngredient(Integer.parseInt(ingredients[1]), Material.valueOf(ingredients[0]));
			}
			registeredSmeltingRecipes.put(recipeName, furnaceRecipe);
		}
		for (String recipeName : cookingSection.getKeys(false)) {
			ConfigurationSection recipeSection = cookingSection.getConfigurationSection(recipeName);
			String[] stringResults = recipeSection.getString("ItemResults").split(":");
			List<String[]> recipeIngredients = new ArrayList<>();
			for (String ingredients : recipeSection.getStringList("Ingredients")) {
				recipeIngredients.add(ingredients.split(":"));
			}
			ShapelessRecipe furnaceRecipe = new ShapelessRecipe(new NamespacedKey(plugin, recipeName), new ItemStack(Material.valueOf(stringResults[0]), Integer.parseInt(stringResults[1])));
			for (String[] ingredients : recipeIngredients) {
				furnaceRecipe.addIngredient(Integer.parseInt(ingredients[1]), Material.valueOf(ingredients[0]));
			}
			registeredCookingRecipes.put(recipeName, furnaceRecipe);
		}
	}
}
