package jp.archesporeadventure.main.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ParticleUtil {

	/**
	 * Spawns the specified particle at the specified location a specified
	 * number of times. The particle will be forced to all players, meaning
	 * the view distance is larger, and will be displayed to those with
	 * minimal particle settings.
	 * 
	 * @param particle particle the particle to spawn
	 * @param location location the location to spawn at
	 * @param count count the number of particles
	 */
	public static void spawnWorldParticles(Particle particle, Location location, int count) {
		spawnWorldParticles(particle, location, count, 0, 0, 0, 0, null);
	}
	
	/**
	 * Spawns the specified particle at the specified location a specified
	 * number of times. The particle will be forced to all players, meaning
	 * the view distance is larger, and will be displayed to those with
	 * minimal particle settings.
	 * 
	 * @param particle particle the particle to spawn
	 * @param location location the location to spawn at
	 * @param count count the number of particles
	 * @param offsetX the maximum random offset on the X axis
	 * @param offsetY the maximum random offset on the Y axis
	 * @param offsetZ the maximum random offset on the Z axis
	 */
	public static void spawnWorldParticles(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
		spawnWorldParticles(particle, location, count, offsetX, offsetY, offsetZ, 0, null);
	}
	
	/**
	 * Spawns the specified particle at the specified location a specified
	 * number of times. The particle will be forced to all players, meaning
	 * the view distance is larger, and will be displayed to those with
	 * minimal particle settings.
	 * 
	 * @param particle particle the particle to spawn
	 * @param location location the location to spawn at
	 * @param count count the number of particles
	 * @param offsetX the maximum random offset on the X axis
	 * @param offsetY the maximum random offset on the Y axis
	 * @param offsetZ the maximum random offset on the Z axis
	 * @param speed the extra data for this particle, depends on the
     *              particle used (normally speed)
	 */
	public static void spawnWorldParticles(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double speed) {
		spawnWorldParticles(particle, location, count, offsetX, offsetY, offsetZ, speed, null);
	}
	
	/**
	 * Spawns the specified particle at the specified location a specified
	 * number of times. The particle will be forced to all players, meaning
	 * the view distance is larger, and will be displayed to those with
	 * minimal particle settings.
	 * 
	 * @param particle particle the particle to spawn
	 * @param location location the location to spawn at
	 * @param count count the number of particles
	 * @param offsetX the maximum random offset on the X axis
	 * @param offsetY the maximum random offset on the Y axis
	 * @param offsetZ the maximum random offset on the Z axis
	 * @param speed the extra data for this particle, depends on the
     *              particle used (normally speed)
	 * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
	 */
	public static <T> void spawnWorldParticles(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double speed, T data) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed, data);
		}
	}
}
