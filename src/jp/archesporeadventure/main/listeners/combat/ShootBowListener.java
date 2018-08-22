package jp.archesporeadventure.main.listeners.combat;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.enchantments.CustomEnchantment;
import jp.archesporeadventure.main.enchantments.SpecialEnchantment;
import jp.archesporeadventure.main.utils.MagicalItemsUtil;

public class ShootBowListener implements Listener {

	@EventHandler
	public void shootBowEvent(EntityShootBowEvent event) {
		
		ItemStack shooterBow = event.getBow();
		LivingEntity shooter = event.getEntity();
		Entity eventProjectile = event.getProjectile();
		//boolean used to determine if this event is a support event.
		boolean supportEffect = false;
		
		//If the event is a support, set the boolean.
		if (shooterBow.getEnchantments().containsKey(CustomEnchantment.SUPPORT.getEnchant())) { supportEffect = true; }
		
		for (Enchantment enchantment : shooterBow.getEnchantments().keySet()) {
			if (enchantment instanceof SpecialEnchantment) {
				SpecialEnchantment specialEnchantment = (SpecialEnchantment) enchantment;
				if (!specialEnchantment.isPassive() && specialEnchantment.isSupport() == supportEffect) {
					specialEnchantment.enchantmentEffect(event);
				}
			}
		}
		
		if (shooter instanceof Player && eventProjectile instanceof Projectile) {
			
			//Arrow Fireworks, causes fireworks when an arrow lands.
			Player playerShooter = (Player) shooter;
			Projectile shooterProjectile = (Projectile) eventProjectile;
			if (MagicalItemsUtil.doesContainMagicItem(Material.PRISMARINE_CRYSTALS, playerShooter, true)) {
				shooterProjectile.setMetadata("FIREWORK", new FixedMetadataValue(ArchesporeAdventureMain.getPlugin(), true));
			}
		}
	}
}
