package jp.archesporeadventure.main.listeners.entity;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import jp.archesporeadventure.main.utils.MagicalItemsUtil;

public class EntityRegainHealthListener implements Listener {

	@EventHandler
	public void regainHealthEvent(EntityRegainHealthEvent event){
		
		Entity eventEntity = event.getEntity();
		
		if (eventEntity instanceof Player){
			Player player = (Player) eventEntity;
			
			//Soothing Dust, increases natural health regen amount to 3/4 of a heart.
			if (event.getRegainReason() == RegainReason.SATIATED && MagicalItemsUtil.doesContainMagicItem(Material.GLOWSTONE_DUST, player)) {
				event.setAmount(1.5);
			}
		}
	}
}
