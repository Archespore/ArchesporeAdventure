package jp.archesporeadventure.main.abilities.fishing;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerFishEvent;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.abilities.SkillAbility;
import jp.archesporeadventure.main.controllers.MagicalItemsController;
import jp.archesporeadventure.main.skills.SkillType;

public class MagicFisherAbility extends SkillAbility {

	public MagicFisherAbility(List<String> displayStrings, Material material, int minLevel, double minChance,
			int maxLevel, double maxChance, int minAbilityLevel, int maxAbilityLevel) {
		super(displayStrings, material, minLevel, minChance, maxLevel, maxChance, minAbilityLevel, maxAbilityLevel, AbilityActivation.PLAYER_FISH, SkillType.FISHING);
	}

	public boolean abilityEffect(Event event) {
		if (event instanceof PlayerFishEvent) {
			
			PlayerFishEvent fishingEvent = (PlayerFishEvent) event;
			Player player = fishingEvent.getPlayer();
			MagicalItemsController magicalItemController = ArchesporeAdventureMain.getMagicItemController();
			
			if (ThreadLocalRandom.current().nextDouble(100) < getChanceForPlayer(player)) {
				player.getWorld().dropItemNaturally(player.getLocation(), magicalItemController.generateScroll(ThreadLocalRandom.current().nextInt(magicalItemController.getRegisteredScrollsAmount()) + 1));
				return true;
			}
		}
		return false;
	}

}
