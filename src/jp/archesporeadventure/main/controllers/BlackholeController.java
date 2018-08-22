package jp.archesporeadventure.main.controllers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.utils.ParticleUtil;
import jp.archesporeadventure.main.utils.PotionEffectUtil;

public class BlackholeController {
	
	//How many ticks per effect of blackhole.
	private final static int BLACKHOLE_EFFECT_TICKS_CYCLE = 2;
	
	//blackhole effect radius
	private final static int BLACKHOLE_EFFECT_RADIUS = 12;

	private static Map<Location, Integer> blackholeMap = new HashMap<>();
	
	/**
	 * Adds a chaos storm in the world at the specified location for the specified duration.
	 * @param location the center of the storm.
	 * @param duration amount of times the effect should go 
	 */
	public static void addBlackholeEffect(Location location, int duration) {
		blackholeMap.put(location, duration);
	}
	
	/**
	 * Removes the chaos storm effect with the center at the specified location.
	 * @param location the center of the storm to remove.
	 */
	public static void removeBlackholeEffect(Location location) {
		blackholeMap.remove(location);
	}
	
	/**
	 * Runs the chaos storm effects, creates particles and strikes lightning.
	 * @param tickCount Used to determine if effects should run.
	 */
	public static void blackholeEffect(int tickCount) {
		
		blackholeMap.forEach( (location, duration) -> {
			ParticleUtil.spawnWorldParticles(Particle.FALLING_DUST, location.clone().add(0, 1, 0), 3, .15, .15, .15, 0, Material.OBSIDIAN.createBlockData());
		});
		
		if (tickCount % BLACKHOLE_EFFECT_TICKS_CYCLE == 0) {
			
			blackholeMap.forEach( (location, duration) -> {

				for (int loopValue = 0; loopValue < 72; loopValue++) {
					double particleAngle = Math.toRadians((360.0 / 72.0) * loopValue);
					ParticleUtil.spawnWorldParticles(Particle.SMOKE_LARGE, location.clone().add(Math.cos(particleAngle) * BLACKHOLE_EFFECT_RADIUS, 0, Math.sin(particleAngle) * BLACKHOLE_EFFECT_RADIUS), 1, 0, 0, 0, .01);
				}
				for (Entity entity : location.getWorld().getNearbyEntities(location, BLACKHOLE_EFFECT_RADIUS, BLACKHOLE_EFFECT_RADIUS, BLACKHOLE_EFFECT_RADIUS)) {
					
					if (location.distance(entity.getLocation()) <= BLACKHOLE_EFFECT_RADIUS) {
						
						entity.setVelocity(entity.getVelocity().add(location.clone().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(.125)));
						if (entity instanceof LivingEntity) {
							
							LivingEntity entityLiving = (LivingEntity) entity;
							PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 10, 0), entityLiving);
							entityLiving.addPotionEffect(newPotionEffect, true);
						}
					}
				}
				blackholeMap.put(location, duration - 1);
			});
			
			blackholeMap.entrySet().removeIf( entry -> entry.getValue() <= 0);
		}
	}
}