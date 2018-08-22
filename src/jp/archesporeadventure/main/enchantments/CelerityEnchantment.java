package jp.archesporeadventure.main.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.utils.PotionEffectUtil;

public class CelerityEnchantment extends SpecialEnchantment {
	
	final int EFFECT_DURATION_BASE = 30;
	final int EFFECT_DURATION_INCREMENT = 0;

	public CelerityEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Celerity";
	}

	public int getMaxLevel() {
		return 2;
	}
	
	public boolean isPassive() {
		return false;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.BOW;
	}

	/**
	 * Grants an entity a burst of speed when they fire a bow.
	 */
	public boolean enchantmentEffect(Event event) {
		if (event instanceof EntityShootBowEvent) {
			
			EntityShootBowEvent bowShootEvent = (EntityShootBowEvent) event;
			LivingEntity shooter = bowShootEvent.getEntity();
			ItemStack shooterBow = bowShootEvent.getBow();
			int enchantmentLevel = shooterBow.getEnchantments().get(this);
			
			PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.SPEED, 
					EFFECT_DURATION_BASE + (enchantmentLevel * EFFECT_DURATION_INCREMENT), (enchantmentLevel - 1)), shooter);
			shooter.addPotionEffect(newPotionEffect, true);
			return true;
		}
		return false;
	}
}
