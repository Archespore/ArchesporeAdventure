package jp.archesporeadventure.main.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.utils.LivingEntityUtil;

public class HemorrhageEnchantment extends SpecialEnchantment {

	public HemorrhageEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Hemorrhage";
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
	 * Adds bleed to an entity.
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
				
				LivingEntityUtil.addBleed(eventLivingDefender, (3 * enchantmentLevel));
				return true;
			}
		}
		return false;
	}
}
