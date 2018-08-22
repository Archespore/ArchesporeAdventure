package jp.archesporeadventure.main.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.controllers.ArtilleryBarrageController;
import jp.archesporeadventure.main.controllers.BlackholeController;
import jp.archesporeadventure.main.controllers.ChaosStormController;
import jp.archesporeadventure.main.skills.SkillType;
import jp.archesporeadventure.main.skills.mining.MiningSkillController;
import jp.archesporeadventure.main.utils.EnchantmentUtil;
import jp.archesporeadventure.main.utils.LivingEntityUtil;
import jp.archesporeadventure.main.utils.MagicalItemsUtil;

public class TaskController {
	
	//How high our tickCount should go before resetting to 0.
	private final int MAX_TICK_COUNT = 200;

	private BukkitTask taskTimer;
	private int tickCount;
	
	public TaskController(boolean startTimer) {
		tickCount = 0;
		if (startTimer) {
			startTasks();
		}
	}
	
	/**
	 * Used to start the task timers.
	 */
	public void startTasks(){
		//If we start the tasks and a timer already exists, cancel it and create a new one.
		stopTasks();
		
		taskTimer = new BukkitRunnable() {
			
			public void run() {
				if (tickCount >= MAX_TICK_COUNT) {
					tickCount = 0;
				}
				EnchantmentUtil.passiveEnchantmentEffects(tickCount);
				MagicalItemsUtil.magicalItemEffects(tickCount);
				LivingEntityUtil.entityBleedEffect(tickCount);
				ChaosStormController.stormEffect(tickCount);
				BlackholeController.blackholeEffect(tickCount);
				ArtilleryBarrageController.artilleryEffect(tickCount);
				((MiningSkillController) ArchesporeAdventureMain.getSkillController(SkillType.MINING)).refreshOres(tickCount);
				tickCount++;
			}
		}.runTaskTimer(ArchesporeAdventureMain.getPlugin(), 0, 1);
		
		new BukkitRunnable() {
			
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.getLocation().getY() >= 128) { 
						player.stopSound(Sound.ITEM_ELYTRA_FLYING);
						player.playSound(player.getLocation(), Sound.ITEM_ELYTRA_FLYING, SoundCategory.AMBIENT, 100.0f, .5f); 
					}
				}
			}
		}.runTaskTimer(ArchesporeAdventureMain.getPlugin(), 0, 100);
	}
	
	/**
	 * Used to stop the task timers.
	 */
	public void stopTasks(){
		if (taskTimer != null) {
			Bukkit.getScheduler().cancelTask(taskTimer.getTaskId());
		}
	}
}
