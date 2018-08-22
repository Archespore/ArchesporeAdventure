package jp.archesporeadventure.main.abilities.fishing;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.abilities.SkillAbility;
import jp.archesporeadventure.main.skills.SkillType;
import jp.archesporeadventure.main.skills.fishing.FishingSkillController;
import net.md_5.bungee.api.ChatColor;

public class CulinaryFisherAbility extends SkillAbility {

	public CulinaryFisherAbility(List<String> displayStrings, Material material, int minLevel, double minChance,
			int maxLevel, double maxChance, int minAbilityLevel, int maxAbilityLevel) {
		super(displayStrings, material, minLevel, minChance, maxLevel, maxChance, minAbilityLevel, maxAbilityLevel, AbilityActivation.ITEM_CRAFT, SkillType.FISHING);
	}

	public boolean abilityEffect(Event event) {
		if (event instanceof CraftItemEvent) {
			
			CraftItemEvent craftEvent = (CraftItemEvent) event;
			Player player = (Player) craftEvent.getWhoClicked();
			ItemStack craftedItem = craftEvent.getCurrentItem();
			if (((FishingSkillController)ArchesporeAdventureMain.getSkillController(SkillType.FISHING)).getRegisteredRecipes().contains(craftedItem) && ArchesporeAdventureMain.getPlayerSkillsController().getPlayerSkillStats(player, SkillType.FISHING).get(0) >= getMinimumLevel()) {
				return true;
			}
			craftEvent.setCancelled(true);
			player.closeInventory();
			player.sendMessage(ChatColor.RED + "You must be level " + getMinimumLevel() + " in fishing to craft that item!");
		}
		return false;
	}

}
