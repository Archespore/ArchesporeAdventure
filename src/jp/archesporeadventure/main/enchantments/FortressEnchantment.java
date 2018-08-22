package jp.archesporeadventure.main.enchantments;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.enchantments.CustomEnchantment;
import jp.archesporeadventure.main.utils.EnchantmentUtil;
import jp.archesporeadventure.main.utils.ParticleUtil;
import jp.archesporeadventure.main.utils.PotionEffectUtil;

public class FortressEnchantment extends SpecialEnchantment {
	
	final double MAX_EFFECT_CHANCE_COMBINED = 15.0;
	final int EFFECT_DURATION_BASE = 60;
	final int EFFECT_DURATION_INCREMENT = 5;

	public FortressEnchantment(NamespacedKey enchID) {
		super(enchID);
	}

	public String getName() {
		return "Fortress";
	}
	
	public int getMaxLevel() {
		return 2;
	}

	public boolean isPassive() {
		return false;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.WEARABLE;
	}

	/**
	 * Has a chance to give the defender of an attack absorption and resistance.
	 */
	public boolean enchantmentEffect(Event event) {
		if (event instanceof EntityDamageEvent) {
			
			EntityDamageEvent damageEvent = (EntityDamageEvent) event;
			Entity eventDefender = damageEvent.getEntity();
			
			if (eventDefender instanceof LivingEntity) {
				
				LivingEntity entityLivingDefender = (LivingEntity) eventDefender;
				ItemStack[] armorItems = entityLivingDefender.getEquipment().getArmorContents();
				Map<ItemStack, Integer> enchantLevels = EnchantmentUtil.getEnchantmentLevels(armorItems, CustomEnchantment.FORTRESS.getEnchant());
				int effectDuration = EFFECT_DURATION_BASE;
				double effectProcChance = 0;
				
				for(int loopValue = 0; loopValue < armorItems.length; loopValue++) {
					if (armorItems[loopValue] != null) {
						if (enchantLevels.get(armorItems[loopValue]) != 0) {
							effectProcChance = effectProcChance + (enchantLevels.get(armorItems[loopValue]) * ((MAX_EFFECT_CHANCE_COMBINED / 4) / this.getMaxLevel()));
							effectDuration = effectDuration + (EFFECT_DURATION_INCREMENT * enchantLevels.get(armorItems[loopValue]));
						}
					}
				}
				
				if (ThreadLocalRandom.current().nextDouble(0, 100) < effectProcChance) {
					
					PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, effectDuration, 0), entityLivingDefender);
					entityLivingDefender.addPotionEffect(newPotionEffect, true);
					
					newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, effectDuration, 0), entityLivingDefender);
					entityLivingDefender.addPotionEffect(newPotionEffect, true);
					
					entityLivingDefender.getWorld().playSound(entityLivingDefender.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, .75f, 1.5f);
					ParticleUtil.spawnWorldParticles(Particle.SMOKE_NORMAL, entityLivingDefender.getLocation().add(0, 1, 0), 10, .25, .25, .25, .05);
					
					return true;
				}
			}
		}
		return false;
	}
}
