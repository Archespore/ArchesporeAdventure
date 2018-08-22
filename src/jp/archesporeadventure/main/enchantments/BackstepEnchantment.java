package jp.archesporeadventure.main.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

public class BackstepEnchantment extends SpecialEnchantment {
	
	final double BACKSTEP_POWER = .75;
	final double BACKSTEP_AIR_PENALTY = .5;

	public BackstepEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Backstep";
	}
	
	public int getMaxLevel() {
		return 1;
	}
	
	public boolean isPassive() {
		return false;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.BOW;
	}

	/**
	 * Pushes the entity backwards when they fire a bow.
	 */
	public boolean enchantmentEffect(Event event) {
		if (event instanceof EntityShootBowEvent) {
			
			EntityShootBowEvent bowShootEvent = (EntityShootBowEvent) event;
			LivingEntity shooter = bowShootEvent.getEntity();
			Vector shooterDirection = shooter.getLocation().getDirection();
			double backstepPitchPenalty = (shooter.getLocation().getPitch() / 90) * (BACKSTEP_POWER - BACKSTEP_AIR_PENALTY);
			
			if (shooter.isOnGround()) {
				shooter.setVelocity(shooterDirection.multiply(-BACKSTEP_POWER));
			}
			else {
				shooter.setVelocity(shooterDirection.multiply(-BACKSTEP_POWER + BACKSTEP_AIR_PENALTY + backstepPitchPenalty));
			}
			return true;
		}
		return false;
	}
}
