package jp.archesporeadventure.main.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.menus.InventoryMenuController;
import jp.archesporeadventure.main.menus.SmeltingMenuInventory;

public class PlayerInteractEntityListener implements Listener {

	@EventHandler
	public void playerInteractEntityEvent(PlayerInteractEntityEvent event) {
		
		Player player = event.getPlayer();
		Entity eventEntity = event.getRightClicked();
		if (eventEntity != null && eventEntity.getType().equals(EntityType.MINECART_FURNACE)) {
			event.setCancelled(true);
			Inventory smeltingInventory = Bukkit.createInventory(null, 9, "Smelting Menu");
			InventoryMenuController menuController = ArchesporeAdventureMain.getMenuController();
			menuController.registerInventoryMenu(smeltingInventory, new SmeltingMenuInventory());
			menuController.getInventoryMenu(smeltingInventory).populateInventory(player, smeltingInventory);
			player.openInventory(smeltingInventory);
		}
	}
}
