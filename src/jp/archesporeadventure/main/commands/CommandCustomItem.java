package jp.archesporeadventure.main.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.generation.itempools.DefaultPoolFiles;

public class CommandCustomItem implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			
			Player player = (Player) sender;
			ItemStack customItem = player.getInventory().getItemInMainHand().clone();
			ArchesporeAdventureMain.getLootPoolController().getYMLConfig(DefaultPoolFiles.CUSTOM).set("ItemPools", customItem);
			ArchesporeAdventureMain.getLootPoolController().saveItems();
		}
		return true;
	}

}
