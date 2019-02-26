package jp.archesporeadventure.main.generation.generators.boss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

import jp.archesporeadventure.main.ArchesporeAdventureMain;

public class BossGenerator {

	private Map<LivingEntity, BossEntity> aliveBossMobs = new HashMap<>();
	
	public void spawnBoss(Location location, EntityType entity) {
		if (entity.isAlive()) {
			LivingEntity bossEntity = (LivingEntity)location.getWorld().spawnEntity(location, entity);
			aliveBossMobs.put(bossEntity, new BossEntity(bossEntity, Bukkit.createBossBar("Boss", BarColor.RED, BarStyle.SEGMENTED_10)));
			bossEntity.setMetadata("BOSS", new FixedMetadataValue(ArchesporeAdventureMain.getPlugin(), true));
		}
	}
	
	/**
	 * Gets the boss info for the specified entity
	 * @param entity the entity to get boss info for.
	 * @return the BossEntity, or null if the entity is not a boss.
	 */
	public BossEntity getBoss(LivingEntity entity) {
		return aliveBossMobs.get(entity);
	}
	
	/**
	 * Removes a boss from the generator, kills the entity if it exists as a boss.
	 * @param entity the boss to remove.
	 * @param kill should we kill the entity when we remove it?
	 */
	public void removeBoss(LivingEntity entity, boolean kill) {
		if (aliveBossMobs.containsKey(entity)) {
			aliveBossMobs.get(entity).getBossBar().removeAll();
			aliveBossMobs.remove(entity);
			if (kill) { entity.remove(); }
		}
	}
	
	/**
	 * Checks if an entity is registered as a boss
	 * @param entity the entity to check
	 * @return true/false
	 */
	public boolean isBoss(LivingEntity entity) {
		return aliveBossMobs.containsKey(entity);
	}
	
	public void bossTick(int tickCount) {
		if (tickCount % 5 == 0) {
			for (BossEntity boss : aliveBossMobs.values()) {
				boss.updateBarViewers();
				Location bossLocation = boss.getEntity().getLocation();
				for (int loopValue = 0; loopValue < 128; loopValue++) {
					double angle = Math.toRadians((360.0/128.0) * loopValue);
					
					bossLocation.getWorld().spawnParticle(Particle.SMOKE_NORMAL, bossLocation.clone().add(Math.cos(angle) * 16, .1, Math.sin(angle) * 16), 2, 0, 0, 0, 0);
				}
			}
		}
	}
}
