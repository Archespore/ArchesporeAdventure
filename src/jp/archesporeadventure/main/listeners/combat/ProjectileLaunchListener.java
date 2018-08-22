package jp.archesporeadventure.main.listeners.combat;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.projectiles.ProjectileSource;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.utils.MagicalItemsUtil;

public class ProjectileLaunchListener implements Listener{

	@EventHandler
	public void projectileLaunchEvent(ProjectileLaunchEvent event){
		
		Projectile eventProjectile = event.getEntity();
		ProjectileSource eventShooter = eventProjectile.getShooter();
		
		if (eventShooter instanceof Player){
			
			Player player = (Player) eventShooter;
			ItemStack projectileItem = player.getInventory().getItemInMainHand();
			
			if (eventProjectile instanceof Snowball){
				
				Snowball eventSnowball = (Snowball) eventProjectile;
				
				//Slowball, enchanted with power and applies slowness to hit entities.
				if (projectileItem.containsEnchantment(Enchantment.ARROW_DAMAGE)){
					eventSnowball.setMetadata("SLOWBALL", new FixedMetadataValue(ArchesporeAdventureMain.getPlugin(), true));
				}
				
				//Rock, makes snowballs do 2 health to hit entities.
				if (MagicalItemsUtil.doesContainMagicItem(Material.COAL, player)){
					eventSnowball.setMetadata("ROCKBALL", new FixedMetadataValue(ArchesporeAdventureMain.getPlugin(), 2));
				}
			}
			
			else if (event.getEntityType().equals(EntityType.SPLASH_POTION) || event.getEntityType().equals(EntityType.LINGERING_POTION)) {
				
				//Lightweight Bottle, increases potion throw distance.
				if (MagicalItemsUtil.doesContainMagicItem(Material.GLASS_BOTTLE, player, true)) {
					eventProjectile.setVelocity(event.getEntity().getVelocity().multiply(1.75));
				}
			}
		}
	}
}
