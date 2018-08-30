package jp.archesporeadventure.main.listeners.player;

import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import jp.archesporeadventure.main.ArchesporeAdventureMain;

public class PlayerJoinListener implements Listener {

	@EventHandler
	public void onJoinEvent(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		
		player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(24.0);
		player.setGameMode(GameMode.SURVIVAL);
		ArchesporeAdventureMain.getPlayerSkillsController().addPlayerToController(player);
		ArchesporeAdventureMain.getScoreboardController().createSidebarScoreboard(player);
	}
}
