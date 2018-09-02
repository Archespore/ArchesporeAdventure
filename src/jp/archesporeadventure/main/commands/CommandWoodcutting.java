package jp.archesporeadventure.main.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import net.md_5.bungee.api.ChatColor;

public class CommandWoodcutting implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player) {
			
			ArchesporeAdventureMain.getBossGenerator().spawnBoss(((Player) sender).getLocation(), EntityType.ZOMBIE);
			sender.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Boss Spawned!");
		}
		return true;
	}

}
