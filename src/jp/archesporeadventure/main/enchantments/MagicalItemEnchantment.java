package jp.archesporeadventure.main.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;

public class MagicalItemEnchantment extends SpecialEnchantment {

	public MagicalItemEnchantment(NamespacedKey enchID) {
		super(enchID);
	}

	public String getName() {
		return "Magical Effect";
	}

	public int getMaxLevel() {
		return 1;
	}
	
	public boolean isTreasure() {
		return true;
	}

	public boolean isPassive() {
		return true;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ALL;
	}
}
