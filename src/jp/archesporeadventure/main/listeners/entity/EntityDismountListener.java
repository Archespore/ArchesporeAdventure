package jp.archesporeadventure.main.listeners.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityDismountEvent;

public class EntityDismountListener implements Listener{

	@EventHandler
	public void entityDismountEvent(EntityDismountEvent event) {
		
		Entity eventMounted = event.getDismounted();
		Entity eventMounter = event.getEntity();
		
		if (eventMounted instanceof Horse && eventMounter instanceof Player) {
			
			Horse horseMount = (Horse) event.getDismounted();
			
			if (horseMount.hasPotionEffect(PotionEffectType.SPEED)){
				if (horseMount.getPotionEffect(PotionEffectType.SPEED).getDuration() > 12000){
					horseMount.removePotionEffect(PotionEffectType.SPEED);
				}
			}
		}
	}
}
