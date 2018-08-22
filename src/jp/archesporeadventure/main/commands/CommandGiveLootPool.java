package jp.archesporeadventure.main.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import net.md_5.bungee.api.ChatColor;

public class CommandGiveLootPool implements CommandExecutor, TabCompleter{

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player){
			Player player = (Player) sender;
			if (args.length >= 1) {
				if (ArchesporeAdventureMain.getLootPoolController().doesLootPoolExist(args[0])) {
					player.sendMessage(ChatColor.GREEN + "Here is the set: " + args[0]);
					StorageMinecart newMinecart = player.getWorld().spawn(player.getLocation(), StorageMinecart.class);
					int itemNumber = 0;
					for (ItemStack item : ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool(args[0].toUpperCase()).getContents()) {
						if (itemNumber >= 27) {
							newMinecart = player.getWorld().spawn(player.getLocation().add(ThreadLocalRandom.current().nextDouble(-1, 1), 0, ThreadLocalRandom.current().nextDouble(-1, 1)), StorageMinecart.class);
							itemNumber = 0;
						}
						newMinecart.getInventory().setItem(itemNumber, item.clone());
						itemNumber++;
					}
				}
				else {
					player.sendMessage(ChatColor.RED + "The set: " + args[0] + " doesn't exist!");
				}
			}
		}
		return true;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			List<String> allMatchingLootPools = new ArrayList<>();
			Set<String> allLootPools = ArchesporeAdventureMain.getLootPoolController().getLootPoolMap().keySet();
			if (args[0].equals("")) {
				for (String poolName : allLootPools) {
					allMatchingLootPools.add(poolName);
				}
			}
			else {
				for (String poolName : allLootPools) {
					if (poolName.toLowerCase().startsWith(args[0].toLowerCase())) {
						allMatchingLootPools.add(poolName);
					}
				}
			}
			Collections.sort(allMatchingLootPools);
			return allMatchingLootPools;
		}
		return null;
	}
}
