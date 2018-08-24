package jp.archesporeadventure.main.skills.mining;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.abilities.mining.ChainMinerAbility;
import jp.archesporeadventure.main.abilities.mining.LootMinerAbility;
import jp.archesporeadventure.main.abilities.mining.XPMinerAbility;
import jp.archesporeadventure.main.skills.SkillController;
import jp.archesporeadventure.main.skills.SkillType;
import net.md_5.bungee.api.ChatColor;

public class MiningSkillController extends SkillController {

	private List<DepletedOre> depletedOreList = new ArrayList<>();
	private Map<Material, MiningSkillOre> registeredMiningOres = new HashMap<>();
	private List<Material> orderedOreList = new ArrayList<>();
	
	//The amount of ticks between each ore refresh.
	private final int ORE_REFRESH_TICK_CYCLE = 20;
	
	/**
	 * Constructor.
	 * @param plugin The ArchesporeAdventurePlugin.
	 */
	public MiningSkillController(ArchesporeAdventureMain plugin) {
		super(SkillType.MINING, plugin);
		loadFileConfigurations();
	}
	
	/**
	 * Adds a depleted ore to this controller.
	 * @param newDepletedOre the DepletedOre to add.
	 */
	public void addDepletedOre(DepletedOre newDepletedOre) {
		depletedOreList.add(newDepletedOre);
	}
	
	/**
	 * Checks if the specified ore type is registered within this controller
	 * @param oreType oreType to check
	 * @return true/false
	 */
	public boolean doesContainOre(Material oreType) {
		return registeredMiningOres.containsKey(oreType);
	}
	
	/**
	 * Gets the MiningSkillOre for the specified material if is registered.
	 * @param oreType the oreType to get.
	 * @return the MiningSkillOre, or null if the specified type doesn't exist.
	 */
	public MiningSkillOre getMiningOre(Material oreType) {
		if (doesContainOre(oreType)) {
			return registeredMiningOres.get(oreType);
		}
		return null;
	}
	
	/**
	 * Gets a collection of all registered mining ores.
	 * @return a list of MiningSkillOre material types.
	 */
	public List<Material> getRegisteredOres() {
		return new ArrayList<Material>(orderedOreList);
	}
	
	/**
	 * Checks each registered depleted ore to see if it should be refreshed.
	 * @param tickCount the tick cycle.
	 */
	public void refreshOres(int tickCount) {
		
		if (tickCount % ORE_REFRESH_TICK_CYCLE == 0) {
			for (DepletedOre ore : depletedOreList) {
				int oreTimer = ore.getOreTimer();
				if (oreTimer <= 0) {
					Location oreLocation = ore.getOreLocation();
					oreLocation.getBlock().setType(ore.getOreMaterial());
				}
				ore.setOreTimer(oreTimer - 1);
			}
			depletedOreList.removeIf( ore -> ore.getOreTimer() < 0);
		}
	}
	
	/**
	 * Resets all ores to their original state.
	 */
	public void resetOres() {
		for (DepletedOre ore : depletedOreList) {
			Location oreLocation = ore.getOreLocation();
			oreLocation.getBlock().setType(ore.getOreMaterial());
		}
		depletedOreList.clear();
	}
	
	/**
	 * Adds the MiningSkillOre to the registered ores map and list.
	 * @param ore the ore to add.
	 */
	private void registerMiningOre(MiningSkillOre ore) {
		registeredMiningOres.put(ore.getOreMaterial(), ore);
		orderedOreList.add(ore.getOreMaterial());
	}
	
	/**
	 * Loads the config options within the mining files.
	 */
	protected void loadFileConfigurations() {
		
		for (String configSection : skillFileYML.getConfigurationSection("MiningOres").getKeys(false)) {
			ConfigurationSection oreSection = skillFileYML.getConfigurationSection("MiningOres." + configSection);
			String displayName = ChatColor.translateAlternateColorCodes('&', oreSection.getString("displayString"));
			String[] oreHarvestInfo = oreSection.getString("harvestChance").split(",");
			String[] minumumHarvestInfo = oreHarvestInfo[0].split(":");
			String[] maximumHarvestInfo = oreHarvestInfo[1].split(":");
			
			String[] blockDrops = oreSection.getString("materialDrops").split(",");
			Material[] itemDrops = new Material[blockDrops.length];
			int arrayValue = 0;
			for (String drop : blockDrops) {
				itemDrops[arrayValue] = Material.valueOf(drop);
				arrayValue++;
			}
			
			MiningSkillOre registeredOre = new MiningSkillOre(Material.valueOf(configSection), displayName, Integer.valueOf(minumumHarvestInfo[0]), Double.valueOf(minumumHarvestInfo[1]), 
					Integer.valueOf(maximumHarvestInfo[0]), Double.valueOf(maximumHarvestInfo[1]), itemDrops, oreSection.getDouble("blockXPAmount"), oreSection.getInt("blockHardness"), oreSection.getInt("renewDuration"));
			registerMiningOre(registeredOre);
		}
		
		ConfigurationSection abilitySection;
		List<String> displayStrings;
		Material displayMaterial;
		String[] abilityInfo;
		String[] abilityLevelInfo;
		String[] minumumAbilityInfo;
		String[] maximumAbilityInfo;
		String minumumAbilityLevelInfo;
		String maximumAbilityLevelInfo;
		
		abilitySection = skillFileYML.getConfigurationSection("MiningAbilities.ChainMiner");
		displayStrings = new LinkedList<>(Arrays.asList(abilitySection.getString("displayString").split(";")));
		displayMaterial = Material.valueOf(abilitySection.getString("itemStackIcon"));
		abilityInfo = abilitySection.getString("abilityChance").split(",");
		minumumAbilityInfo = abilityInfo[0].split(":");
		maximumAbilityInfo = abilityInfo[1].split(":");
		abilityLevelInfo = abilitySection.getString("abilityLevels").split(",");
		minumumAbilityLevelInfo = abilityLevelInfo[0];
		maximumAbilityLevelInfo = abilityLevelInfo[1];
		ChainMinerAbility miningAbilityChain = new ChainMinerAbility(displayStrings, displayMaterial, Integer.valueOf(minumumAbilityInfo[0]), Double.valueOf(minumumAbilityInfo[1]),
				Integer.valueOf(maximumAbilityInfo[0]), Double.valueOf(maximumAbilityInfo[1]), Integer.valueOf(minumumAbilityLevelInfo), Integer.valueOf(maximumAbilityLevelInfo));
		registerSkillAbility(miningAbilityChain);
		
		abilitySection = skillFileYML.getConfigurationSection("MiningAbilities.XPMiner");
		displayStrings = new LinkedList<>(Arrays.asList(abilitySection.getString("displayString").split(";")));
		displayMaterial = Material.valueOf(abilitySection.getString("itemStackIcon"));
		abilityInfo = abilitySection.getString("abilityChance").split(",");
		minumumAbilityInfo = abilityInfo[0].split(":");
		maximumAbilityInfo = abilityInfo[1].split(":");
		abilityLevelInfo = abilitySection.getString("abilityLevels").split(",");
		minumumAbilityLevelInfo = abilityLevelInfo[0];
		maximumAbilityLevelInfo = abilityLevelInfo[1];
		XPMinerAbility miningAbilityXP = new XPMinerAbility(displayStrings, displayMaterial, Integer.valueOf(minumumAbilityInfo[0]), Double.valueOf(minumumAbilityInfo[1]),
				Integer.valueOf(maximumAbilityInfo[0]), Double.valueOf(maximumAbilityInfo[1]), Integer.valueOf(minumumAbilityLevelInfo), Integer.valueOf(maximumAbilityLevelInfo));
		registerSkillAbility(miningAbilityXP);
		
		abilitySection = skillFileYML.getConfigurationSection("MiningAbilities.LootMiner");
		displayStrings = new LinkedList<>(Arrays.asList(abilitySection.getString("displayString").split(";")));
		displayMaterial = Material.valueOf(abilitySection.getString("itemStackIcon"));
		abilityInfo = abilitySection.getString("abilityChance").split(",");
		minumumAbilityInfo = abilityInfo[0].split(":");
		maximumAbilityInfo = abilityInfo[1].split(":");
		abilityLevelInfo = abilitySection.getString("abilityLevels").split(",");
		minumumAbilityLevelInfo = abilityLevelInfo[0];
		maximumAbilityLevelInfo = abilityLevelInfo[1];
		LootMinerAbility miningAbilityLoot = new LootMinerAbility(displayStrings, displayMaterial, Integer.valueOf(minumumAbilityInfo[0]), Double.valueOf(minumumAbilityInfo[1]),
				Integer.valueOf(maximumAbilityInfo[0]), Double.valueOf(maximumAbilityInfo[1]), Integer.valueOf(minumumAbilityLevelInfo), Integer.valueOf(maximumAbilityLevelInfo));
		registerSkillAbility(miningAbilityLoot);
	}
}
