package jp.archesporeadventure.main.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.ArchesporeAdventureMain;

public class InventoryClickListener implements Listener {

	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent event) {
		
		//First we get the details of the event: Player, inventory name, and clicked item
		Player player = (Player) event.getWhoClicked();
		String eventInventoryName = event.getInventory().getName();
		ItemStack eventItem = event.getCurrentItem();
		if (eventItem != null && eventInventoryName.equals("Mining Menu")) {
			event.setCancelled(true);
			ArchesporeAdventureMain.getMiningMenu().clickActions(player, eventItem.getType());
			player.updateInventory();
		}
		if (eventItem != null && eventInventoryName.equals("Fishing Menu")) {
			event.setCancelled(true);
			ArchesporeAdventureMain.getFishingMenu().clickActions(player, eventItem.getType());
			player.updateInventory();
		}
	}
}
