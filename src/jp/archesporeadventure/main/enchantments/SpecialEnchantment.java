package jp.archesporeadventure.main.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public abstract class SpecialEnchantment extends Enchantment{

	public SpecialEnchantment(NamespacedKey id) {
		super(id);
	}
	
	/**
	 * Checks if an item can contain this enchantment.
	 * @return Whether this item can naturally contain this enchantment.
	 */
	public boolean canEnchantItem(ItemStack itemstack){
		return true;
	}
	
	/**
	 * Checks if this enchantment conflicts with another.
	 * @return Whether this enchantment can coexist naturally with another.
	 */
	public boolean conflictsWith(Enchantment enchantment){
		return false;
	}
	
	/**
	 * Gets the level that this enchantment should start at.
	 * @return The default starting level of the enchantment.
	 */
	public int getStartLevel() {
		return 0;
	}
	
	/**
	 * Checks if enchantment is labeled as a curse
	 * @return True if enchantment is cursed.
	 */
	public boolean isCursed(){
		return false;
	}
	
	/**
	 * Checks if enchantment is labeled as a treasure.
	 * @return True is enchantment is treasure.
	 */
	public boolean isTreasure(){
		return false;
	}
	
	/**
	 * Checks if enchantment is labeled as a support.
	 * @return True is enchantment is support.
	 */
	public boolean isSupport(){
		return false;
	}
	
	/**
	 * The effect of the enchantment, called whenever the effect should go off.
	 * @return returns whether or not the enchantment effect activated.
	 */
	public boolean enchantmentEffect(Player player, int enchantmentLevel) {
		return true;
	}
	
	/**
	 * The effect of the enchantment, called whenever the effect should go off.
	 * @return returns whether or not the enchantment effect activated.
	 */
	public boolean enchantmentEffect(Event event) {
		return true;
	}

	/**
	 * Gets the type of ItemStack that can naturally contain this enchantment.
	 * @return The EnchantmentTarget type that can contain this enchantment.
	 */
	public abstract EnchantmentTarget getItemTarget();
	
	/**
	 * Gets the maximum level that this enchantment can naturally occur at.
	 * @return The default maximum level of this enchantment.
	 */
	public abstract int getMaxLevel();
	
	/**
	 * Gets the internal name of the enchantment
	 * @return The internal name of the enchantment
	 */
	public abstract String getName();
	
	/**
	 * Checks if enchantment is considered a passive enchantment
	 * @return True if this enchantment is not activated by an event.
	 */
	public abstract boolean isPassive();
}
