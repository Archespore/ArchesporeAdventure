package jp.archesporeadventure.main.menus.blocks.furnace;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.menus.InventoryMenu;
import jp.archesporeadventure.main.menus.InventoryMenuController;
import net.md_5.bungee.api.ChatColor;

public class FurnaceMenuInventory extends InventoryMenu {

	public FurnaceMenuInventory(Inventory inventory) {
		super(inventory);
	}

	public void populateInventory(Player player) {
		inventoryMenu.setItem(0, createMenuItem(Material.FURNACE, ChatColor.GRAY + "Smelting Menu"));
		inventoryMenu.setItem(1, createMenuItem(Material.BOWL, ChatColor.GOLD + "Cooking Menu"));
		
		inventoryMenu.setItem(8, createMenuItem(Material.BARRIER, ChatColor.RED + "Close Menu", 
				Arrays.asList(ChatColor.GRAY + "Close the menu.")));
	}

	public void clickActions(Inventory inventory, Player player, ItemStack itemStack) {
		Material itemMaterial = itemStack.getType();
		if (itemMaterial.equals(Material.BARRIER)) {
			player.closeInventory();
		}
		else if (itemMaterial.equals(Material.FURNACE)) {
			Inventory smeltingInventory = Bukkit.createInventory(null, 9, "Smelting Menu");
			InventoryMenuController menuController = ArchesporeAdventureMain.getMenuController();
			menuController.registerInventoryMenu(smeltingInventory, new SmeltingMenuInventory(smeltingInventory));
			menuController.getInventoryMenu(smeltingInventory).populateInventory(player);
			player.openInventory(smeltingInventory);
		}
		else if (itemMaterial.equals(Material.BOWL)){
			Inventory cookingInventory = Bukkit.createInventory(null, 36, "Cooking Menu");
			InventoryMenuController menuController = ArchesporeAdventureMain.getMenuController();
			menuController.registerInventoryMenu(cookingInventory, new CookingMenuInventory(cookingInventory));
			menuController.getInventoryMenu(cookingInventory).populateInventory(player);
			player.openInventory(cookingInventory);
		}
	}

}
