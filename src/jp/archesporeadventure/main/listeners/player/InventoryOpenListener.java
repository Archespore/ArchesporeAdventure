package jp.archesporeadventure.main.listeners.player;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.generation.generators.chests.ChestGenerator;

public class InventoryOpenListener implements Listener{

	@EventHandler
	public void chestOpenedEvent(InventoryOpenEvent event){
		
		Inventory openedInventory = event.getInventory();
		if (openedInventory != null && openedInventory.getType().equals(InventoryType.CHEST)) {
			
			Location chestLocation = openedInventory.getLocation();
			if (chestLocation != null) {
				Block chest = chestLocation.getBlock();
				ChestGenerator chestGenerator = ArchesporeAdventureMain.getChestGenerator(chest.getWorld());
				if (chestGenerator.doesChestExist(chest.getLocation())) {
					
					int lootAmount = 4;
					
					if (event.getPlayer().hasPotionEffect(PotionEffectType.LUCK)) { lootAmount += event.getPlayer().getPotionEffect(PotionEffectType.LUCK).getAmplifier() + 1; } 
					chestGenerator.getChestAtLocation(openedInventory.getLocation()).generateLoot(lootAmount);
				}
			}
		}
	}
}
