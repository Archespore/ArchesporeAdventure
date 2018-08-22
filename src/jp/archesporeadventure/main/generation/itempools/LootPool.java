package jp.archesporeadventure.main.generation.itempools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.inventory.ItemStack;

public class LootPool extends ItemPool {

	private List<ItemStack> lootPoolContents = new ArrayList<>();

	/**
	 * Creates a new LootPool with the specified name, and contains the specified items.
	 * @param name Internal name used to identify the pool.
	 * @param itemstack Items to add to the new pool.
	 */
	public LootPool(String name, ItemStack... itemStacks) {
		super(name);
		addItemsToPool(itemStacks);
	}
	
	/**
	 * Creates a new LootPool with the specified name and display name, and contains the specified items.
	 * @param name Internal name used to identify the pool.
	 * @param displayName The display name used by the pool.
	 * @param itemstack Items to add to the new pool.
	 */
	public LootPool(String name, String displayName, ItemStack... itemStacks) {
		super(name, displayName);
		addItemsToPool(itemStacks);
	}
	
	/**
	 * Adds the specified item/items to the pool.
	 * @param itemStacks Item(s) to add to the pool.
	 */
	public void addItemsToPool(ItemStack...itemStacks) {
		for (ItemStack item : itemStacks) {
			lootPoolContents.add(item.clone());
		}
	}
	
	/**
	 * Gets all ItemStacks in this item pool
	 * @return a list of ItemStacks in this pool.
	 */
	public List<ItemStack> getContents() {
		return lootPoolContents;
	}
	
	/**
	 * Generates random items from this object's pool.
	 * @param amount the amount of items to generate.
	 * @return Array of the random items.
	 */
	public ItemStack[] generateItems(int amount) {
		
		ItemStack[] randomItems = new ItemStack[amount];
		
		for (int loopValue = 0; loopValue < amount; loopValue++) {
			randomItems[loopValue] = lootPoolContents.get(ThreadLocalRandom.current().nextInt(lootPoolContents.size())).clone();
		}
		return randomItems;
	}
}
