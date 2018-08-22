package jp.archesporeadventure.main.commands.magicitems;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.controllers.MagicalItemsController;
import net.md_5.bungee.api.ChatColor;

public class CommandRegisterMagicItem implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		MagicalItemsController magicItemController = ArchesporeAdventureMain.getMagicItemController();
		
		if (sender instanceof Player){
			
			Player player = (Player) sender;
			
			ItemStack heldItem = player.getInventory().getItemInMainHand();
			if (magicItemController.doesItemExist(args[0])) {
				player.sendMessage(ChatColor.RED + "An item with the name: " + args[0] + " already exists.");
			}
			else{
				magicItemController.registerMagicItem(args[0], heldItem);
				player.getInventory().setItemInMainHand(null);
				player.sendMessage(ChatColor.GREEN + "New magical item registered with name: " + args[0]);
			}
		}
		return true;
	}

}
