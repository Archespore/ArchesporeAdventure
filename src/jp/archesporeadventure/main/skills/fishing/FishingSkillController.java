package jp.archesporeadventure.main.skills.fishing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.abilities.fishing.AbundanceFisherAbility;
import jp.archesporeadventure.main.abilities.fishing.BarbaricFisherAbility;
import jp.archesporeadventure.main.abilities.fishing.CulinaryFisherAbility;
import jp.archesporeadventure.main.abilities.fishing.ExperienceFisherAbility;
import jp.archesporeadventure.main.abilities.fishing.FieryFisherAbility;
import jp.archesporeadventure.main.abilities.fishing.KelpFisherAbility;
import jp.archesporeadventure.main.abilities.fishing.MagicFisherAbility;
import jp.archesporeadventure.main.skills.SkillController;
import jp.archesporeadventure.main.skills.SkillType;

public class FishingSkillController extends SkillController {
	
	private List<ItemStack> registeredFishingRecipes = new ArrayList<>();
	
	private double catchXPReward;
	
	public FishingSkillController(ArchesporeAdventureMain plugin) {
		super(SkillType.FISHING, plugin);
		loadFileConfigurations();
	}
	
	/**
	 * Gets the xp reward for catching a fish.
	 * @return the amount of xp gained for cathing a fish.
	 */
	public double getCatchXPReward() {
		return catchXPReward;
	}
	
	public List<ItemStack> getRegisteredRecipes(){
		return registeredFishingRecipes;
	}

	protected void loadFileConfigurations() {
		catchXPReward = skillFileYML.getDouble("FishingConfig.catchReward");
		
		ConfigurationSection abilitySection;
		List<String> displayStrings;
		Material displayMaterial;
		String[] abilityInfo;
		String[] abilityLevelInfo;
		String[] minumumAbilityInfo;
		String[] maximumAbilityInfo;
		String minumumAbilityLevelInfo;
		String maximumAbilityLevelInfo;
		
		abilitySection = skillFileYML.getConfigurationSection("FishingAbilities.KelpFisher");
		displayStrings = new LinkedList<>(Arrays.asList(abilitySection.getString("displayString").split(";")));
		displayMaterial = Material.valueOf(abilitySection.getString("itemStackIcon"));
		abilityInfo = abilitySection.getString("abilityChance").split(",");
		minumumAbilityInfo = abilityInfo[0].split(":");
		maximumAbilityInfo = abilityInfo[1].split(":");
		abilityLevelInfo = abilitySection.getString("abilityLevels").split(",");
		minumumAbilityLevelInfo = abilityLevelInfo[0];
		maximumAbilityLevelInfo = abilityLevelInfo[1];
		KelpFisherAbility fishingAbilityKelp = new KelpFisherAbility(displayStrings, displayMaterial, Integer.valueOf(minumumAbilityInfo[0]), Double.valueOf(minumumAbilityInfo[1]),
				Integer.valueOf(maximumAbilityInfo[0]), Double.valueOf(maximumAbilityInfo[1]), Integer.valueOf(minumumAbilityLevelInfo), Integer.valueOf(maximumAbilityLevelInfo));
		registerSkillAbility(fishingAbilityKelp);
		
		abilitySection = skillFileYML.getConfigurationSection("FishingAbilities.ExperienceFisher");
		displayStrings = new LinkedList<>(Arrays.asList(abilitySection.getString("displayString").split(";")));
		displayMaterial = Material.valueOf(abilitySection.getString("itemStackIcon"));
		abilityInfo = abilitySection.getString("abilityChance").split(",");
		minumumAbilityInfo = abilityInfo[0].split(":");
		maximumAbilityInfo = abilityInfo[1].split(":");
		abilityLevelInfo = abilitySection.getString("abilityLevels").split(",");
		minumumAbilityLevelInfo = abilityLevelInfo[0];
		maximumAbilityLevelInfo = abilityLevelInfo[1];
		ExperienceFisherAbility fishingAbilityEXP = new ExperienceFisherAbility(displayStrings, displayMaterial, Integer.valueOf(minumumAbilityInfo[0]), Double.valueOf(minumumAbilityInfo[1]),
				Integer.valueOf(maximumAbilityInfo[0]), Double.valueOf(maximumAbilityInfo[1]), Integer.valueOf(minumumAbilityLevelInfo), Integer.valueOf(maximumAbilityLevelInfo));
		registerSkillAbility(fishingAbilityEXP);
		
		abilitySection = skillFileYML.getConfigurationSection("FishingAbilities.AbundanceFisher");
		displayStrings = new LinkedList<>(Arrays.asList(abilitySection.getString("displayString").split(";")));
		displayMaterial = Material.valueOf(abilitySection.getString("itemStackIcon"));
		abilityInfo = abilitySection.getString("abilityChance").split(",");
		minumumAbilityInfo = abilityInfo[0].split(":");
		maximumAbilityInfo = abilityInfo[1].split(":");
		abilityLevelInfo = abilitySection.getString("abilityLevels").split(",");
		minumumAbilityLevelInfo = abilityLevelInfo[0];
		maximumAbilityLevelInfo = abilityLevelInfo[1];
		AbundanceFisherAbility fishingAbilityDupe = new AbundanceFisherAbility(displayStrings, displayMaterial, Integer.valueOf(minumumAbilityInfo[0]), Double.valueOf(minumumAbilityInfo[1]),
				Integer.valueOf(maximumAbilityInfo[0]), Double.valueOf(maximumAbilityInfo[1]), Integer.valueOf(minumumAbilityLevelInfo), Integer.valueOf(maximumAbilityLevelInfo));
		registerSkillAbility(fishingAbilityDupe);
		
		abilitySection = skillFileYML.getConfigurationSection("FishingAbilities.BarbaricFisher");
		displayStrings = new LinkedList<>(Arrays.asList(abilitySection.getString("displayString").split(";")));
		displayMaterial = Material.valueOf(abilitySection.getString("itemStackIcon"));
		abilityInfo = abilitySection.getString("abilityChance").split(",");
		minumumAbilityInfo = abilityInfo[0].split(":");
		maximumAbilityInfo = abilityInfo[1].split(":");
		abilityLevelInfo = abilitySection.getString("abilityLevels").split(",");
		minumumAbilityLevelInfo = abilityLevelInfo[0];
		maximumAbilityLevelInfo = abilityLevelInfo[1];
		BarbaricFisherAbility fishingAbilityMob = new BarbaricFisherAbility(displayStrings, displayMaterial, Integer.valueOf(minumumAbilityInfo[0]), Double.valueOf(minumumAbilityInfo[1]),
				Integer.valueOf(maximumAbilityInfo[0]), Double.valueOf(maximumAbilityInfo[1]), Integer.valueOf(minumumAbilityLevelInfo), Integer.valueOf(maximumAbilityLevelInfo));
		registerSkillAbility(fishingAbilityMob);
		
		abilitySection = skillFileYML.getConfigurationSection("FishingAbilities.CulinaryFisher");
		displayStrings = new LinkedList<>(Arrays.asList(abilitySection.getString("displayString").split(";")));
		displayMaterial = Material.valueOf(abilitySection.getString("itemStackIcon"));
		abilityInfo = abilitySection.getString("abilityChance").split(",");
		minumumAbilityInfo = abilityInfo[0].split(":");
		maximumAbilityInfo = abilityInfo[1].split(":");
		abilityLevelInfo = abilitySection.getString("abilityLevels").split(",");
		minumumAbilityLevelInfo = abilityLevelInfo[0];
		maximumAbilityLevelInfo = abilityLevelInfo[1];
		CulinaryFisherAbility fishingAbilityFood = new CulinaryFisherAbility(displayStrings, displayMaterial, Integer.valueOf(minumumAbilityInfo[0]), Double.valueOf(minumumAbilityInfo[1]),
				Integer.valueOf(maximumAbilityInfo[0]), Double.valueOf(maximumAbilityInfo[1]), Integer.valueOf(minumumAbilityLevelInfo), Integer.valueOf(maximumAbilityLevelInfo));
		registerSkillAbility(fishingAbilityFood);
		
		abilitySection = skillFileYML.getConfigurationSection("FishingAbilities.FieryFisher");
		displayStrings = new LinkedList<>(Arrays.asList(abilitySection.getString("displayString").split(";")));
		displayMaterial = Material.valueOf(abilitySection.getString("itemStackIcon"));
		abilityInfo = abilitySection.getString("abilityChance").split(",");
		minumumAbilityInfo = abilityInfo[0].split(":");
		maximumAbilityInfo = abilityInfo[1].split(":");
		abilityLevelInfo = abilitySection.getString("abilityLevels").split(",");
		minumumAbilityLevelInfo = abilityLevelInfo[0];
		maximumAbilityLevelInfo = abilityLevelInfo[1];
		FieryFisherAbility fishingAbilityFlame = new FieryFisherAbility(displayStrings, displayMaterial, Integer.valueOf(minumumAbilityInfo[0]), Double.valueOf(minumumAbilityInfo[1]),
				Integer.valueOf(maximumAbilityInfo[0]), Double.valueOf(maximumAbilityInfo[1]), Integer.valueOf(minumumAbilityLevelInfo), Integer.valueOf(maximumAbilityLevelInfo));
		registerSkillAbility(fishingAbilityFlame);
		
		abilitySection = skillFileYML.getConfigurationSection("FishingAbilities.MagicFisher");
		displayStrings = new LinkedList<>(Arrays.asList(abilitySection.getString("displayString").split(";")));
		displayMaterial = Material.valueOf(abilitySection.getString("itemStackIcon"));
		abilityInfo = abilitySection.getString("abilityChance").split(",");
		minumumAbilityInfo = abilityInfo[0].split(":");
		maximumAbilityInfo = abilityInfo[1].split(":");
		abilityLevelInfo = abilitySection.getString("abilityLevels").split(",");
		minumumAbilityLevelInfo = abilityLevelInfo[0];
		maximumAbilityLevelInfo = abilityLevelInfo[1];
		MagicFisherAbility fishingAbilityMagic = new MagicFisherAbility(displayStrings, displayMaterial, Integer.valueOf(minumumAbilityInfo[0]), Double.valueOf(minumumAbilityInfo[1]),
				Integer.valueOf(maximumAbilityInfo[0]), Double.valueOf(maximumAbilityInfo[1]), Integer.valueOf(minumumAbilityLevelInfo), Integer.valueOf(maximumAbilityLevelInfo));
		registerSkillAbility(fishingAbilityMagic);
		
		abilitySection = skillFileYML.getConfigurationSection("FishingRecipes");
		ItemStack makiSushiItem = abilitySection.getItemStack("MakiSushi");
		ItemStack temakiSushiItem = abilitySection.getItemStack("TemakiSushi");
		ItemStack fishTacosItem = abilitySection.getItemStack("FishTacos");
		ItemStack fishAndChipsItem = abilitySection.getItemStack("FishAndChips");
		
		ShapedRecipe makiSushi = new ShapedRecipe(new NamespacedKey(plugin, "Maki_Sushi"), makiSushiItem)
				.shape("XXX","YZY","XXX")
				.setIngredient('X', Material.DRIED_KELP)
				.setIngredient('Y', Material.COD)
				.setIngredient('Z', Material.SALMON);
		ShapedRecipe temakiSushi = new ShapedRecipe(new NamespacedKey(plugin, "Temaki_Sushi"), temakiSushiItem)
				.shape("XXX","YZY","XXX")
				.setIngredient('X', Material.DRIED_KELP)
				.setIngredient('Y', Material.SALMON)
				.setIngredient('Z', Material.TROPICAL_FISH);
		ShapedRecipe fishTacos = new ShapedRecipe(new NamespacedKey(plugin, "Fish_Tacos"), fishTacosItem)
				.shape("YZY","XXX")
				.setIngredient('X', Material.WHEAT)
				.setIngredient('Y', Material.KELP)
				.setIngredient('Z', Material.COD);
		ShapelessRecipe fishAndChips = new ShapelessRecipe(new NamespacedKey(plugin, "Fish_and_Chips"), fishAndChipsItem)
				.addIngredient(Material.BAKED_POTATO)
				.addIngredient(Material.COOKED_COD);
		
		Bukkit.addRecipe(makiSushi);
		Bukkit.addRecipe(temakiSushi);
		Bukkit.addRecipe(fishTacos);
		Bukkit.addRecipe(fishAndChips);
		
		registeredFishingRecipes.add(makiSushi.getResult());
		registeredFishingRecipes.add(temakiSushi.getResult());
		registeredFishingRecipes.add(fishTacos.getResult());
		registeredFishingRecipes.add(fishAndChips.getResult());
	}
}
