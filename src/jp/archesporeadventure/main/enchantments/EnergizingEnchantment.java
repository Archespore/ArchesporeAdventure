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

public class EnergizingEnchantment extends SpecialEnchantment {
	
	final int EFFECT_DURATION_BASE = 20;
	final int EFFECT_DURATION_INCREMENT = 20;

	public EnergizingEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Energizing";
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
	 * Gives a player a small boost of speed upon eating an item.
	 */
	public boolean enchantmentEffect(Event event) {
		if (event instanceof PlayerItemConsumeEvent) {
			
			PlayerItemConsumeEvent itemConsumeEvent = (PlayerItemConsumeEvent) event;
			Player player = itemConsumeEvent.getPlayer();
			ItemStack consumedItem = itemConsumeEvent.getItem();
			int enchantmentLevel = consumedItem.getEnchantments().get(this);
			
			PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.SPEED, 
					EFFECT_DURATION_BASE + (EFFECT_DURATION_INCREMENT * enchantmentLevel), 1), player);
			player.addPotionEffect(newPotionEffect, true);
			return true;
		}
		return true;
	}
}
