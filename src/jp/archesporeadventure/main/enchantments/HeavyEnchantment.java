package jp.archesporeadventure.main.enchantments;

import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.utils.PotionEffectUtil;

public class HeavyEnchantment extends SpecialEnchantment {

	public HeavyEnchantment(NamespacedKey enchID) {
		super(enchID);
	}

	public String getName() {
		return "Heavy";
	}
	
	public int getMaxLevel() {
		return 2;
	}

	public boolean isPassive() {
		return true;
	}
	
	public boolean isCursed(){
		return true;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ALL;
	}

	/**
	 * Slows a player who has a heavy item in their inventory.
	 */
	public boolean enchantmentEffect(Player player, int enchantmentLevel) {
		
		if (enchantmentLevel > 0) {
			if (player.getGameMode() != GameMode.CREATIVE) {
				PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, (enchantmentLevel - 1)), player);
				player.addPotionEffect(newPotionEffect, true);
			}
			return true;
		}
		return false;
	}
}
