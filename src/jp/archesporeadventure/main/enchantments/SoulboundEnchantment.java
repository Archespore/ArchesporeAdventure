package jp.archesporeadventure.main.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;

public class SoulboundEnchantment extends SpecialEnchantment {

	public SoulboundEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Soulbound";
	}

	public int getMaxLevel() {
		return 1;
	}

	public boolean isPassive() {
		return true;
	}

	public boolean enchantmentEffect() {
		return false;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ALL;
	}
}
