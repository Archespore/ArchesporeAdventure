package jp.archesporeadventure.main.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.utils.PotionEffectUtil;

public class CloakingEnchantment extends SpecialEnchantment {

	public CloakingEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Cloaking";
	}
	
	public int getMaxLevel() {
		return 1;
	}
	
	public boolean isPassive() {
		return true;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ALL;
	}
	
	/**
	 * Gives invisibility to anyone holding a cloaking item.
	 */
	public boolean enchantmentEffect(Player player, int enchantmentLevel) {

		if (player.getEquipment().getItemInMainHand().getEnchantments().containsKey(CustomEnchantment.CLOAKING.getEnchant())) {
			PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 0), player);
			player.addPotionEffect(newPotionEffect, true);
			return true;
		}
		return false;
	}
}
