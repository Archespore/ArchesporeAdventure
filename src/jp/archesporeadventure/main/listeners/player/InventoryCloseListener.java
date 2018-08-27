package jp.archesporeadventure.main.listeners.player;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.generation.generators.chests.ChestGenerator;
import jp.archesporeadventure.main.menus.InventoryMenuController;

public class InventoryCloseListener implements Listener{

	@EventHandler
	public void chestEmptiedEvent(InventoryCloseEvent event){
		
		Inventory closedInventory = event.getInventory();
		if (closedInventory != null && closedInventory.getType().equals(InventoryType.CHEST)) {
			
			Location chestLocation = closedInventory.getLocation();
			if (chestLocation != null) {
				Block chest = chestLocation.getBlock();
				boolean isEmpty = true;
				for (ItemStack item : closedInventory.getContents()) {
					if (item != null) { 
						isEmpty = false;
						break;
					}
				}
				if (isEmpty) {
					ChestGenerator chestGenerator = ArchesporeAdventureMain.getChestGenerator(chest.getWorld());
					if (chestGenerator.doesChestExist(chest.getLocation())) {
						chestGenerator.removeGeneratedChest(chest.getLocation());
					}
				}
			}
		}
		InventoryMenuController menuController = ArchesporeAdventureMain.getMenuController();
		if (menuController.getInventoryMenu(closedInventory) != null) {
			menuController.removeInventoryMenu(closedInventory);
		}
	}
}
