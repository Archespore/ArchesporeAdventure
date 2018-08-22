package jp.archesporeadventure.main.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.Event;

public class TestEnchantment extends SpecialEnchantment {

	public TestEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Test Enchant";
	}

	public int getMaxLevel() {
		return 1;
	}
	
	public boolean isPassive() {
		return false;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ALL;
	}

	public boolean enchantmentEffect(Event event) {
		return false;
	}
}
