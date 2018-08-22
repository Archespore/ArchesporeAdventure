package jp.archesporeadventure.main.abilities.fishing;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerFishEvent;

import jp.archesporeadventure.main.abilities.SkillAbility;
import jp.archesporeadventure.main.skills.SkillType;

public class AbundanceFisherAbility extends SkillAbility {

	public AbundanceFisherAbility(List<String> displayStrings, Material material, int minLevel, double minChance,
			int maxLevel, double maxChance, int minAbilityLevel, int maxAbilityLevel) {
		super(displayStrings, material, minLevel, minChance, maxLevel, maxChance, minAbilityLevel, maxAbilityLevel, AbilityActivation.PLAYER_FISH, SkillType.FISHING);
	}

	public boolean abilityEffect(Event event) {
		if (event instanceof PlayerFishEvent) {
			
			PlayerFishEvent fishingEvent = (PlayerFishEvent) event;
			Entity fishedEntity = fishingEvent.getCaught();
			Player player = fishingEvent.getPlayer();
			if (ThreadLocalRandom.current().nextDouble(100) < getChanceForPlayer(player) && fishedEntity instanceof Item) {
				
				Item fishedItem = (Item) fishedEntity;
				Material fishedItemType = fishedItem.getItemStack().getType();
				if (fishedItemType.equals(Material.COD) || fishedItemType.equals(Material.SALMON) || fishedItemType.equals(Material.PUFFERFISH) || fishedItemType.equals(Material.TROPICAL_FISH)) { 
					fishedItem.getItemStack().setAmount(getAbilityLevelForPlayer(player));
					return true;
				}
			}
		}
		return false;
	}

}
