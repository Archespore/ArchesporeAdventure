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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.enchantments.CustomEnchantment;
import jp.archesporeadventure.main.utils.EnchantmentUtil;

public class MoltenEnchantment extends SpecialEnchantment {
	
	final double MAX_EFFECT_CHANCE_COMBINED = 100.0;

	public MoltenEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Molten";
	}

	public int getMaxLevel() {
		return 5;
	}

	public boolean isPassive() {
		return false;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.WEARABLE;
	}
	
	/**
	 * Has a chance to ignite the attacker on fire.
	 */
	public boolean enchantmentEffect(Event event) {
		if (event instanceof EntityDamageByEntityEvent) {
			
			EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
			Entity eventAttacker = damageByEntityEvent.getDamager();
			Entity eventDefender = damageByEntityEvent.getEntity();
			
			if ( (eventAttacker instanceof LivingEntity) && (eventDefender instanceof LivingEntity) ) {
				
				LivingEntity eventLivingAttacker = (LivingEntity) eventAttacker;
				LivingEntity eventLivingDefender = (LivingEntity) eventDefender;
				ItemStack[] armorItems = eventLivingDefender.getEquipment().getArmorContents();
				int highestEnchant = EnchantmentUtil.findHighestEnchantMap(armorItems, CustomEnchantment.MOLTEN.getEnchant()).get(CustomEnchantment.MOLTEN.getEnchant());
				Map<ItemStack, Integer> enchantLevels = EnchantmentUtil.getEnchantmentLevels(armorItems, CustomEnchantment.MOLTEN.getEnchant());
				double effectChance = 0;
				
				for(int loopValue = 0; loopValue < armorItems.length; loopValue++) {
					if ( (armorItems[loopValue] != null) && (enchantLevels.get(armorItems[loopValue]) != 0) ){
						effectChance = effectChance + (enchantLevels.get(armorItems[loopValue]) * ((MAX_EFFECT_CHANCE_COMBINED / 4) / this.getMaxLevel()));
					}
				}
		
				if (ThreadLocalRandom.current().nextDouble(0, 100) < effectChance) {
					if ( (highestEnchant >= 5) && (eventLivingAttacker.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) ){
						PotionEffect fireResistance = eventLivingAttacker.getPotionEffect(PotionEffectType.FIRE_RESISTANCE);
						eventLivingAttacker.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, fireResistance.getDuration() - 40, fireResistance.getAmplifier()), true);
					}
					new BukkitRunnable() {
						
						public void run() {
							eventLivingAttacker.setFireTicks(highestEnchant * 20);
						}
					}.runTask(ArchesporeAdventureMain.getPlugin());
					return true;
				}
			}
		}
		return false;
	}
}
