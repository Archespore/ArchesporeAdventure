package jp.archesporeadventure.main.enchantments;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.enchantments.CustomEnchantment;
import jp.archesporeadventure.main.utils.EnchantmentUtil;
import jp.archesporeadventure.main.utils.ItemStackUtil;

public class ReinforcedEnchantment extends SpecialEnchantment {
	
	final double MAX_CHANCE_PER_ARMOR = 25.0;

	public ReinforcedEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Reinforced";
	}

	public int getMaxLevel() {
		return 2;
	}
	
	public boolean isPassive() {
		return false;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.WEARABLE;
	}

	/**
	 * Chance of dealing additional durability to an attacker's weapon.
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
				ItemStack[] armorItems = eventLivingDefender.getEquipment().getArmorContents();
				//Boolean that is used to determine if effect activated at least once.
				boolean effectActivated = false;
			
				Map<ItemStack, Integer> enchantmentLevels = EnchantmentUtil.getEnchantmentLevels(armorItems, CustomEnchantment.REINFORCED.getEnchant());
				
				for (int loopValue = 0; loopValue < armorItems.length; loopValue++) {
					if (armorItems[loopValue] != null) {
						if (ThreadLocalRandom.current().nextDouble(100) < enchantmentLevels.get(armorItems[loopValue]) * (MAX_CHANCE_PER_ARMOR / this.getMaxLevel())) {
							ItemStackUtil.damageItem(attackerWeapon, 1);
							effectActivated = true;
						}
					}
				}
				if (effectActivated) {
					eventLivingAttacker.getWorld().playSound(eventLivingAttacker.getLocation(), Sound.ITEM_SHIELD_BREAK, .75f, .75f);
				}
				return effectActivated;
			}
		}
		return false;
	}
}
