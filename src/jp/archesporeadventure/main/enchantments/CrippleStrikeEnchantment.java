package jp.archesporeadventure.main.enchantments;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.utils.ParticleUtil;
import jp.archesporeadventure.main.utils.PotionEffectUtil;

public class CrippleStrikeEnchantment extends SpecialEnchantment {
	
	final double MAX_EFFECT_CHANCE = 30.0;
	final int EFFECT_DURATION_BASE = 25;
	final int EFFECT_DURATION_INCREMENT = 15;

	public CrippleStrikeEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Cripple Strike";
	}

	public int getMaxLevel() {
		return 3;
	}
	
	public boolean isPassive() {
		return false;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ALL;
	}

	/**
	 * Gives the defender weakness for a short duration when the enchantment activates.
	 */
	public boolean enchantmentEffect(Event event) {
		if (event instanceof EntityDamageByEntityEvent) {
			
			EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
			Entity eventAttacker = damageByEntityEvent.getDamager();
			Entity eventDefender = damageByEntityEvent.getEntity();
			
			if ( (eventAttacker instanceof LivingEntity) && (eventDefender instanceof LivingEntity) ) {
				
				LivingEntity eventLivingAttacker = (LivingEntity) eventAttacker;
				LivingEntity eventLivingDefender = (LivingEntity) eventDefender;
				ItemStack attackerWeapon = eventLivingAttacker.getEquipment().getItemInMainHand();
				int enchantmentLevel = attackerWeapon.getEnchantments().get(this);
				double effectProcChance = (MAX_EFFECT_CHANCE / this.getMaxLevel()) * enchantmentLevel;
				
				if (ThreadLocalRandom.current().nextDouble(100) < effectProcChance) {
					
					PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 
							EFFECT_DURATION_BASE + (EFFECT_DURATION_INCREMENT * enchantmentLevel), 0), eventLivingDefender);
					eventLivingDefender.addPotionEffect(newPotionEffect, true);
					
					eventDefender.getWorld().playSound(eventDefender.getLocation(), Sound.ENTITY_SKELETON_DEATH, .75f, 1.25f);
					ParticleUtil.spawnWorldParticles(Particle.VILLAGER_ANGRY, eventDefender.getLocation().add(0, 1, 0), 5, .5, .5, .5, 0);
					return true;
				}
			}
		}
		return false;
	}
}
