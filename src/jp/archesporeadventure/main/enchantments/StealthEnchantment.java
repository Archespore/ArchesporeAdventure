package jp.archesporeadventure.main.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.utils.PotionEffectUtil;

public class StealthEnchantment extends SpecialEnchantment {
	
	final int EFFECT_DURATION_BASE = 20;
	final int EFFECT_DURATION_INCREMENT = 30;

	public StealthEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Stealth";
	}

	@Override
	public int getMaxLevel() {
		return 2;
	}

	public boolean isPassive() {
		return false;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.BOW;
	}

	public boolean enchantmentEffect(Event event) {
		if (event instanceof EntityShootBowEvent) {
			
			EntityShootBowEvent bowShootEvent = (EntityShootBowEvent) event;
			LivingEntity shooter = bowShootEvent.getEntity();
			ItemStack shooterBow = bowShootEvent.getBow();
			int enchantmentLevel = shooterBow.getEnchantments().get(this);
			
			PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 
					EFFECT_DURATION_BASE + (enchantmentLevel * EFFECT_DURATION_INCREMENT), 0), shooter);
			shooter.addPotionEffect(newPotionEffect, true);
			
			shooter.getWorld().playSound(shooter.getLocation(), Sound.ENTITY_VEX_AMBIENT, 1.0f, 1.5f);
			return true;
		}
		return false;
	}
}
