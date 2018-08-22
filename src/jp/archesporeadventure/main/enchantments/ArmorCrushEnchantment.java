package jp.archesporeadventure.main.enchantments;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.utils.ParticleUtil;

public class ArmorCrushEnchantment extends SpecialEnchantment {

	final double MAX_EFFECT_CHANCE = 25.0;
	
	public ArmorCrushEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Armor Crush";
	}

	public int getMaxLevel() {
		return 2;
	}
	
	public boolean isPassive() {
		return false;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ALL;
	}

	/**
	 * Reduces the durability of the defender's armor if the enchantment activates.
	 */
	public boolean enchantmentEffect(Event event) {
		if (event instanceof EntityDamageByEntityEvent) {
			
			EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
			Entity eventAttacker = damageByEntityEvent.getDamager();
			Entity eventDefener = damageByEntityEvent.getEntity();
			
			if ( (eventAttacker instanceof LivingEntity) && (eventDefener instanceof LivingEntity) ) {
			
				LivingEntity eventLivingAttacker = (LivingEntity) eventAttacker;
				LivingEntity eventLivingDefener = (LivingEntity) eventDefener;
				ItemStack attackerWeapon = eventLivingAttacker.getEquipment().getItemInMainHand();
				ItemStack[] defenderArmor = eventLivingDefener.getEquipment().getArmorContents();
				int enchantmentLevel = attackerWeapon.getEnchantments().get(this);
				double effectProcChance = (MAX_EFFECT_CHANCE / this.getMaxLevel()) * enchantmentLevel;
				//Boolean that is used to determine if effect activated at least once.
				boolean effectActivated = false;
				
				for (int loopValue = 0; loopValue < defenderArmor.length; loopValue++) {
					if (ThreadLocalRandom.current().nextDouble(0, 100) < effectProcChance) {
						if (defenderArmor[loopValue] != null && defenderArmor[loopValue].getType() != Material.AIR) {
							defenderArmor[loopValue].setDurability((short) (defenderArmor[loopValue].getDurability() + 1));
							effectActivated = true;
						}
					}
				}
				if (effectActivated) {
					eventLivingDefener.getWorld().playSound(eventLivingDefener.getLocation(), Sound.BLOCK_ANVIL_LAND, .75f, .75f);
					ParticleUtil.spawnWorldParticles(Particle.BLOCK_CRACK, eventLivingDefener.getLocation().add(0, 1, 0), 10, .25, .25, .25, 0, Material.BEDROCK.createBlockData());
					return true;
				}
			}
		}
		return false;
	}
}
