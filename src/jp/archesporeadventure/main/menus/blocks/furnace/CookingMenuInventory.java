package jp.archesporeadventure.main.menus.blocks.furnace;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

public class CookingMenuInventory extends InventoryMenu {
	
	private FurnaceController furnaceController = ArchesporeAdventureMain.getFurnaceController();
	private Map<Material, ShapelessRecipe> recipeMap = new HashMap<>();
	
	public CookingMenuInventory(Inventory inventory) {
		super(inventory);
	}

	public void populateInventory(Player player) {
		int inventorySlot = 2;
		for (ShapelessRecipe smeltingRecipe : furnaceController.getCookingRecipes()) {
			Material recipeResult = smeltingRecipe.getResult().getType();
			inventoryMenu.setItem(inventorySlot, createMenuItem(recipeResult, ChatColor.GOLD + "Prepare " + recipeResult.toString().toLowerCase().replace('_', ' ')));
			recipeMap.put(recipeResult, smeltingRecipe);
			if (inventorySlot % 9 == 6) {
				inventorySlot += 5;
			}
			else {
				inventorySlot++;
			}
		}

		inventoryMenu.setItem(31, createMenuItem(Material.BARRIER, ChatColor.RED + "Close Menu", 
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
