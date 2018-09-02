package jp.archesporeadventure.main.generation.generators.boss;

import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class BossEntity {

	private LivingEntity bossEntity;
	private BossBar entityBossBar;
	
	public BossEntity(LivingEntity boss, BossBar bossBar) {
		bossEntity = boss;
		entityBossBar = bossBar;
	}
	
	public Entity getEntity() {
		return bossEntity;
	}
	
	public BossBar getBossBar() {
		return entityBossBar;
	}
	
	public void updateBarProgress() {
		double bossMaxHealth = bossEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		double bossCurrentHealth = bossEntity.getHealth();
		
		entityBossBar.setProgress(bossCurrentHealth / bossMaxHealth);
	}
	
	public void updateBarViewers() {
		for (Player player : entityBossBar.getPlayers()) {
			if (player.getLocation().distance(bossEntity.getLocation()) >= 16) { entityBossBar.removePlayer(player); }
		}
		for (Entity entity : bossEntity.getNearbyEntities(16, 16, 16)) {
			if (entity instanceof Player && entity.getLocation().distance(bossEntity.getLocation()) <= 16) {
				Player player = (Player) entity;
				entityBossBar.addPlayer(player);
			}
		}
	}
}
