package jp.archesporeadventure.main.abilities.fishing;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerFishEvent;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.abilities.SkillAbility;
import jp.archesporeadventure.main.skills.PlayerSkillController;
import jp.archesporeadventure.main.skills.SkillType;
import jp.archesporeadventure.main.skills.fishing.FishingSkillController;

public class ExperienceFisherAbility extends SkillAbility {

	public ExperienceFisherAbility(List<String> displayStrings, Material material, int minLevel, double minChance,
			int maxLevel, double maxChance, int minAbilityLevel, int maxAbilityLevel) {
		super(displayStrings, material, minLevel, minChance, maxLevel, maxChance, minAbilityLevel, maxAbilityLevel, AbilityActivation.PLAYER_FISH, SkillType.FISHING);
	}

	public boolean abilityEffect(Event event) {
		if (event instanceof PlayerFishEvent) {
			
			PlayerFishEvent fishingEvent = (PlayerFishEvent) event;
			Player player = fishingEvent.getPlayer();
			PlayerSkillController playerSkillController = ArchesporeAdventureMain.getPlayerSkillsController();
			if (playerSkillController.getPlayerSkillStats(player, SkillType.FISHING).get(0) >= getMinimumLevel()) {
				playerSkillController.addPlayerEXP(player, SkillType.FISHING, (getAbilityLevelForPlayer(player) - 1) * ((FishingSkillController)ArchesporeAdventureMain.getSkillController(SkillType.FISHING)).getCatchXPReward(), true);
			}
		}
		return false;
	}

}
