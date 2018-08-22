package jp.archesporeadventure.main.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.utils.PotionEffectUtil;

public class HeartyEnchantment extends SpecialEnchantment {
	
	final int EFFECT_DURATION_BASE = 0;
	final int EFFECT_DURATION_INCREMENT = 12;

	public HeartyEnchantment(NamespacedKey enchID) {
		super(enchID);
	}

	public String getName() {
		return "Hearty";
	}
	
	public int getMaxLevel() {
		return 5;
	}
	
	public boolean isPassive() {
		return false;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ALL;
	}

	/**
	 * Gives the player regeneration upon eating.
	 */
	public boolean enchantmentEffect(Event event) {
		if (event instanceof PlayerItemConsumeEvent) {
			
			PlayerItemConsumeEvent itemConsumeEvent = (PlayerItemConsumeEvent) event;
			Player player = itemConsumeEvent.getPlayer();
			ItemStack consumedItem = itemConsumeEvent.getItem();
			int enchantmentLevel = consumedItem.getEnchantments().get(this);
			
			PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 
					EFFECT_DURATION_BASE + (EFFECT_DURATION_INCREMENT * enchantmentLevel), 2), player);
			player.addPotionEffect(newPotionEffect, true);
			return true;
		}
		return true;
	}

}
