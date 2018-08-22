package jp.archesporeadventure.main.abilities.mining;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.abilities.SkillAbility;
import jp.archesporeadventure.main.controllers.LootPoolController;
import jp.archesporeadventure.main.skills.SkillType;

public class LootMinerAbility extends SkillAbility {


	public LootMinerAbility(List<String> displayStrings, Material material, int minLevel, double minChance,
			int maxLevel, double maxChance, int minAbilityLevel, int maxAbilityLevel) {
		super(displayStrings, material, minLevel, minChance, maxLevel, maxChance, minAbilityLevel, maxAbilityLevel, AbilityActivation.BLOCK_BREAK, SkillType.MINING);
	}

	public boolean abilityEffect(Event event) {
		if (event instanceof BlockBreakEvent) {
			
			BlockBreakEvent blockEvent = (BlockBreakEvent) event;
			Player player = blockEvent.getPlayer();
			if (ThreadLocalRandom.current().nextDouble(100) < getChanceForPlayer(player)) {
				LootPoolController lootPools = ArchesporeAdventureMain.getLootPoolController();
				String[] allItemPools = lootPools.getLootPoolMap().keySet().toArray(new String[0]);
				ItemStack[] lootedItem = lootPools.getRegisteredLootPool(allItemPools[ThreadLocalRandom.current().nextInt(allItemPools.length)]).generateItems(1);
				Inventory playerInventory = player.getInventory();
				if (playerInventory.firstEmpty() > 0) { 
					playerInventory.addItem(lootedItem[0]);
					player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, .25f, 2.0f);
				}
				else { player.getWorld().dropItemNaturally(player.getLocation(), lootedItem[0]); }
				return true;
			}
		}
		return false;
	}

}
