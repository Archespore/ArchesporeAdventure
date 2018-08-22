package jp.archesporeadventure.main.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.utils.PotionEffectUtil;

public class LifestealEnchantment extends SpecialEnchantment {

	public LifestealEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Lifesteal";
	}

	public int getMaxLevel() {
		return 4;
	}

	public boolean isPassive() {
		return false;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ALL;
	}
	
	/**
	 * Grants the attacker regeneration upon killing an entity.
	 */
	public boolean enchantmentEffect(Event event) {
		if (event instanceof EntityDamageByEntityEvent) {
			
			EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
			Entity eventAttacker = damageByEntityEvent.getDamager();
			Entity eventDefender = damageByEntityEvent.getEntity();
			
			if (eventAttacker instanceof LivingEntity && eventDefender instanceof LivingEntity) {
				
				LivingEntity eventLivingAttacker = (LivingEntity) eventAttacker;
				LivingEntity eventLivingDefender = (LivingEntity) eventDefender;
				ItemStack attackerWeapon = eventLivingAttacker.getEquipment().getItemInMainHand();
				int enchantmentLevel = attackerWeapon.getEnchantments().get(this);
				
				if (eventLivingDefender.getHealth() - damageByEntityEvent.getFinalDamage() <= 0) {
					PotionEffectUtil.forcePotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (24 * enchantmentLevel), 2), eventLivingAttacker);
					if (eventAttacker instanceof Player) {
						((Player) eventAttacker).playSound(eventAttacker.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, .75f, .75f);
					}
				}
				return true;
			}
		}
		return false;
	}
}
