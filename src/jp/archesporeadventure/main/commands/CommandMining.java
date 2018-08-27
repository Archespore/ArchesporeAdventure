package jp.archesporeadventure.main.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.menus.InventoryMenuController;
import jp.archesporeadventure.main.menus.MiningMenuInventory;

public class CommandMining implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player) {
			
			Player player = (Player) sender;
			
			Inventory inventoryMenu = Bukkit.createInventory(null, 36, "Mining Menu");
			InventoryMenuController menuController = ArchesporeAdventureMain.getMenuController();
			menuController.registerInventoryMenu(inventoryMenu, new MiningMenuInventory());
			menuController.getInventoryMenu(inventoryMenu).populateInventory(player, inventoryMenu);
			
			player.openInventory(inventoryMenu);
		}
		return true;
	}

}
