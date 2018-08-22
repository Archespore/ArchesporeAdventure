package jp.archesporeadventure.main.enchantments;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class BlastingEnchantment extends SpecialEnchantment {
	
	final double EXPLOSION_BASE_POWER = .5;
	final double EXPLOSION_GROWTH = .25;

	public BlastingEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Blasting";
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
	 * Creates an explosion at the defender when the attacker hits them.
	 */
	public boolean enchantmentEffect(Event event) {
		if (event instanceof EntityDamageByEntityEvent) {
			
			EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
			Entity entityAttacker = damageByEntityEvent.getDamager();
			Entity entityDefender = damageByEntityEvent.getEntity();
			
			if ( (entityAttacker instanceof LivingEntity) && (entityDefender instanceof LivingEntity) ){
				
				LivingEntity entityLivingAttacker = (LivingEntity) entityAttacker;
				LivingEntity entityLivingDefender = (LivingEntity) entityDefender;
				ItemStack attackerWeapon = entityLivingAttacker.getEquipment().getItemInMainHand();
				Location defenderLocation = entityLivingDefender.getLocation();
				
				int enchantmentLevel = attackerWeapon.getEnchantments().get(this);
				
				entityLivingDefender.getWorld().createExplosion(defenderLocation.getX(), defenderLocation.getY() + 1, defenderLocation.getZ(), (float) (((enchantmentLevel - 1) * EXPLOSION_GROWTH) + EXPLOSION_BASE_POWER), false, false);
				return true;
			}
		}
		return false;
	}
}
