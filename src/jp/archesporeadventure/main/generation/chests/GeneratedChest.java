package jp.archesporeadventure.main.generation.chests;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;

import jp.archesporeadventure.main.generation.generators.chests.ChestGenerator;
import jp.archesporeadventure.main.generation.itempools.LootPool;

public class GeneratedChest {

	Material blockReplaced;
	BlockData blockReplacedData;
	
	LootPool itemPool;
	Chest generatedChest;
	ChestGenerator registeredGenerator;
	
	boolean lootGenerated;
	boolean chestRemoved = false;
	
	public GeneratedChest(Location location, LootPool pool) {
		this(location, pool, false);
	}

	public GeneratedChest(Location location, LootPool pool, boolean generateLoot) {
		Block locationBlock = location.getBlock();
		blockReplaced = locationBlock.getType();
		blockReplacedData = locationBlock.getBlockData();
		itemPool = pool;
		locationBlock.setType(Material.CHEST);
		generatedChest = (Chest) locationBlock.getState();
		generatedChest.setCustomName(itemPool.getPoolDisplayName());
		generatedChest.update(true);
		lootGenerated = generateLoot;
		if (generateLoot) {
			generatedChest.getInventory().addItem(itemPool.generateItems(3));
		}
	}
	
	public boolean isChestRemoved() {
		return chestRemoved;
	}
	
	public void generateLoot(int amount) {
		if (!lootGenerated) {
			generatedChest.getSnapshotInventory().addItem(itemPool.generateItems(amount));
			generatedChest.update(true);
			lootGenerated = true;
		}
	}
	
	public void removeChest() {
		generatedChest.getSnapshotInventory().clear();
		generatedChest.update(true);
		generatedChest.setType(blockReplaced);
		generatedChest.setBlockData(blockReplacedData);
		generatedChest.update(true);
		chestRemoved = true;
	}
}