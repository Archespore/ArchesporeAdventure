package jp.archesporeadventure.main.menus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.abilities.SkillAbility;
import jp.archesporeadventure.main.skills.PlayerSkillController;
import jp.archesporeadventure.main.skills.SkillType;
import jp.archesporeadventure.main.skills.mining.MiningSkillController;
import jp.archesporeadventure.main.skills.mining.MiningSkillOre;
import net.md_5.bungee.api.ChatColor;

public class MiningMenuInventory extends InventoryMenu {
	
	public void populateInventory(Player player, Inventory inventory) {
		int inventorySlot = 0;
		boolean silkTouchBonus = player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH);
		boolean luckBonus = player.hasPotionEffect(PotionEffectType.LUCK);
		MiningSkillController miningController = (MiningSkillController) ArchesporeAdventureMain.getSkillController(SkillType.MINING);
		PlayerSkillController skillController = ArchesporeAdventureMain.getPlayerSkillsController();
		List<Material> registeredOres = miningController.getRegisteredOres();
		
		for (Material oreMaterial : registeredOres) {
			MiningSkillOre miningOre = miningController.getMiningOre(oreMaterial);
			double harvestChance = miningOre.getChanceForPlayer(player);
			List<String> menuItemLore;
			
			if (harvestChance >= 0) { menuItemLore = Arrays.asList(ChatColor.GRAY + "Chance to harvest: " + String.format("%.2f", miningOre.getChanceForPlayer(player)) + "%"); }
			else { menuItemLore = Arrays.asList(ChatColor.RED + "You are not a high enough", ChatColor.RED + "level for this ore.", ChatColor.RED.toString() + ChatColor.BOLD + "Level Required: " + miningOre.getMinimumLevel()); }
			
			inventory.setItem(inventorySlot, createMenuItem(miningOre.getOreMaterial(), miningOre.getDisplayName(), silkTouchBonus || luckBonus, menuItemLore));
			inventorySlot++;
		}
		
		inventorySlot = 12;
		List<SkillAbility> registeredAbilities = miningController.getRegisteredAbilities().stream().sorted(Comparator.comparingInt(entry -> entry.getMinimumLevel())).collect(Collectors.toList());
		for (SkillAbility ability : registeredAbilities) {
			double abilityChance = ability.getChanceForPlayer(player);
			List<String> menuItemLore;
			
			if (abilityChance >= 0) { 
				menuItemLore = new ArrayList<>();
				for (String loreLine : ability.getDisplayDescription()) {
					menuItemLore.add(ChatColor.GRAY + String.format(loreLine, ability.getChanceForPlayer(player), ability.getAbilityLevelForPlayer(player), (int) Math.floor(ability.getAbilityLevelForPlayer(player) / 3) + 1));
				}
			}
			else { menuItemLore = Arrays.asList(ChatColor.RED + "You are not a high enough", ChatColor.RED + "level for this ability.", ChatColor.RED.toString() + ChatColor.BOLD + "Level Required: " + ability.getMinimumLevel()); }
			
			inventory.setItem(inventorySlot, createMenuItem(ability.getAbilityIcon(), ability.getDisplayName(), luckBonus, menuItemLore));
			inventorySlot++;
		}
		
		inventory.setItem(30, createMenuItem(Material.DIAMOND_PICKAXE, ChatColor.DARK_AQUA + "Mining Level", 
				Arrays.asList(ChatColor.GRAY + "Mining Level: " + skillController.getPlayerSkillStats(player, SkillType.MINING).get(0).intValue())));
		inventory.setItem(31, createMenuItem(Material.BOOK, ChatColor.DARK_GREEN + "Mining XP", 
				Arrays.asList(ChatColor.GRAY + "Mining EXP: " + new DecimalFormat("0.0#").format(skillController.getPlayerSkillStats(player, SkillType.MINING).get(1)) + " / " + skillController.getXPToLevel(player, SkillType.MINING))));
		inventory.setItem(32, createMenuItem(Material.BARRIER, ChatColor.RED + "Close Menu", 
				Arrays.asList(ChatColor.GRAY + "Close the menu.")));
	}

	public void clickActions(Player player, Material material) {
		if (material.equals(Material.BARRIER)) {
			player.closeInventory();
		}
	}

}
