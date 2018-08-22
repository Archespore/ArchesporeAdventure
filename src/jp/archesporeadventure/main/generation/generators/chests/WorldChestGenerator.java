package jp.archesporeadventure.main.generation.generators.chests;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.World;

import jp.archesporeadventure.main.generation.chests.GeneratedChest;
import jp.archesporeadventure.main.generation.itempools.LootPool;

public class WorldChestGenerator extends ChestGenerator {
	
/*	*//**
	 * Default constructor.
	 *//*
	public OpenWorldChestGenerator() {
		
	}
	
	*//**
	 * Creates an OpenWorldChestGenerator with the specified itemPools
	 * @param itemPools itemPools to add to this generator.
	 *//*
	public OpenWorldChestGenerator(ItemPool... itemPools) {
		addItemPools(itemPools);
	}
	
	*//**
	 * Adds the specified ItemPools to the generator if the ItemPoolType doesn't already exist in the generator.
	 * @param itemPools ItemPools to add to the generator.
	 *//*
	public void addItemPools(ItemPool... itemPools) {
		
		for (ItemPool pool : itemPools) {
			if (!generatorItemPools.containsKey(pool.getPoolName())) { generatorItemPools.put(pool.getPoolName(), pool); }
		}
	}
	
	*//**
	 * Replaces, or adds, the specified ItemPools to the generator.
	 * @param itemPools ItemPools to add to the generator.
	 *//*
	public void replaceItemPools(ItemPool... itemPools) {
		
		for (ItemPool pool : itemPools) { generatorItemPools.put(pool.getPoolName(), pool); }
	}
	
	*//**
	 * Check if this generator has a ItemPool for the specified name.
	 * @param poolName the ItemPool name to check for.
	 * @return true/false if the name was found.
	 *//*
	public boolean doesContainItemPool(String poolName) {
		return generatorItemPools.containsKey(poolName);
	}
	
	*//**
	 * Check if this generator has a ItemPool for the specified ItemPool.
	 * @param pool the ItemPool to check for.
	 * @return true/false if the ItemPool was found.
	 *//*
	public boolean doesContainItemPool(ItemPool pool) {
		return generatorItemPools.containsKey(pool.getPoolName());
	}*/
	
	public void generateChests(LootPool lootPool, World world, int amount) {
		for (int loopValue = 0; loopValue < amount; loopValue++) {
			
			int xPos = ThreadLocalRandom.current().nextInt(-1010, 1110 + 1);
			int zPos = ThreadLocalRandom.current().nextInt(-1170, 1130 + 1);
			int yPos = world.getHighestBlockYAt(xPos, zPos);
			
			Location blockLocation = new Location(world, xPos, yPos, zPos);
			GeneratedChest newChest = new GeneratedChest(blockLocation, lootPool, false);
			generatedChests.put(blockLocation, newChest);
			if (generatedChests.containsKey(blockLocation)) { 
				System.out.println("Location generated!: " + blockLocation.toString()); 
				System.out.println("Item Pool!: " + lootPool.getPoolName());
			}
		}
	}
}
