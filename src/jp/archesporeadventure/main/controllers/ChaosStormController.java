package jp.archesporeadventure.main.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.utils.ParticleUtil;
import jp.archesporeadventure.main.utils.PotionEffectUtil;

public class ChaosStormController {
	
	//How many ticks per effect of chaos storm.
	private final static int CHAOS_STORM_EFFECT_TICKS_CYCLE = 50;
	//How many ticks per particle refresh.
	private final static int CHAOS_STORM_PARTICLE_TICKS_CYCLE = 20;
	
	//Chaos storm effect radius
	private final static int CHAOS_STORM_EFFECT_RADIUS = 8;

	private static Map<Location, Integer> chaosStormMap = new HashMap<>();
	
	/**
	 * Adds a chaos storm in the world at the specified location for the specified duration.
	 * @param location the center of the storm.
	 * @param duration amount of times the effect should go 
	 */
	public static void addStormEffect(Location location, int duration) {
		chaosStormMap.put(location, duration);
	}
	
	/**
	 * Removes the chaos storm effect with the center at the specified location.
	 * @param location the center of the storm to remove.
	 */
	public static void removeStormEffect(Location location) {
		chaosStormMap.remove(location);
	}
	
	/**
	 * Runs the chaos storm effects, creates particles and strikes lightning.
	 * @param tickCount Used to determine if effects should run.
	 */
	public static void stormEffect(int tickCount) {
		
		if (tickCount % CHAOS_STORM_PARTICLE_TICKS_CYCLE == 0) {
			
			chaosStormMap.forEach( (location, duration) -> {
				for (int loopValue = 0; loopValue < 50; loopValue++) {
					double particleAngle = Math.toRadians((360.0 / 50.0) * loopValue);
					ParticleUtil.spawnWorldParticles(Particle.VILLAGER_ANGRY, location.clone().add(Math.cos(particleAngle) * CHAOS_STORM_EFFECT_RADIUS, 0, Math.sin(particleAngle) * CHAOS_STORM_EFFECT_RADIUS), 1, .1, 0, .1);
				}
				for (Entity entity : location.getWorld().getNearbyEntities(location, CHAOS_STORM_EFFECT_RADIUS, CHAOS_STORM_EFFECT_RADIUS, CHAOS_STORM_EFFECT_RADIUS)) {
					if (entity instanceof LivingEntity && location.distance(entity.getLocation()) <= CHAOS_STORM_EFFECT_RADIUS) {
						
						LivingEntity entityLiving = (LivingEntity) entity;
						PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 1), entityLiving);
						entityLiving.addPotionEffect(newPotionEffect, true);
					}
				}
			});
		}
		
		if (tickCount % CHAOS_STORM_EFFECT_TICKS_CYCLE == 0) {
			
			chaosStormMap.forEach( (location, duration) -> {
				double offsetAngle = Math.toRadians(ThreadLocalRandom.current().nextDouble(360));
				double offsetLength = ThreadLocalRandom.current().nextDouble(CHAOS_STORM_EFFECT_RADIUS);
				
				Location stormEffect = location.clone().add(Math.cos(offsetAngle) * offsetLength, 0, Math.sin(offsetAngle) * offsetLength);
				World stormWorld = stormEffect.getWorld();
				
				stormWorld.spigot().strikeLightning(stormEffect, true);
				stormWorld.createExplosion(stormEffect.getX(), stormEffect.getY(), stormEffect.getZ(), 1.5f, false, false);
				
				chaosStormMap.put(location, duration - 1);
			});
			
			chaosStormMap.entrySet().removeIf( entry -> entry.getValue() <= 0);
		}
	}
}
