package jp.archesporeadventure.main.listeners.skills;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Statistic;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.abilities.SkillAbility.AbilityActivation;
import jp.archesporeadventure.main.skills.SkillController;
import jp.archesporeadventure.main.skills.SkillType;
import jp.archesporeadventure.main.skills.fishing.FishingSkillController;
import net.md_5.bungee.api.ChatColor;

public class PlayerFishListener implements Listener {

	@EventHandler
	public void playerFishEvent(PlayerFishEvent event) {
		
		Player player = event.getPlayer();
		SkillController skillController = ArchesporeAdventureMain.getSkillController(SkillType.FISHING);
		if (skillController instanceof FishingSkillController) {
			FishingSkillController fishingController = (FishingSkillController) skillController;
			if (event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
				ArchesporeAdventureMain.getPlayerSkillsController().addPlayerEXP(player, SkillType.FISHING, fishingController.getCatchXPReward(), true);
				ArchesporeAdventureMain.abilityEvent(AbilityActivation.PLAYER_FISH, event);
				if (ThreadLocalRandom.current().nextDouble(100) <= 25) {
					Drowned fishedMob = player.getWorld().spawn(event.getHook().getLocation().clone().add(0, 1.25, 0), Drowned.class);
					fishedMob.setVelocity(player.getEyeLocation().toVector().add(new Vector(0, .5, 0)).subtract(fishedMob.getLocation().toVector()).normalize().multiply(1.75));
					player.sendMessage(ChatColor.DARK_AQUA + "You feel something else tugging on the hook...");
				}
				
				if (player.getStatistic(Statistic.FISH_CAUGHT) <= 0) {
					player.sendMessage("You caught your first fish!");
				}
				
			}
		}
	}
}
