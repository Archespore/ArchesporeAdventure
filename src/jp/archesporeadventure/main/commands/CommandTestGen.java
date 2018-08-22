package jp.archesporeadventure.main.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import net.md_5.bungee.api.ChatColor;

public class CommandTestGen implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player) {
			
			Player player = (Player) sender;
			World generationWorld = player.getWorld();
			ArchesporeAdventureMain.getChestGenerator(generationWorld).generateChests(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_WOOD"), Bukkit.getWorld("ServerWorld"), 2128);
			ArchesporeAdventureMain.getChestGenerator(generationWorld).generateChests(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_COAL"), Bukkit.getWorld("ServerWorld"), 2128);
			ArchesporeAdventureMain.getChestGenerator(generationWorld).generateChests(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_IRON"), Bukkit.getWorld("ServerWorld"), 1197);
			ArchesporeAdventureMain.getChestGenerator(generationWorld).generateChests(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_LAPIS"), Bukkit.getWorld("ServerWorld"), 766);
			ArchesporeAdventureMain.getChestGenerator(generationWorld).generateChests(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_REDSTONE"), Bukkit.getWorld("ServerWorld"), 532);
			ArchesporeAdventureMain.getChestGenerator(generationWorld).generateChests(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_GOLD"), Bukkit.getWorld("ServerWorld"), 397);
			ArchesporeAdventureMain.getChestGenerator(generationWorld).generateChests(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_DIAMOND"), Bukkit.getWorld("ServerWorld"), 299);
			ArchesporeAdventureMain.getChestGenerator(generationWorld).generateChests(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_EMERALD"), Bukkit.getWorld("ServerWorld"), 75);
			Bukkit.broadcastMessage(ChatColor.GREEN + "Chests generated!");
		}
		return true;
	}

}
