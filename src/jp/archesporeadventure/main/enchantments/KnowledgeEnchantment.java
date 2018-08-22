package jp.archesporeadventure.main.enchantments;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class KnowledgeEnchantment extends SpecialEnchantment {
	
	final double MAX_EFFECT_CHANCE = 100.0;

	public KnowledgeEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Knowledge";
	}

	public int getMaxLevel() {
		return 5;
	}

	public boolean isPassive() {
		return false;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ALL;
	}

	/**
	 * Has a chance to spawn an experience orb upon attacking.
	 */
	public boolean enchantmentEffect(Event event) {
		if (event instanceof EntityDamageByEntityEvent) {
			
			EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
			Entity eventAttacker = damageByEntityEvent.getDamager();
			Entity eventDefender = damageByEntityEvent.getEntity();
			
			if ( (eventAttacker instanceof LivingEntity) && (eventDefender instanceof LivingEntity) ) {
				
				LivingEntity eventLivingAttacker = (LivingEntity) eventAttacker;
				ItemStack attackerWeapon = eventLivingAttacker.getEquipment().getItemInMainHand();
				int enchantmentLevel = attackerWeapon.getEnchantments().get(this);
				double effectProcChance = (MAX_EFFECT_CHANCE / this.getMaxLevel()) * enchantmentLevel;
				
				if (ThreadLocalRandom.current().nextDouble(100) < effectProcChance) {
					ExperienceOrb xpOrb = eventAttacker.getWorld().spawn(eventAttacker.getLocation(), ExperienceOrb.class);
					xpOrb.setExperience(1);
				}
			}
		}
		return false;
	}
}
