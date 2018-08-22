package jp.archesporeadventure.main.skills.mining;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.skills.SkillType;

public class MiningSkillOre {
	
	private final Material oreType;
	private final String displayName;
	private final int minimumLevel;
	private final int maximumLevel;
	private final double minimumChance;
	private final double chanceIncrease;
	private final double oreXPReward;
	private final int toolDamage;
	private final int defaultRefreshTime;
	
	/**
	 * Constructor.
	 * @param material Material of the ore.
	 * @param minLevel minimum harvest level of the ore.
	 * @param minChance minimum harvest chance of the ore.
	 * @param maxLevel mining level when harvest chance reaches maximum.
	 * @param maxChance maximum harvest chance of this ore.
	 * @param xpReward the amount of xp this ore rewards.
	 * @param refreshTime the seconds it takes to refresh this ore.
	 */
	public MiningSkillOre(Material material, String menuDisplay, int minLevel, double minChance, int maxLevel, double maxChance, double xpReward, int durability, int refreshTime) {
		oreType = material;
		displayName = menuDisplay;
		minimumLevel = minLevel;
		maximumLevel = maxLevel;
		minimumChance = minChance;
		chanceIncrease = (maxChance - minChance) / (maxLevel - minLevel);
		oreXPReward = xpReward;
		toolDamage = durability;
		defaultRefreshTime = refreshTime;
	}
	
	/**
	 * Gets the material of this ore.
	 * @return material of this ore.
	 */
	public Material getOreMaterial() {
		return oreType;
	}
	
	/**
	 * Gets the display name of this ore.
	 * @return display name.
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Gets the minimum harvest level of this ore.
	 * @return minimum harvest level.
	 */
	public int getMinimumLevel() {
		return minimumLevel;
	}
	
	/**
	 * Gets the maximum harvest level of this ore.
	 * @return maximum harvest level.
	 */
	public int getMaximumLevel() {
		return minimumLevel;
	}
	
	/**
	 * Gets the base chance of harvesting this ore.
	 * @return base harvest chance.
	 */
	public double getMinimumChance() {
		return minimumChance;
	}
	
	/**
	 * Get the chance increase of harvest per level
	 * @return the increase in chance per level.
	 */
	public double getChanceIncrease() {
		return chanceIncrease;
	}
	
	/**
	 * Gets the skill xp awarded when mining this ore.
	 * @return the amount of xp rewarded.
	 */
	public double getXPReward() {
		return oreXPReward;
	}
	
	/**
	 * Gets the amount of durability it takes to mine this ore.
	 * @return damage this ore does to a tool.
	 */
	public int getToolDamage() {
		return toolDamage;
	}
	
	/**
	 * Gets the refresh time of this ore.
	 * @return the seconds it takes to renew this ore.
	 */
	public int getDefaultRefresh() {
		return defaultRefreshTime;
	}
	
	/**
	 * Gets the chance to mine this ore at the specified level.
	 * @param miningLevel the mining level to use.
	 * @return the chance, or -1 if the level is not high enough.
	 */
	public double getChanceAtLevel(double miningLevel) {
		if (miningLevel >= minimumLevel) { return Math.min(100, minimumChance + (chanceIncrease * (miningLevel - minimumLevel))); }
		else { return -1; }
	}
	
	/**
	 * Gets the chance to mine this ore for the specified player. (Factors in silk touch and luck effects.)
	 * @param player the player to get the chance for.
	 * @return the chance, or -1 if the level is not high enough.
	 */
	public double getChanceForPlayer(Player player) {
		ItemStack playerTool = player.getInventory().getItemInMainHand();
		double miningLevel = Math.min(maximumLevel, ArchesporeAdventureMain.getPlayerSkillsController().getPlayerSkillStats(player, SkillType.MINING).get(0));
		int silkTouchEffect = 0, luckEffect = 0;
		
		if (player.hasPotionEffect(PotionEffectType.LUCK)) { luckEffect = player.getPotionEffect(PotionEffectType.LUCK).getAmplifier() + 1; }
		if (playerTool.containsEnchantment(Enchantment.SILK_TOUCH)) { silkTouchEffect = playerTool.getEnchantmentLevel(Enchantment.SILK_TOUCH); }
		if (miningLevel >= minimumLevel) { 
			return Math.min(100, (minimumChance + (chanceIncrease * (miningLevel - minimumLevel))) + (silkTouchEffect + luckEffect) * 6.25);
		}
		else { return -1; }
	}
}
