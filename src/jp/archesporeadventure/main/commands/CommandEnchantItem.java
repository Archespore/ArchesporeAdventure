package jp.archesporeadventure.main.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.enchantments.CustomEnchantment;
import jp.archesporeadventure.main.utils.EnchantmentUtil;
import net.md_5.bungee.api.ChatColor;

public class CommandEnchantItem implements CommandExecutor, TabCompleter{

	//This command is used to add custom enchantments to items since we can't with the vanilla command.
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		//First we need to make sure a player is using this command
		if (sender instanceof Player){
			
			//Make sure we have the correct amount of arguments
			if (args.length >= 2) {
				
				boolean validEnchant = false;
				
				//Get the player who sent the command and the item that the player is going to enchant
				Player player = (Player) sender;
				ItemStack itemToEnchant = player.getInventory().getItem(player.getInventory().getHeldItemSlot());
				
				//If the second agrument is a digit, we are good to go.
				if (args[1].matches("\\d+")){
					//Make sure the argument is a valid enchantment type
					for(CustomEnchantment enchantment : CustomEnchantment.values()){
						if (enchantment.toString().equals(args[0].toUpperCase())) {
							//If the enchantment is valid, add it to the item and set the validEnchant to true
							EnchantmentUtil.addSpecialEnchantment(itemToEnchant, CustomEnchantment.valueOf(args[0].toUpperCase()).getEnchant(), Integer.parseInt(args[1]), true);
							sender.sendMessage(ChatColor.GREEN + "Added enchantment " + args[0].toUpperCase() + " to the item.");
							validEnchant = true;
						}
					}
					
					//If we could not find the enchantment
					if (validEnchant == false){
						sender.sendMessage(ChatColor.RED + "Enchantment " + args[0].toUpperCase() + " could not be found, try again.");
					}
				}
				
				//If the second argument is not a digit, inform the sender.
				else {
					sender.sendMessage(ChatColor.RED + args[1] + " is not an integer, try again.");
				}

			}
			
			//If we have an invalid number of arguments
			else {
				sender.sendMessage(ChatColor.RED + "Expected 2 arguments, recieved " + args.length);
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1){
			List<CustomEnchantment> enchantmentList = Arrays.asList(CustomEnchantment.values());
			List<String> allEnchants = new ArrayList<>();
			if (args[0].equals("")){
				for(CustomEnchantment enchantment : enchantmentList){
					allEnchants.add(enchantment.toString());
				}
			}
			else {
				for(CustomEnchantment enchantment : enchantmentList){
					if (enchantment.toString().toLowerCase().startsWith(args[0].toLowerCase())){
						allEnchants.add(enchantment.toString());
					}
				}
			}
			Collections.sort(allEnchants);
			return allEnchants;
		}
		else {
			return null;
		}
	}
}
