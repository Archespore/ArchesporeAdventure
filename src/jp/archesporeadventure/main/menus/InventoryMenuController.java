package jp.archesporeadventure.main.menus;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.Inventory;

public class InventoryMenuController {

	private Map<Inventory, InventoryMenu> openInventoryMenus = new HashMap<>();
	
	/**
	 * adds an inventory to the controller
	 * @param inventory the inventory to add.
	 * @param menuType the menu to register to the inventory.
	 */
	public void registerInventoryMenu(Inventory inventory, InventoryMenu menuType) {
		openInventoryMenus.put(inventory, menuType);
	}
	
	/**
	 * Removes a registered inventory from the controller.
	 * @param inventory the inventory to remove from the controller.
	 */
	public void removeInventoryMenu(Inventory inventory) {
		openInventoryMenus.remove(inventory);
	}
	
	/**
	 * Gets the InventoryMenu for the specified inventory.
	 * @param inventory the inventory to get data for.
	 * @return the InventoryMenu registered to the Inventory, or null if the inventory is not a menu.
	 */
	public InventoryMenu getInventoryMenu(Inventory inventory) {
		return openInventoryMenus.get(inventory);
	}
}
