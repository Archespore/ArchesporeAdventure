package jp.archesporeadventure.main.listeners.entity;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityMountEvent;

import jp.archesporeadventure.main.utils.MagicalItemsUtil;

public class EntityMountListener implements Listener{

	@EventHandler
	public void entityMountEvent(EntityMountEvent event) {
		
		Entity eventMounted = event.getMount();
		Entity eventMounter = event.getEntity();
		
		if (eventMounted instanceof Horse && eventMounter instanceof Player){
			
			Player player = (Player) event.getEntity();
			Horse horseMount = (Horse) event.getMount();
			
			if(MagicalItemsUtil.doesContainMagicItem(Material.SADDLE, player)) {
				horseMount.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 864000, 1), true);
			}
		}
	}
}
