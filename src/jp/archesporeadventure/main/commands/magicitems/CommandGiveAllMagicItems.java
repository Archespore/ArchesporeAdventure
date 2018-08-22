package jp.archesporeadventure.main.commands.magicitems;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.controllers.MagicalItemsController;
import net.md_5.bungee.api.ChatColor;

public class CommandGiveAllMagicItems implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		MagicalItemsController magicItemController = ArchesporeAdventureMain.getMagicItemController();
		
		if (args.length >= 1) {
			Player receivingPlayer = Bukkit.getPlayer(args[0]);
			if (receivingPlayer != null){
				for (String magicitem : magicItemController.magicalItemKeys()) {
					receivingPlayer.getWorld().dropItemNaturally(receivingPlayer.getLocation(), magicItemController.generateItem(magicitem, true));	
				}
				receivingPlayer.sendMessage(ChatColor.GREEN + "Enjoy!");
			}
		}
		return true;
	}

}
