package jp.archesporeadventure.main.abilities.mining;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

import jp.archesporeadventure.main.abilities.SkillAbility;
import jp.archesporeadventure.main.skills.SkillType;

public class XPMinerAbility extends SkillAbility {


	public XPMinerAbility(List<String> displayStrings, Material material, int minLevel, double minChance, int maxLevel,
			double maxChance, int minAbilityLevel, int maxAbilityLevel) {
		super(displayStrings, material, minLevel, minChance, maxLevel, maxChance, minAbilityLevel, maxAbilityLevel, AbilityActivation.BLOCK_BREAK, SkillType.MINING);
	}

	public boolean abilityEffect(Event event) {
		if (event instanceof BlockBreakEvent) {
			
			BlockBreakEvent blockEvent = (BlockBreakEvent) event;
			Player player = blockEvent.getPlayer();
			if (ThreadLocalRandom.current().nextDouble(100) < getChanceForPlayer(player)) {
				ExperienceOrb xpMinerOrb = player.getWorld().spawn(player.getLocation(), ExperienceOrb.class);
				xpMinerOrb.setExperience(ThreadLocalRandom.current().nextInt((int)getAbilityLevelForPlayer(player) + 1));
				return true;
			}
		}
		return false;
	}

}
