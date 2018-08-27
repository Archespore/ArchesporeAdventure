package jp.archesporeadventure.main.menus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.abilities.SkillAbility;
import jp.archesporeadventure.main.skills.PlayerSkillController;
import jp.archesporeadventure.main.skills.SkillType;
import jp.archesporeadventure.main.skills.fishing.FishingSkillController;
import net.md_5.bungee.api.ChatColor;

public class FishingMenuInventory implements InventoryMenu {
	
	public void populateInventory(Player player, Inventory inventory) {
		int inventorySlot = 1;
		boolean luckBonus = player.hasPotionEffect(PotionEffectType.LUCK);
		FishingSkillController fishingController = (FishingSkillController) ArchesporeAdventureMain.getSkillController(SkillType.FISHING);
		PlayerSkillController skillController = ArchesporeAdventureMain.getPlayerSkillsController();
		
		List<SkillAbility> registeredAbilities = fishingController.getRegisteredAbilities().stream().sorted(Comparator.comparingInt(entry -> entry.getMinimumLevel())).collect(Collectors.toList());
		for (SkillAbility ability : registeredAbilities) {
			double abilityChance = ability.getChanceForPlayer(player);
			List<String> menuItemLore;
			
			if (abilityChance >= 0) { 
				menuItemLore = new ArrayList<>();
				for (String loreLine : ability.getDisplayDescription()) {
					menuItemLore.add(ChatColor.GRAY + String.format(loreLine, ability.getChanceForPlayer(player), ability.getAbilityLevelForPlayer(player)));
				}
			}
			else { menuItemLore = Arrays.asList(ChatColor.RED + "You are not a high enough", ChatColor.RED + "level for this ability.", ChatColor.RED.toString() + ChatColor.BOLD + "Level Required: " + ability.getMinimumLevel()); }
			
			inventory.setItem(inventorySlot, createMenuItem(ability.getAbilityIcon(), ability.getDisplayName(), luckBonus, menuItemLore));
			inventorySlot++;
		}

		inventory.setItem(21, createMenuItem(Material.FISHING_ROD, ChatColor.DARK_AQUA + "Fishing Level", 
				Arrays.asList(ChatColor.GRAY + "Fishing Level: " + skillController.getPlayerSkillStats(player, SkillType.FISHING).get(0).intValue())));
		inventory.setItem(22, createMenuItem(Material.BOOK, ChatColor.DARK_GREEN + "Fishing XP", 
				Arrays.asList(ChatColor.GRAY + "Fishing EXP: " + new DecimalFormat("0.0#").format(skillController.getPlayerSkillStats(player, SkillType.FISHING).get(1)) + " / " + skillController.getXPToLevel(player, SkillType.FISHING))));
		inventory.setItem(23, createMenuItem(Material.BARRIER, ChatColor.RED + "Close Menu", 
				Arrays.asList(ChatColor.GRAY + "Close the menu.")));
	}

	public void clickActions(Inventory inventory, Player player, ItemStack itemStack) {
		Material itemMaterial = itemStack.getType();
		if (itemMaterial.equals(Material.BARRIER)) {
			player.closeInventory();
		}
	}

}
