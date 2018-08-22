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

public class AdrenalineEnchantment extends SpecialEnchantment {
	
	final double MAX_EFFECT_CHANCE = 25.0;
	final int EFFECT_DURATION_BASE = 30;
	final int EFFECT_DURATION_INCREMENT = 15;

	public AdrenalineEnchantment(NamespacedKey enchID) {
		super(enchID);
	}

	public String getName() {
		return "Adrenaline";
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
	 * Gives the player the haste potion effect if the enchantment activates.
	 */
	public boolean enchantmentEffect(Event event) {
		if (event instanceof EntityDamageByEntityEvent) {
			
			EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
			Entity eventAttacker = damageByEntityEvent.getDamager();
			Entity eventDefener = damageByEntityEvent.getEntity();
			
			if (eventAttacker instanceof LivingEntity && eventDefener instanceof LivingEntity) {
				
				LivingEntity eventLivingAttacker = (LivingEntity) eventAttacker;
				ItemStack attackerWeapon = eventLivingAttacker.getEquipment().getItemInMainHand();
				int enchantmentLevel = attackerWeapon.getEnchantments().get(this);
				double effectProcChance = (MAX_EFFECT_CHANCE / this.getMaxLevel()) * enchantmentLevel;
				
				if (ThreadLocalRandom.current().nextDouble(100) < effectProcChance) {
					
					PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 
							EFFECT_DURATION_BASE + (EFFECT_DURATION_INCREMENT * enchantmentLevel), (enchantmentLevel - 1)), eventLivingAttacker);
					eventLivingAttacker.addPotionEffect(newPotionEffect, true);
					
					eventLivingAttacker.getWorld().playSound(eventLivingAttacker.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, .75f, 1.75f);
					ParticleUtil.spawnWorldParticles(Particle.LAVA, eventLivingAttacker.getLocation().add(0, 1, 0), 8, .25, .25, .25, 1);
					return true;
				}
			}
		}
		return false;
	}
}
