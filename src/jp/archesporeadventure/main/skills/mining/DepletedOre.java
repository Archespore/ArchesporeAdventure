package jp.archesporeadventure.main.skills.mining;

import org.bukkit.Location;
import org.bukkit.Material;

public class DepletedOre {

	private Location oreLocation;
	private Material oreMaterial;
	private int oreTimer;
	
	/**
	 * Constructor for a new depleted ore.
	 * @param location location of the depleted ore.
	 * @param oreType the material of this depleted ore
	 * @param refreshTimer the amount of seconds before the ore refreshes.
	 */
	public DepletedOre(Location location, Material oreType, int refreshTimer) {
		oreLocation = location;
		oreMaterial = oreType;
		oreTimer = refreshTimer;
	}
	
	/**
	 * Get the depleted ore location.
	 * @return The location of the depleted ore.
	 */
	public Location getOreLocation() {
		return oreLocation;
	}
	
	/**
	 * Get the depleted ore type.
	 * @return the material of the depleted ore.
	 */
	public Material getOreMaterial() {
		return oreMaterial;
	}
	
	/**
	 * Get the depleted ore timer.
	 * @return the amount of seconds left to refresh.
	 */
	public int getOreTimer() {
		return oreTimer;
	}
	
	/**
	 * Sets the location of this depleted ore.
	 * @param location location of the ore.
	 */
	public void setOreLocation(Location location) {
		oreLocation = location;
	}
	
	/**
	 * Sets the material of this depleted ore.
	 * @param oreType material of the ore.
	 */
	public void setOreMaterial(Material oreType) {
		oreMaterial = oreType;
	}
	
	/**
	 * Sets the refresh timer of this depleted ore.
	 * @param refreshTimer seconds to refresh.
	 */
	public void setOreTimer(int refreshTimer) {
		oreTimer = refreshTimer;
	}
}
