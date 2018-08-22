package jp.archesporeadventure.main.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.utils.PotionEffectUtil;

public class QuickeningEnchantment extends SpecialEnchantment {

	public QuickeningEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Quickening";
	}

	public int getMaxLevel() {
		return 1;
	}
	
	public boolean isPassive() {
		return false;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.WEARABLE;
	}

	/**
	 * Gives an entity a burst of speed upon being hit.
	 */
	public boolean enchantmentEffect(Event event) {
		if (event instanceof EntityDamageEvent) {
			
			EntityDamageEvent entityDamageEvent = (EntityDamageEvent) event;
			Entity eventDefender = entityDamageEvent.getEntity();
			
			if (eventDefender instanceof LivingEntity) {
				
				LivingEntity eventLivingDefender = (LivingEntity) eventDefender;
				PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.SPEED, 30, 0), eventLivingDefender);
				eventLivingDefender.addPotionEffect(newPotionEffect, true);
				return true;
			}
		}
		return false;
	}
}
