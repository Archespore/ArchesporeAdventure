package jp.archesporeadventure.main.enchantments;

import java.util.Map;

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

public class SacraficeEnchantment extends SpecialEnchantment {
	
	final int EFFECT_RESISTANCE_DURATION_BASE = 0;
	final int EFFECT_RESISTANCE_DURATION_INCREMENT = 10;
	final int EFFECT_REGENERATION_DURATION_BASE = 0;
	final int EFFECT_REGENERATION_DURATION_INCREMENT = 12;

	public SacraficeEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Sacrafice";
	}

	public int getMaxLevel() {
		return 3;
	}

	public boolean isPassive() {
		return false;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.WEARABLE;
	}

	/**
	 * Gives an entity regeneration and resistance upon taking lethal damage.
	 */
	public boolean enchantmentEffect(Event event) {
		if (event instanceof EntityDamageEvent) {
			
			EntityDamageEvent entityDamageEvent = (EntityDamageEvent) event;
			Entity eventDefender = entityDamageEvent.getEntity();
			
			if (eventDefender instanceof LivingEntity) {
				
				LivingEntity eventLivingDefender = (LivingEntity) eventDefender;
				
				if (eventLivingDefender.getHealth() - entityDamageEvent.getFinalDamage() <= 0) {
					
					entityDamageEvent.setDamage(0);
					eventLivingDefender.setHealth(.5);
					ItemStack[] armorItems = eventLivingDefender.getEquipment().getArmorContents();
					eventLivingDefender.getEquipment().setArmorContents(new ItemStack[4]);
					Map<ItemStack, Integer> enchantmentLevels = EnchantmentUtil.getEnchantmentLevels(armorItems, CustomEnchantment.SACRAFICE.getEnchant());
					int enchantmentLevelCombined = 0;
					
					for (ItemStack armor : armorItems) {
						if (armor != null) {
							enchantmentLevelCombined += enchantmentLevels.get(armor);
						}
					}
			
					PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 
							(EFFECT_RESISTANCE_DURATION_BASE + (EFFECT_RESISTANCE_DURATION_INCREMENT * enchantmentLevelCombined)), 4), eventLivingDefender);
					eventLivingDefender.addPotionEffect(newPotionEffect, true);
					
					PotionEffectUtil.forcePotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 
							EFFECT_REGENERATION_DURATION_BASE + (EFFECT_REGENERATION_DURATION_INCREMENT * enchantmentLevelCombined), 3), eventLivingDefender);
	
					if (enchantmentLevelCombined >= 12) {
						newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 600, (int)Math.floor(enchantmentLevelCombined / 4) - 3), eventLivingDefender);
						eventLivingDefender.addPotionEffect(newPotionEffect, true);
					}
					
					eventLivingDefender.getWorld().playSound(eventLivingDefender.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1.5f, 1.25f);
					ParticleUtil.spawnWorldParticles(Particle.TOTEM, eventLivingDefender.getLocation(), 15, 0, .25, 0, .25);
					return true;
				}
			}
		}
		return false;
	}
}
