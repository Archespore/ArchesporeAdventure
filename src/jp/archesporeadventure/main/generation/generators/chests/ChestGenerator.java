package jp.archesporeadventure.main.generation.generators.chests;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import jp.archesporeadventure.main.generation.chests.GeneratedChest;

public class ChestGenerator {

	protected Map<Location, GeneratedChest> generatedChests = new HashMap<>();
	
	/**
	 * Checks if there is a generated chest at the specified location
	 * @param location the location to check
	 * @return true/false if there is a chest registered at the specified location with this generator.
	 */
	public boolean doesChestExist(Location location) {
		return generatedChests.containsKey(location);
	}
	
	/**
	 * Gets the GeneratedChest at the specified location if it exists.
	 * @param location the location of the GeneratedChest
	 * @return the GeneratedChest found, or null if none was found.
	 */
	public GeneratedChest getChestAtLocation(Location location) {
		
		if (doesChestExist(location)) {
			return generatedChests.get(location);
		}
		return null;
	}
	
	/**
	 * Removes the specified chest from the generator.
	 */
	public void removeGeneratedChest(Location location) {
		
		getChestAtLocation(location).removeChest();
		generatedChests.remove(location);
	}
	
	/**
	 * Removes all registered chests to this generator.
	 */
	public void removeAllGeneratedChests() {
		
		for (GeneratedChest generatedChest : generatedChests.values()) {
			generatedChest.removeChest();
		}
		generatedChests.clear();
	}
}
