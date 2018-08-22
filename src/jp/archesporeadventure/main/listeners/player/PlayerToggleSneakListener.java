package jp.archesporeadventure.main.listeners.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.utils.MagicalItemsUtil;

public class PlayerToggleSneakListener implements Listener{

	@EventHandler
	public void playerSneakEvent(PlayerToggleSneakEvent event){
		
		Player player = event.getPlayer();
		
		if (!player.isSneaking()){
			if (MagicalItemsUtil.doesContainMagicItem(Material.RABBIT_FOOT, player)) {
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10, 2));
			}
		}
	}
}
