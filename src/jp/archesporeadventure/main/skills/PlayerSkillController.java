package jp.archesporeadventure.main.skills;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.utils.MagicalItemsUtil;
import net.md_5.bungee.api.ChatColor;

public class PlayerSkillController {
	
	private ArchesporeAdventureMain plugin;

	private Map<Player, Map<SkillType, List<Double>>> playerSkillMap = new HashMap<>();
	private Set<String> skillNamesSet = new HashSet<>();
	
	private File playerSkillsFile;
	private FileConfiguration playerSkillsYML;
	
	private final double MAGICAL_BOOKSHELF_XP_BONUS = .025;
	
	/**
	 * Default Constructor.
	 */
	public PlayerSkillController(ArchesporeAdventureMain plugin) {
		this.plugin = plugin;
		loadPlayerFiles();
		for (SkillType skill : SkillType.values()) {
			skillNamesSet.add(skill.toString().toUpperCase());
		}
	}
	
	/**
	 * Adds a player and it's skills stats to the controller.
	 * @param player The player to register
	 */
	public void addPlayerToController(Player player) {
		
		Map<SkillType, List<Double>> skillMap = new HashMap<>();
		for (SkillType skill : SkillType.values()) {
			List<Double> skillStats = new ArrayList<>();
			skillStats.add(playerSkillsYML.getDouble("Players." + player.getUniqueId() + "." + skill.toString() + ".Level", 0.0));
			skillStats.add(playerSkillsYML.getDouble("Players." + player.getUniqueId() + "." + skill.toString() + ".EXP", 0.0));
			skillMap.put(skill, skillStats);
		}
		playerSkillMap.put(player, skillMap);
	}
	
	/**
	 * Adds the specified amount to the specified player's level.
	 * @param player player to add stats to
	 * @param skill skill to add stats to
	 * @param amount amount to add
	 */
	public void addPlayerLevel(Player player, SkillType skill, double amount) {
		List<Double> playerSkillStat = playerSkillMap.get(player).get(skill);
		playerSkillStat.set(0, playerSkillStat.get(0) + amount);
	}
	
	/**
	 * Adds the specified amount to the specified player's EXP.
	 * @param player player to add stats to
	 * @param skill skill to add stats to
	 * @param amount amount to add
	 * @param xpBoost should we check for magical items that increase xp amount.
	 */
	public void addPlayerEXP(Player player, SkillType skill, double amount, boolean xpBoost) {
		double xpBonusMultiplier = 1;
		if (xpBoost == true) { xpBonusMultiplier = 1 + (Math.min(3, MagicalItemsUtil.containsMagicItemAmount(Material.BOOKSHELF, player)) * MAGICAL_BOOKSHELF_XP_BONUS); }
		List<Double> playerSkillStat = playerSkillMap.get(player).get(skill);
		playerSkillStat.set(1, playerSkillStat.get(1) + (amount * xpBonusMultiplier));
		checkForLevelUp(player, skill);
	}
	
	/**
	 * Checks if the specified player is registered in the controller.
	 * @param player The player to check for.
	 * @return true/false
	 */
	public boolean doesPlayerExist(Player player) {
		return playerSkillMap.containsKey(player);
	}
	
	/**
	 * Gets the specified player's stats for the specified skill.
	 * @param player The player to get the stats of.
	 * @param skill The skill to get the stats for.
	 * @return List of the player's stats for the specified skill, or null if the player doesn't exist.
	 */
	public List<Double> getPlayerSkillStats(Player player, SkillType skill) {
		if (doesPlayerExist(player)) { return playerSkillMap.get(player).get(skill); }
		return null;
	}
	
	/**
	 * Gets a set of all registered skill names.
	 * @return a set of skill names.
	 */
	public Set<String> getSkillNameSet() {
		return skillNamesSet;
	}
	
	/**
	 * Gets the amount of xp required for the player to level in the specified skill.
	 * @param player player to get xp for
	 * @param skill the skill to get xp needed.
	 * @return the xp needed to level up.
	 */
	public double getXPToLevel(Player player, SkillType skill) {
		double playerLevel = getPlayerSkillStats(player, skill).get(0);
		return Math.round((((playerLevel * 1.75) + 6) * (Math.pow(2, (playerLevel / 8))) + 32) / 2);
	}
	
	/**
	 * Checks to see if the player leveled up a specified skill.
	 * @param player player to check.
	 * @param skill skill to check.
	 * @return true/false if the player leveled up.
	 */
	private boolean checkForLevelUp(Player player, SkillType skill) {
		List<Double> skillStats = getPlayerSkillStats(player, skill);
		double xpNeededToLevel = getXPToLevel(player, skill);
		if (skillStats.get(1) >= xpNeededToLevel) {
			player.sendTitle(ChatColor.GREEN + "Level Up!", ChatColor.GOLD + StringUtils.capitalize(skill.toString().toLowerCase()) + " Level: " + skillStats.get(0).intValue() + " → " + (skillStats.get(0).intValue() + 1), 10, 60, 10);
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, .75f);
			addPlayerLevel(player, skill, 1);
			addPlayerEXP(player, skill, -xpNeededToLevel, false);
			return true;
		}
		return false;
	}
	
	/**
	 * Saves all player's stats to the YML file.
	 */
	public void saveStats() {
		
		for (Player player : playerSkillMap.keySet()) {
			for (SkillType skill : SkillType.values()) {
				//We round the xp value to the nearest 5 decimal places when it is stored to help with floating point imprecision.
				DecimalFormat xpFormat = new DecimalFormat("#.#####");
				xpFormat.setRoundingMode(RoundingMode.CEILING);
				playerSkillsYML.set("Players." + player.getUniqueId() + "." + skill.toString() + ".Level", playerSkillMap.get(player).get(skill).get(0));
				playerSkillsYML.set("Players." + player.getUniqueId() + "." + skill.toString() + ".EXP", Double.valueOf(xpFormat.format(playerSkillMap.get(player).get(skill).get(1))));
			}
		}
		try { playerSkillsYML.save(playerSkillsFile); } 
		catch (IOException e) { e.printStackTrace(); }
	}
	
	/**
	 * Loads the YML file containing player stats. Doesn't actually load the stats unless the player joins.
	 */
	private void loadPlayerFiles() {
		
		playerSkillsFile = new File(plugin.getDataFolder(), "PlayerSkillsStats.yml");
		if (!playerSkillsFile.exists()) {
			playerSkillsFile.getParentFile().mkdirs();
			plugin.saveResource(playerSkillsFile.getName(), true);
		}
		
		playerSkillsYML = new YamlConfiguration();
		try { playerSkillsYML.load(playerSkillsFile); } 
		catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
	}
}
