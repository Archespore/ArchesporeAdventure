package jp.archesporeadventure.main.controllers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import jp.archesporeadventure.main.utils.ParticleUtil;

public class ArtilleryBarrageController {

	//How many ticks per effect of artillery.
	private final static int ARTILLERY_EFFECT_TICKS_CYCLE = 5;
	
	//artillery effect radius
	private final static int ARTILLERY_EFFECT_RADIUS = 3;

	private static Map<Location, Integer> artilleryBarrageMap = new HashMap<>();
	
	/**
	 * Adds a chaos storm in the world at the specified location for the specified duration.
	 * @param location the center of the storm.
	 * @param duration amount of times the effect should go 
	 */
	public static void addArtilleryEffect(Location location, int duration) {
		artilleryBarrageMap.put(location, duration);
	}
	
	/**
	 * Removes the chaos storm effect with the center at the specified location.
	 * @param location the center of the storm to remove.
	 */
	public static void removeArtilleryEffect(Location location) {
		artilleryBarrageMap.remove(location);
	}
	
	/**
	 * Runs the chaos storm effects, creates particles and strikes lightning.
	 * @param tickCount Used to determine if effects should run.
	 */
	public static void artilleryEffect(int tickCount) {
		
		if (tickCount % ARTILLERY_EFFECT_TICKS_CYCLE == 0) {
			
			artilleryBarrageMap.forEach( (location, duration) -> {

				for (int loopValue = 0; loopValue < 48; loopValue++) {
					double particleAngle = Math.toRadians((360.0 / 48.0) * loopValue);
					ParticleUtil.spawnWorldParticles(Particle.REDSTONE, location.clone().add(Math.cos(particleAngle) * ARTILLERY_EFFECT_RADIUS, .25, Math.sin(particleAngle) * ARTILLERY_EFFECT_RADIUS), 1, 0, 0, 0, 0, new DustOptions(Color.GRAY, 1.5f));
				}
				
				if (duration <= 0) {
					ParticleUtil.spawnWorldParticles(Particle.EXPLOSION_HUGE, location, 1);
					location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 3.5f, 1.0f);
					for (Entity entity : location.getWorld().getNearbyEntities(location, ARTILLERY_EFFECT_RADIUS, ARTILLERY_EFFECT_RADIUS, ARTILLERY_EFFECT_RADIUS)) {
						if (entity instanceof LivingEntity && entity.getLocation().distance(location) <= 3) {
							((LivingEntity) entity).damage(15);
						}
					}
				}
				
				artilleryBarrageMap.put(location, duration - 1);
			});
			
			artilleryBarrageMap.entrySet().removeIf( entry -> entry.getValue() < 0);
		}
	}
}
