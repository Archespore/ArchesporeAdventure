package jp.archesporeadventure.main.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.menus.InventoryMenu;
import jp.archesporeadventure.main.menus.InventoryMenuController;

public class InventoryClickListener implements Listener {

	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent event) {
		
		//First we get the details of the event: Player, inventory name, and clicked item
		Player player = (Player) event.getWhoClicked();
		ItemStack eventItem = event.getCurrentItem();
		Inventory eventInventory = event.getInventory();
		InventoryMenuController menuController = ArchesporeAdventureMain.getMenuController();
		InventoryMenu menuType = menuController.getInventoryMenu(eventInventory);
		if (menuType != null) {
			event.setCancelled(true);
			menuType.clickActions(eventInventory, player, eventItem);
			player.updateInventory();
		}
	}
}
