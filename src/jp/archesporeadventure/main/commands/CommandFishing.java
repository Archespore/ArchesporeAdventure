package jp.archesporeadventure.main.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import jp.archesporeadventure.main.ArchesporeAdventureMain;

public class CommandFishing implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player) {
			
			Player player = (Player) sender;
			
			Inventory inventoryMenu = Bukkit.createInventory(null, 27, "Fishing Menu");
			ArchesporeAdventureMain.getFishingMenu().populateInventory(player, inventoryMenu);
			
			player.openInventory(inventoryMenu);
		}
		return true;
	}

}
