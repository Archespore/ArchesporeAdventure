package jp.archesporeadventure.main.controllers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import jp.archesporeadventure.main.utils.LivingEntityUtil;
import jp.archesporeadventure.main.utils.ParticleUtil;
import jp.archesporeadventure.main.utils.PotionEffectUtil;

public class GroundPoundController {
	
	//Ground Pound effect radius
	private final static int GROUND_POUND_EFFECT_RADIUS = 6;

	private static List<Player> groundPoundList = new ArrayList<>();
	
	/**
	 * Adds a chaos storm in the world at the specified location for the specified duration.
	 * @param location the center of the storm.
	 * @param duration amount of times the effect should go 
	 */
	public static void addGroundPound(Player player) {
		groundPoundList.add(player);
	}
	
	/**
	 * Checks to see if the specified player has a ground pound.
	 * @param player player to check for.
	 * @return true or false.
	 */
	public static boolean doesHaveGroundPond(Player player) {
		return groundPoundList.contains(player);
	}
	
	/**
	 * Removes the chaos storm effect with the center at the specified location.
	 * @param location the center of the storm to remove.
	 */
	public static void removeGroundPound(Player player) {
		groundPoundList.remove(player);
	}

	/**
	 * Creates a ground pound effect at the specified player then removes them from the list.
	 * @param player player to create a ground pound around.
	 */
	public static void createGroundPoundEffect(Player player) {
		
		Location playerLocation = player.getLocation();
		
		for (int loopValue = 0; loopValue < 64; loopValue++) {
			double angle = Math.toRadians((360.0 / 64.0) * loopValue);
			ParticleUtil.spawnWorldParticles(Particle.BLOCK_CRACK, playerLocation.clone().add(Math.cos(angle) * GROUND_POUND_EFFECT_RADIUS, .25, Math.sin(angle) * GROUND_POUND_EFFECT_RADIUS), 3, 0, 0, 0, .25, Material.DIRT.createBlockData());
		}
		ParticleUtil.spawnWorldParticles(Particle.BLOCK_CRACK, playerLocation.clone().add(0, .25, 0), 50, 2.5, 0, 2.5, .25, Material.DIRT.createBlockData());
		playerLocation.getWorld().playSound(playerLocation, Sound.BLOCK_ANVIL_LAND, 3.5f, .6f);
		playerLocation.getWorld().playSound(playerLocation, Sound.BLOCK_GRASS_BREAK, 3.5f, .9f);
		
		for (Entity entity : player.getNearbyEntities(GROUND_POUND_EFFECT_RADIUS, GROUND_POUND_EFFECT_RADIUS, GROUND_POUND_EFFECT_RADIUS)) {
			if (entity.getLocation().distance(playerLocation) <= GROUND_POUND_EFFECT_RADIUS) {
				
				entity.setVelocity(entity.getVelocity().add(new Vector(0, 1.25, 0)));
				if (entity instanceof LivingEntity) {
					
					LivingEntity entityLiving = (LivingEntity) entity;
					LivingEntityUtil.removeHealth(entityLiving, 8);
					entityLiving.addPotionEffect(PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 0), entityLiving), true);
				}
			}
		}
		
		removeGroundPound(player);
	}
}
