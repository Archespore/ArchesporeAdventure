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
			ArchesporeAdventureMain.getChestGenerator(generationWorld).generateChests(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_WOOD"), Bukkit.getWorld("ServerWorld"), 2000);
			ArchesporeAdventureMain.getChestGenerator(generationWorld).generateChests(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_COAL"), Bukkit.getWorld("ServerWorld"), 2000);
			ArchesporeAdventureMain.getChestGenerator(generationWorld).generateChests(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_IRON"), Bukkit.getWorld("ServerWorld"), 1500);
			ArchesporeAdventureMain.getChestGenerator(generationWorld).generateChests(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_LAPIS"), Bukkit.getWorld("ServerWorld"), 1050);
			ArchesporeAdventureMain.getChestGenerator(generationWorld).generateChests(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_REDSTONE"), Bukkit.getWorld("ServerWorld"), 683);
			ArchesporeAdventureMain.getChestGenerator(generationWorld).generateChests(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_GOLD"), Bukkit.getWorld("ServerWorld"), 410);
			ArchesporeAdventureMain.getChestGenerator(generationWorld).generateChests(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_DIAMOND"), Bukkit.getWorld("ServerWorld"), 226);
			ArchesporeAdventureMain.getChestGenerator(generationWorld).generateChests(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_EMERALD"), Bukkit.getWorld("ServerWorld"), 113);
			Bukkit.broadcastMessage(ChatColor.GREEN + "Chests generated!");
		}
		return true;
	}

}
