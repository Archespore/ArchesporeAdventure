package jp.archesporeadventure.main.menus.scoreboards;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import net.md_5.bungee.api.ChatColor;

public class ScoreboardMenuController {

	Map<Player, Scoreboard> scoreboardMap = new HashMap<>();
	
	public void createSidebarScoreboard(Player player) {
		Scoreboard newSidebarScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = newSidebarScoreboard.registerNewObjective("Player_Info", "dummy", "Player Info");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		Score blankLine = objective.getScore(ChatColor.MAGIC.toString() + "");
		blankLine.setScore(15);
		
		Score renownRecord = objective.getScore(ChatColor.BOLD + "» Player Renown");
		renownRecord.setScore(14);
		
		Score renownScore = objective.getScore(ChatColor.GRAY + "  " + ArchesporeAdventureMain.getPlayerSkillsController().getPlayerRenown(player) + " Renown");
		renownScore.setScore(13);
		
		player.setScoreboard(newSidebarScoreboard);
		scoreboardMap.put(player, newSidebarScoreboard);
	}
	
	public void updateScoreboard(Player player) {
		Scoreboard playerScoreboard = scoreboardMap.get(player);
		Objective objective = playerScoreboard.getObjective(DisplaySlot.SIDEBAR);
		
		for (String score : playerScoreboard.getEntries()) {
			if (score.matches(".*\\d+\\sRenown$")) {
				playerScoreboard.resetScores(score);
				Score renownScore = objective.getScore(ChatColor.GRAY + "  " + ArchesporeAdventureMain.getPlayerSkillsController().getPlayerRenown(player) + " Renown");
				renownScore.setScore(13);
			}
		}
			
		player.setScoreboard(scoreboardMap.get(player));
	}
}
