package jp.archesporeadventure.main.enchantments;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.enchantments.CustomEnchantment;
import jp.archesporeadventure.main.utils.EnchantmentUtil;

public class ConductiveEnchantment extends SpecialEnchantment {
	
	final double MAX_EFFECT_CHANCE_COMBINED = 35.0;

	public ConductiveEnchantment(NamespacedKey enchID) {
		super(enchID);
	}

	public String getName() {
		return "Conductive";
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
	 * Has a chance to strike an attacker with lightning
	 */
	public boolean enchantmentEffect(Event event) {
		if (event instanceof EntityDamageByEntityEvent) {
			
			EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
			Entity eventAttacker = damageByEntityEvent.getDamager();
			Entity eventDefender = damageByEntityEvent.getEntity();
			
			if ( (eventAttacker instanceof LivingEntity) && (eventDefender instanceof LivingEntity) ) {
				
				LivingEntity eventLivingDefender = (LivingEntity) eventDefender;
				ItemStack[] armorItems = eventLivingDefender.getEquipment().getArmorContents();
				
				Map<ItemStack, Integer> enchantLevels = EnchantmentUtil.getEnchantmentLevels(armorItems, CustomEnchantment.CONDUCTIVE.getEnchant());
				double effectProcChance = 0;
				
				for(int loopValue = 0; loopValue < armorItems.length; loopValue++) {
					if (armorItems[loopValue] != null) {
						if (enchantLevels.get(armorItems[loopValue]) != 0) {
							effectProcChance = effectProcChance + (enchantLevels.get(armorItems[loopValue]) * ((MAX_EFFECT_CHANCE_COMBINED / 4) / this.getMaxLevel()));
						}
					}
				}
		
				if (ThreadLocalRandom.current().nextDouble(0, 100) < effectProcChance) {
					eventAttacker.getWorld().strikeLightning(eventAttacker.getLocation());
					return true;
				}
			}
		}
		return false;
	}
}
