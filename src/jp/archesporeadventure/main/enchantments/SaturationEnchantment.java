package jp.archesporeadventure.main.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class SaturationEnchantment extends SpecialEnchantment {

	public SaturationEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Saturation";
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

	public boolean enchantmentEffect(Event event) {
		if (event instanceof PlayerItemConsumeEvent) {
			
			PlayerItemConsumeEvent itemConsumeEvent = (PlayerItemConsumeEvent) event;
			Player player = itemConsumeEvent.getPlayer();
			ItemStack consumedItem = itemConsumeEvent.getItem();
			int enchantmentLevel = consumedItem.getEnchantments().get(this);
			
			player.setFoodLevel(player.getFoodLevel() + enchantmentLevel);
			player.setSaturation(player.getSaturation() + enchantmentLevel);
			return true;
		}
		return false;
	}
}
