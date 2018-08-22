package jp.archesporeadventure.main.listeners.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.utils.MagicalItemsUtil;
import jp.archesporeadventure.main.utils.PotionEffectUtil;

public class PlayerTeleportListener implements Listener{

	@EventHandler
	public void playerEnderPearl(PlayerTeleportEvent event){
		
		Player player = event.getPlayer();
		
		if (event.getCause().equals(TeleportCause.ENDER_PEARL)){
			if (MagicalItemsUtil.doesContainMagicItem(Material.ENDER_PEARL, player)) {
				event.setCancelled(true);
				player.teleport(event.getTo());
				
				PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 0), player);
				player.addPotionEffect(newPotionEffect, true);
				
				newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 0), player);
				player.addPotionEffect(newPotionEffect, true);
			}
		}
		else if (event.getCause().equals(TeleportCause.SPECTATE) && !player.isOp()) {
			event.setCancelled(true);
		}
	}
}
