package jp.archesporeadventure.main.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import net.md_5.bungee.api.ChatColor;

public class CommandTestRemove implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

		ArchesporeAdventureMain.getChestGenerator(Bukkit.getWorld("ServerWorld")).removeAllGeneratedChests();
		Bukkit.broadcastMessage(ChatColor.RED + "Chests removed!");
		return true;
	}

}
