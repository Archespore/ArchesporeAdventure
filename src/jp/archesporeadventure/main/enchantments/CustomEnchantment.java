package jp.archesporeadventure.main.enchantments;

import java.util.Arrays;
import java.util.List;

import org.bukkit.NamespacedKey;

import jp.archesporeadventure.main.ArchesporeAdventureMain;

public enum CustomEnchantment {
	
	MAGICAL(new MagicalItemEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "MAGICAL"))), 
	SATURATION(new SaturationEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "SATURATION"))), HEARTY(new HeartyEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "HEARTY"))), 
	ENERGIZING(new EnergizingEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "ENERGIZING"))), REPAIR(new RepairEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "REPAIR"))), 
	SOULBOUND(new SoulboundEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "SOULBOUND"))), SUPPORT(new SupportEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "SUPPORT"))),
	HEALINGSTRIKE(new HealingStrikeEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "HEALINGSTRIKE"))), HEAVY(new HeavyEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "HEAVY"))), 
	CURSED(new CursedEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "CURSED"))), HEMORRHAGE(new HemorrhageEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "HEMORRHAGE"))), 
	LIFESTEAL(new LifestealEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "LIFESTEAL"))), KNOWLEDGE(new KnowledgeEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "KNOWLEDGE"))), 
	BLASTING(new BlastingEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "BLASTING"))), CRIPPLESTRIKE(new CrippleStrikeEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "CRIPPLESTRIKE"))), 
	ADRENALINE(new AdrenalineEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "ADRENALINE"))), JAGGEDEDGE(new JaggedEdgeEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "JAGGEDEDGE"))), 
	CLOAKING(new CloakingEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "CLOAKING"))), ARMORCRUSH(new ArmorCrushEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "ARMORCRUSH"))), 
	PARTICLE(new ParticleEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "PARTICLE"))), VOID(new VoidEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "VOID"))),
	STEALTH(new StealthEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "STEALTH"))), CELERITY(new CelerityEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "CELERITY"))), 
	BACKSTEP(new BackstepEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "BACKSTEP"))), MULTISHOT(new MultiShotEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "MULTISHOT"))), 
	HEAVYIMPACT(new HeavyImpactEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "HEAVYIMPACT"))), ELECTRICAL(new ElectricalEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "ELECTRICAL"))), 
	PIERCE(new PierceEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "PIERCE"))), RAINOFARROWS(new RainofArrowsEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "RAINOFARROWS"))),
	MOLTEN(new MoltenEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "MOLTEN"))), REINFORCED(new ReinforcedEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "REINFORCED"))), 
	FORGING(new ForgingEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "FORGING"))), CONDUCTIVE(new ConductiveEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "CONDUCTIVE"))), 
	QUICKENING(new QuickeningEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "QUICKENING"))), FORTRESS(new FortressEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "FORTRESS"))), 
	SACRAFICE(new SacraficeEnchantment(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "SACRAFICE")));

	final SpecialEnchantment enchant;
	
	static List<String> displayLevels = Arrays.asList("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X");
	
	private CustomEnchantment(SpecialEnchantment enchantment){
		this.enchant = enchantment;
	}
	
	public SpecialEnchantment getEnchant() {
		return this.enchant;
	}
	
	public static List<String> getDisplayLevels(){
		return displayLevels;
	}
}