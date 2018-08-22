package jp.archesporeadventure.main.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.utils.ItemStackUtil;
import jp.archesporeadventure.main.utils.LivingEntityUtil;
import jp.archesporeadventure.main.utils.ParticleUtil;

public class HealingStrikeEnchantment extends SpecialEnchantment {

	public HealingStrikeEnchantment(NamespacedKey enchID) {
		super(enchID);
	}

	public String getName() {
		return "Healing Strike";
	}

	public int getMaxLevel() {
		return 2;
	}
	
	public boolean isTreasure() {
		return true;
	}

	public boolean isPassive() {
		return false;
	}
	
	public boolean isSupport(){
		return true;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ALL;
	}
	
	/**
	 * Heals the target hit.
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
				
				int enchantmentLevel = attackerWeapon.getEnchantments().get(this);
				LivingEntityUtil.addHealth(entityLivingDefender, enchantmentLevel);
				ParticleUtil.spawnWorldParticles(Particle.HEART, entityLivingDefender.getLocation().add(0, 1, 0), 7, .25, .25, .25);
				ItemStackUtil.damageItem(attackerWeapon, 2);
				
				return true;
			}
		}
		return false;
	}
}
