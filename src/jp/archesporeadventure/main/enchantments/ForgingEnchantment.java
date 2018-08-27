package jp.archesporeadventure.main.enchantments;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.enchantments.CustomEnchantment;
import jp.archesporeadventure.main.utils.EnchantmentUtil;
import jp.archesporeadventure.main.utils.ItemStackUtil;

public class ForgingEnchantment extends SpecialEnchantment {
	
	final double MAX_CHANCE_PER_ARMOR = 25.0;

	public ForgingEnchantment(NamespacedKey enchID) {
		super(enchID);
	}

	public String getName() {
		return "Forging";
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
	 * Has a chance to restore durability upon being taking damage.
	 */
	public boolean enchantmentEffect(Event event) {
		if (event instanceof EntityDamageEvent) {
			
			EntityDamageEvent damageEvent = (EntityDamageEvent) event;
			Entity eventDefender = damageEvent.getEntity();
			
			if (eventDefender instanceof LivingEntity) {
				
				LivingEntity eventLivingDefender = (LivingEntity) eventDefender;
				ItemStack[] armorItems = eventLivingDefender.getEquipment().getArmorContents();
				Map<ItemStack, Integer> enchantLevels = EnchantmentUtil.getEnchantmentLevels(armorItems, CustomEnchantment.FORGING.getEnchant());
				//Boolean that is used to determine if effect activated at least once.
				boolean effectActivated = false;
				
				for (int loopValue = 0; loopValue < armorItems.length; loopValue++) {
					if (armorItems[loopValue] != null) {
						if (ThreadLocalRandom.current().nextDouble(100) < (MAX_CHANCE_PER_ARMOR / this.getMaxLevel()) * enchantLevels.get(armorItems[loopValue])) {
							ItemStackUtil.damageItem(armorItems[loopValue], 1);
							effectActivated = true;
						}
					}
				}
				
				if ( (effectActivated) && (eventLivingDefender instanceof Player) ) {
					((Player) eventLivingDefender).playSound(eventLivingDefender.getLocation(), Sound.ENTITY_BLAZE_HURT, .75f, .75f);
				}
				return effectActivated;
			}
		}
		return false;
	}
}
