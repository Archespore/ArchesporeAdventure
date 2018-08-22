package jp.archesporeadventure.main.enchantments;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import jp.archesporeadventure.main.enchantments.CustomEnchantment;

public class RepairEnchantment extends SpecialEnchantment {

	public RepairEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Repairing";
	}

	public int getMaxLevel() {
		return 3;
	}
	
	public boolean isPassive() {
		return true;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.BREAKABLE;
	}

	/**
	 * Repairs items in a players inventory that has the repair enchantment
	 */
	public boolean enchantmentEffect(Player player, int enchantmentLevel) {

		if (enchantmentLevel > 0) {
			for(ItemStack loopItem : player.getInventory()) {
				if ( (loopItem != null) && (!loopItem.getType().equals(Material.AIR)) ) {
					
					//If the loop item contains the repair enchantment and is not full durability, we are going to repair it
					Map<Enchantment, Integer> itemEnchantments = loopItem.getEnchantments();
					if ( (itemEnchantments.containsKey(CustomEnchantment.REPAIR.getEnchant())) && (loopItem.getDurability() != 0) ){
						
						loopItem.setDurability((short) (loopItem.getDurability() - itemEnchantments.get(CustomEnchantment.REPAIR.getEnchant())));
					}
				}
			}
			return true;
		}
		return false;
	}
}
