package jp.archesporeadventure.main.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import jp.archesporeadventure.main.ArchesporeAdventureMain;

public class HeavyImpactEnchantment extends SpecialEnchantment {

	public HeavyImpactEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Heavy Impact";
	}

	public int getMaxLevel() {
		return 2;
	}

	public boolean isPassive() {
		return false;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.BOW;
	}

	/**
	 * Adds the heavyimpact metadata to arrows shot.
	 * The ProjectileHit Listener will be in charge of what happens when the arrow hits a target.
	 */
	public boolean enchantmentEffect(Event event) {
		if (event instanceof EntityShootBowEvent) {
			
			EntityShootBowEvent shootBowEvent = (EntityShootBowEvent) event;
			Entity projectile = shootBowEvent.getProjectile();
			ItemStack shooterBow = shootBowEvent.getBow();
			
			if (projectile instanceof Arrow) {
				
				Arrow arrowProjectile = (Arrow) projectile;
				int enchantmentLevel = shooterBow.getEnchantments().get(this);
				
				arrowProjectile.setMetadata("HEAVYIMPACT", new FixedMetadataValue(ArchesporeAdventureMain.getPlugin(), enchantmentLevel));
				return true;
			}
		}
		return false;
	}
}
