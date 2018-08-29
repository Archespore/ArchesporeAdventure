package jp.archesporeadventure.main.menus.blocks.furnace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.controllers.FurnaceController;
import jp.archesporeadventure.main.menus.InventoryExchangeMenu;
import jp.archesporeadventure.main.menus.InventoryMenu;
import jp.archesporeadventure.main.menus.InventoryMenuController;
import net.md_5.bungee.api.ChatColor;

public class SmeltingMenuInventory extends InventoryMenu {
	
	private FurnaceController furnaceController = ArchesporeAdventureMain.getFurnaceController();
	private Map<Material, ShapelessRecipe> recipeMap = new HashMap<>();
	
	public SmeltingMenuInventory(Inventory inventory) {
		super(inventory);
	}

	public void populateInventory(Player player) {
		int inventorySlot = 0;
		for (ShapelessRecipe smeltingRecipe : furnaceController.getSmeltingRecipes()) {
			Material recipeResult = smeltingRecipe.getResult().getType();
			List<String> itemLore = new ArrayList<>();
			List<Material> countedItems = new ArrayList<>();
			itemLore.add(ChatColor.GRAY.toString() + "Ingredients:");
			
			smeltingRecipe.getIngredientList().forEach( ingredient -> {
				
				Material ingredientMaterial = ingredient.getType();
				if (!countedItems.contains(ingredientMaterial)) {
					countedItems.add(ingredientMaterial);
					int ingredientAmount = smeltingRecipe.getIngredientList().stream().filter(itemStack -> itemStack.getType().equals(ingredient.getType())).collect(Collectors.toList()).size();
					itemLore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + " - " + ingredientAmount + " x " + StringUtils.capitalize(ingredient.getType().toString().toLowerCase().replace('_', ' ')));
				}
			});

			inventoryMenu.setItem(inventorySlot, createMenuItem(recipeResult, ChatColor.DARK_RED + "Smelt " + recipeResult.toString().toLowerCase().replace('_', ' '), itemLore));
			recipeMap.put(recipeResult, smeltingRecipe);
			inventorySlot++;
		}

		inventoryMenu.setItem(8, createMenuItem(Material.BARRIER, ChatColor.RED + "Close Menu", 
				Arrays.asList(ChatColor.GRAY + "Close the menu.")));
	}

	public void clickActions(Inventory inventory, Player player, ItemStack itemStack) {
		Material itemMaterial = itemStack.getType();
		if (itemMaterial.equals(Material.BARRIER)) {
			player.closeInventory();
		}
		else if (recipeMap.containsKey(itemMaterial)){
			InventoryMenuController menuController = ArchesporeAdventureMain.getMenuController();
			Inventory inventoryMenu  = Bukkit.createInventory(null, 9, "Select Amount");
			ShapelessRecipe exchangeRecipe = recipeMap.get(itemMaterial);
			InventoryExchangeMenu exchangeMenu = new InventoryExchangeMenu(inventoryMenu, exchangeRecipe);
			exchangeMenu.populateInventory(player);
			menuController.registerInventoryMenu(inventoryMenu, exchangeMenu);
			player.openInventory(inventoryMenu);
		}
	}
}
