package jp.archesporeadventure.main.commands.magicitems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.controllers.MagicalItemsController;

public class CommandGiveMagicItem implements CommandExecutor, TabCompleter{

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player receivingPlayer = Bukkit.getPlayer(args[0]);
		MagicalItemsController magicItemController = ArchesporeAdventureMain.getMagicItemController();
		
		if (receivingPlayer != null){
			if (magicItemController.doesItemExist(args[1])){
				if (args.length > 2){
					if (args[2].toLowerCase().equals("true")){
						receivingPlayer.getInventory().addItem(magicItemController.generateItem(args[1], true));
						return true;
					}
				}
				receivingPlayer.getInventory().addItem(magicItemController.generateItem(args[1], false));
			}
			else {
				sender.sendMessage("The item: " + args[1] + " Doesn't exist!");
			}
		}
		else {
			sender.sendMessage("The player: " + args[0] + " is not online!");
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		
		MagicalItemsController magicItemController = ArchesporeAdventureMain.getMagicItemController();
		
		if (args.length == 2){
			
			Set<String> keySet = magicItemController.magicalItemKeys();
			List<String> allMagicItems = new ArrayList<>();
			
			if (args[1].equals("")){
				for(String itemName : keySet){
					allMagicItems.add(itemName);
				}
			}
			else {
				for(String itemName : keySet){
					if (itemName.toLowerCase().startsWith(args[1].toLowerCase())){
						allMagicItems.add(itemName);
					}
				}
			}
			Collections.sort(allMagicItems);
			return allMagicItems;
		}
		else {
			return null;
		}
	}
	
}
