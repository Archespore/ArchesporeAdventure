package jp.archesporeadventure.main.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.skills.SkillType;
import net.md_5.bungee.api.ChatColor;

public abstract class SkillAbility {
	
	public enum AbilityActivation {
		BLOCK_BREAK, ENTITY_KILL, ITEM_CRAFT, PLAYER_FISH
	}

	private final String displayName;
	private final List<String> displayDescription;
	private final Material abilityIcon;
	private final int minimumLevel;
	private final int maximumLevel;
	private final double minimumChance;
	private final double chanceIncrease;
	private final double minimumAbilityLevel;
	private final double abilityLevelIncrease;
	private final AbilityActivation abilityActivation;
	private final SkillType skillAbilityType;
	
	public SkillAbility(List<String> displayStrings, Material material, int minLevel, double minChance, int maxLevel, double maxChance, int minAbilityLevel, int maxAbilityLevel,
			AbilityActivation activationType, SkillType abilityType) {
		
		displayName = ChatColor.translateAlternateColorCodes('&', displayStrings.remove(0));
		displayDescription = displayStrings;
		abilityIcon = material;
		minimumLevel = minLevel;
		maximumLevel = maxLevel;
		
		int levelDifference = maxLevel - minLevel;
		minimumChance = minChance;
		minimumAbilityLevel = minAbilityLevel;
		chanceIncrease = (levelDifference == 0) ? 0 : (maxChance - minChance) / levelDifference;
		abilityLevelIncrease = (levelDifference == 0) ? 0 : (maxAbilityLevel - minAbilityLevel) / (double)levelDifference;
		
		abilityActivation = activationType;
		skillAbilityType = abilityType;
	}
	
	/**
	 * Gets the display name of this ability.
	 * @return display name.
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Gets the material for this ability's icon.
	 * @return the material to display.
	 */
	public Material getAbilityIcon() {
		return abilityIcon;
	}
	
	/**
	 * Gets the description of this ability
	 * @return
	 */
	public List<String> getDisplayDescription() {
		return new ArrayList<>(displayDescription);
	}
	
	/**
	 * Gets the minimum activation level of this ability.
	 * @return minimum activation level.
	 */
	public int getMinimumLevel() {
		return minimumLevel;
	}
	
	/**
	 * Gets the maximum activation level of this ability.
	 * @return maximum activation level.
	 */
	public int getMaximumLevel() {
		return maximumLevel;
	}
	
	/**
	 * Gets the base chance of activating this ability.
	 * @return base activation chance.
	 */
	public double getMinimumChance() {
		return minimumChance;
	}
	
	/**
	 * Get the chance increase of activation per level
	 * @return the increase in activation per level.
	 */
	public double getChanceIncrease() {
		return chanceIncrease;
	}
	
	/**
	 * Gets the base level of this ability
	 * @return base ability level chance.
	 */
	public double getMinimumAbilityLevel() {
		return minimumAbilityLevel;
	}
	
	/**
	 * Get the increase of ability's effect per level
	 * @return the increase in ability's effect per level.
	 */
	public double getAbilityLevelIncrease() {
		return abilityLevelIncrease;
	}
	
	/**
	 * Gets the activation type that activates this ability.
	 * @return AbilityActivation of this ability.
	 */
	public AbilityActivation getActivationType() {
		return abilityActivation;
	}
	
	/**
	 * Gets the skill this ability is related to.
	 * @return the SkillType of this ability.
	 */
	public SkillType getSkillType() {
		return skillAbilityType;
	}
	
	/**
	 * Gets the chance to activate this ability for the specified player.
	 * @param skillLevel the mining level to use.
	 * @return the chance, or -1 if the level is not high enough.
	 */
	public double getChanceAtLevel(double skillLevel) {
		if (skillLevel >= minimumLevel) { return Math.min(100, minimumChance + (chanceIncrease * (skillLevel - minimumLevel))); }
		else { return -1; }
	}
	
	/**
	 * Gets the chance to activate this ability for the specified player. (Factors in luck effects.)
	 * @param player the player to get the chance for.
	 * @return the chance, or -1 if the level is not high enough.
	 */
	public double getChanceForPlayer(Player player) {
		double skillLevel = Math.min(maximumLevel, ArchesporeAdventureMain.getPlayerSkillsController().getPlayerSkillStats(player, skillAbilityType).get(0));
		int luckEffect = 0;
		
		if (player.hasPotionEffect(PotionEffectType.LUCK)) { luckEffect = player.getPotionEffect(PotionEffectType.LUCK).getAmplifier() + 1; }
		if (skillLevel >= minimumLevel) { 
			return Math.min(100, (minimumChance + (chanceIncrease * (skillLevel - minimumLevel))) + (luckEffect * 1));
		}
		else { return -1; }
	}
	
	/**
	 * Gets the ability level for this ability for the specified player.
	 * @param skillLevel the mining level to use.
	 * @return the level, or -1 if the level is not high enough.
	 */
	public int getAbilityLevelAtLevel(double skillLevel) {
		if (skillLevel >= minimumLevel) { return (int) Math.floor(minimumAbilityLevel + (abilityLevelIncrease * (skillLevel - minimumLevel))); }
		else { return -1; }
	}
	
	/**
	 * Gets the ability level for this ability for the specified player.
	 * @param player the player to get the chance for.
	 * @return the chance, or -1 if the level is not high enough.
	 */
	public int getAbilityLevelForPlayer(Player player) {
		double skillLevel = Math.min(maximumLevel, ArchesporeAdventureMain.getPlayerSkillsController().getPlayerSkillStats(player, skillAbilityType).get(0));
		
		if (skillLevel >= minimumLevel) { 
			return (int) Math.floor(minimumAbilityLevel + (abilityLevelIncrease * (skillLevel - minimumLevel)));
		}
		else { return -1; }
	}
	
	/**
	 * runs the effect of this ability.
	 */
	public abstract boolean abilityEffect(Event event);
}
