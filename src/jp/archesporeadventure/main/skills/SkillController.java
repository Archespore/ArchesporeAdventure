package jp.archesporeadventure.main.skills;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.abilities.SkillAbility;
import jp.archesporeadventure.main.abilities.SkillAbility.AbilityActivation;
import net.md_5.bungee.api.ChatColor;

public abstract class SkillController {

	protected ArchesporeAdventureMain plugin;
	
	protected SkillType skillControllerType;
	
	protected File skillFile;
	protected FileConfiguration skillFileYML;
	
	protected Map<AbilityActivation, List<SkillAbility>> registeredAbilityMap = new HashMap<>();
	
	public SkillController(SkillType skillType, ArchesporeAdventureMain plugin) {
		this.plugin = plugin;
		skillControllerType = skillType;
		loadFiles(skillControllerType);
		for (AbilityActivation eventType : AbilityActivation.values()) {
			registeredAbilityMap.put(eventType, new ArrayList<SkillAbility>());
		}
	}
	
	/**
	 * Gets the plugin this controller is registered to.
	 * @return The ArchesporeAdventureMain class.
	 */
	protected ArchesporeAdventureMain getRegisteredPlugin() {
		return plugin;
	}
	
	/**
	 * Gets the IO file for this skill
	 * @return the skill's File
	 */
	public File getSkillFile() {
		return skillFile;
	}
	
	/**
	 * Gets the YML configuration for this skill
	 * @return the skill's YML
	 */
	public FileConfiguration getSkillFileYML() {
		return skillFileYML;
	}
	
	/**
	 * Checks if the specified ability is registered within this controller
	 * @param abilityName abilty to check
	 * @return true/false
	 */
	public boolean doesContainAbility(String abilityName) {
		for (SkillAbility ability : getRegisteredAbilities()) {
			if (ChatColor.stripColor(ability.getDisplayName()).equals(abilityName)) { return true; }
		}
		return false;
	}
	
	/**
	 * Gets the SkillAbility for the specified name if is registered.
	 * @param abilityName the ability to get.
	 * @return the SkillAbility, or null if the specified ability doesn't exist.
	 */
	public SkillAbility getSkillAbility(String abilityName) {
		for (SkillAbility ability : getRegisteredAbilities()) {
			if (ChatColor.stripColor(ability.getDisplayName()).equals(abilityName)) { return ability; }
		}
		return null;
	}
	
	/**
	 * Gets a collection of all registered skill abilities.
	 * @return a list of SkillAbility names.
	 */
	public List<SkillAbility> getRegisteredAbilities() {
		List<SkillAbility> registeredAbilities = new ArrayList<>();
		registeredAbilityMap.values().forEach( list -> list.forEach( ability -> registeredAbilities.add(ability)));
		return registeredAbilities;
	}
	
	/**
	 * Plays the events for all registered abilites of the specified AbilityActivation
	 * @param eventType the AbilityActivation of this event
	 * @param event the event information
	 */
	public void abilityEvent(AbilityActivation eventType, Event event) {
		for (SkillAbility ability : registeredAbilityMap.get(eventType)) {
			ability.abilityEffect(event);
		}
	}
	
	/**
	 * Adds the SkillAbility to the registered ability map and list.
	 * @param ability the ability to add.
	 */
	protected void registerSkillAbility(SkillAbility ability) {
		registeredAbilityMap.get(ability.getActivationType()).add(ability);
	}
	
	/**
	 * Loads the File and FileConfiguration for this skill controller
	 * @param skillType the skillType of this controller, determines which config to load.
	 */
	private void loadFiles(SkillType skillType) {
		String skillFileName = StringUtils.capitalize(skillType.toString().toLowerCase()) + "SkillConfig.yml";
		skillFile = new File(plugin.getDataFolder(), skillFileName);
		if (!skillFile.exists()) {
			skillFile.getParentFile().mkdirs();
			plugin.saveResource(skillFileName, true);
		}
		
		skillFileYML = new YamlConfiguration();
		try { skillFileYML.load(skillFile); } 
		catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
	}
	
	/**
	 * Load the configs set in the File related to this skill.
	 */
	protected abstract void loadFileConfigurations();
}
