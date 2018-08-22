package jp.archesporeadventure.main.abilities.mining;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.abilities.SkillAbility;
import jp.archesporeadventure.main.skills.SkillType;
import jp.archesporeadventure.main.utils.PotionEffectUtil;

public class ChainMinerAbility extends SkillAbility {


	public ChainMinerAbility(List<String> displayStrings, Material material, int minLevel, double minChance,
			int maxLevel, double maxChance, int minAbilityLevel, int maxAbilityLevel) {
		super(displayStrings, material, minLevel, minChance, maxLevel, maxChance, minAbilityLevel, maxAbilityLevel, AbilityActivation.BLOCK_BREAK, SkillType.MINING);
	}

	public boolean abilityEffect(Event event) {
		if (event instanceof BlockBreakEvent) {
			
			BlockBreakEvent blockEvent = (BlockBreakEvent) event;
			Player player = blockEvent.getPlayer();
			if (ThreadLocalRandom.current().nextDouble(100) < getChanceForPlayer(player)) {
				int abilityLevel = getAbilityLevelForPlayer(player);
				PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, abilityLevel * 20, (abilityLevel / 3)), player);
				player.addPotionEffect(newPotionEffect, true);
				return true;
			}
		}
		return false;
	}
}
