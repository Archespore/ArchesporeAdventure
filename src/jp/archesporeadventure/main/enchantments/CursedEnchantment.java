package jp.archesporeadventure.main.enchantments;

import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;

import jp.archesporeadventure.main.utils.LivingEntityUtil;

public class CursedEnchantment extends SpecialEnchantment {

	public CursedEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Cursed";
	}

	public int getMaxLevel() {
		return 1;
	}
	
	public boolean isPassive() {
		return true;
	}
	
	public boolean isCursed(){
		return true;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ALL;
	}
	
	/**
	 * Damages a player who has a cursed item in their inventory.
	 */
	public boolean enchantmentEffect(Player player, int enchantmentLevel) {
			
		if (enchantmentLevel > 0) {
			if (player.getGameMode() != GameMode.CREATIVE) {
				LivingEntityUtil.removeHealth(player, 1);
				player.spawnParticle(Particle.DAMAGE_INDICATOR, player.getLocation().add(0, 1, 0), 5, .25, .5, .25, .25);
			}
			return true;
		}
		return false;
	}
}
