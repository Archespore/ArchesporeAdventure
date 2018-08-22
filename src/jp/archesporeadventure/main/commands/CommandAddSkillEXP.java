package jp.archesporeadventure.main.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.skills.PlayerSkillController;
import jp.archesporeadventure.main.skills.SkillType;
import net.md_5.bungee.api.ChatColor;

public class CommandAddSkillEXP implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length >= 3) {
			
			PlayerSkillController playerSkillController = ArchesporeAdventureMain.getPlayerSkillsController();
			Player giftedPlayer = Bukkit.getPlayer(args[0]);
			if (giftedPlayer != null && playerSkillController.doesPlayerExist(giftedPlayer) && playerSkillController.getSkillNameSet().contains(args[1].toUpperCase())) {
				
				SkillType skill = SkillType.valueOf(args[1].toUpperCase());
				double amount = Double.valueOf(args[2]);
				playerSkillController.addPlayerEXP(giftedPlayer, skill, amount, false);
				sender.sendMessage(ChatColor.GREEN + "Gave " + amount + " exp to the player, " + giftedPlayer.getName());
				return true;
			}
		}
		sender.sendMessage(ChatColor.RED + "Sorry, something failed...");
		return false;
	}

}
