package jp.archesporeadventure.main.enchantments;

import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.utils.ItemStackUtil;

public class JaggedEdgeEnchantment extends SpecialEnchantment {

	public JaggedEdgeEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Jagged Edge";
	}

	public int getMaxLevel() {
		return 2;
	}
	
	public boolean isPassive() {
		return false;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.BREAKABLE;
	}

	/**
	 * Deals extra damage in exchange for extra durability.
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
				
				damageByEntityEvent.setDamage(damageByEntityEvent.getDamage() + (2.5 * enchantmentLevel) );
				
				if (eventLivingAttacker instanceof Player) {
					if (((Player) eventLivingAttacker).getGameMode() != GameMode.CREATIVE) {
						ItemStackUtil.damageItem(attackerWeapon, enchantmentLevel);
					}
				}
			}
		}
		return false;
	}
}
