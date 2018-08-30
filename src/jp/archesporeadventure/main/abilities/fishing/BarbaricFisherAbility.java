package jp.archesporeadventure.main.abilities.fishing;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.abilities.SkillAbility;
import jp.archesporeadventure.main.skills.PlayerSkillController;
import jp.archesporeadventure.main.skills.SkillType;
import jp.archesporeadventure.main.skills.fishing.FishingSkillController;

public class BarbaricFisherAbility extends SkillAbility {


	public BarbaricFisherAbility(List<String> displayStrings, Material material, int minLevel, double minChance,
			int maxLevel, double maxChance, int minAbilityLevel, int maxAbilityLevel) {
		super(displayStrings, material, minLevel, minChance, maxLevel, maxChance, minAbilityLevel, maxAbilityLevel, AbilityActivation.ENTITY_KILL, SkillType.FISHING);
	}

	public boolean abilityEffect(Event event) {
		if (event instanceof EntityDeathEvent) {
			
			EntityDeathEvent deathEvent = (EntityDeathEvent) event;
			EntityType killedEntityType = deathEvent.getEntityType();
			Player player = deathEvent.getEntity().getKiller();
			PlayerSkillController playerSkillController = ArchesporeAdventureMain.getPlayerSkillsController();
			FishingSkillController fishingController = (FishingSkillController) ArchesporeAdventureMain.getSkillController(SkillType.FISHING);
			if (playerSkillController.getPlayerSkillStats(player, SkillType.FISHING).get(0) >= getMinimumLevel() && (killedEntityType.equals(EntityType.SQUID) || killedEntityType.equals(EntityType.COD) || killedEntityType.equals(EntityType.SALMON) || killedEntityType.equals(EntityType.PUFFERFISH) || killedEntityType.equals(EntityType.TROPICAL_FISH))) {
				playerSkillController.addPlayerEXP(player, SkillType.FISHING, fishingController.getCatchXPReward() * fishingController.getSkillAbility("Experience Fisher").getAbilityLevelForPlayer(player), true);
				return true;
			}
		}
		return false;
	}

}
