package jp.archesporeadventure.main.generation.itempools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class EquipmentPool extends ItemPool {

	private final List<EnchantmentTarget> armorTypes = new ArrayList<>(Arrays.asList(EnchantmentTarget.ARMOR_FEET, EnchantmentTarget.ARMOR_LEGS, EnchantmentTarget.ARMOR_TORSO, EnchantmentTarget.ARMOR_HEAD));
	
	private List<ItemStack> equipmentPoolArmor = new ArrayList<>();
	private List<ItemStack> equipmentPoolWeapons = new ArrayList<>();
	
	/**
	 * Creates a new EquipmentPool with the specified name, and contains the specified items.
	 * @param name Internal name used to identify the pool.
	 * @param armorItems Items to add to the new armor pool.
	 * @param weaponItems Items to add to the new weapon pool.
	 */
	public EquipmentPool(String name, ItemStack[] armorItems, ItemStack[] weaponItems) {
		this(name, "", armorItems, weaponItems);
	}
	
	/**
	 * Creates a new EquipmentPool with the specified name and display name, and contains the specified items.
	 * @param name Internal name used to identify the pool.
	 * @param displayName The display name used by the pool.
	 * @param armorItems Items to add to the new armor pool.
	 * @param weaponItems Items to add to the new weapon pool.
	 */
	public EquipmentPool(String name, String displayName, ItemStack[] armorItems, ItemStack[] weaponItems) {
		super(name, displayName);
		addItemsToArmor(armorItems);
		addItemsToWeapon(weaponItems);
	}
	
	/**
	 * Adds the specified item/items to the armor pool.
	 * @param itemStacks Item(s) to add to the pool.
	 */
	public void addItemsToArmor(ItemStack... itemStacks) {
		for (ItemStack item : itemStacks) {
			if (EnchantmentTarget.WEARABLE.includes(item)) {
				equipmentPoolArmor.add(item.clone());
			}
		}
	}
	
	/**
	 * Adds the specified item/items to the weapon pool.
	 * @param itemStacks Item(s) to add to the pool.
	 */
	public void addItemsToWeapon(ItemStack... itemStacks) {
		for (ItemStack item : itemStacks) {
			equipmentPoolWeapons.add(item.clone());
		}
	}
	
	/**
	 * Gets all ItemStacks in this item armor pool
	 * @return a list of ItemStacks in this pool.
	 */
	public List<ItemStack> getArmorContents() {
		return equipmentPoolArmor;
	}
	
	/**
	 * Gets all ItemStacks of a specified type in this item armor pool
	 * @return a list of ItemStacks in this pool.
	 */
	public List<ItemStack> getArmorTypeContents(EnchantmentTarget target) {
		
		List<ItemStack> armorItems = new ArrayList<>();
		for (ItemStack item : equipmentPoolArmor) {
			if (target.includes(item)) { armorItems.add(item.clone()); }
		}
		return armorItems;
	}
	
	/**
	 * Gets all ItemStacks in this item weapon pool
	 * @return a list of ItemStacks in this pool.
	 */
	public List<ItemStack> getWeaponContents() {
		return equipmentPoolWeapons;
	}
	
	/**
	 * Generates a random armor set from this object's armor pool.
	 * @return Array of the random items.
	 */
	public ItemStack[] generateArmorSet() {
		return generateArmorSet(true, true, true, true);
	}
	
	/**
	 * Generates a random armor set from this object's armor pool with the specified pieces.
	 * @param armorItems a boolean for each piece of armor that determines if it should generate. Ex. 2 true generates 2 armor pieces and 2 air.
	 * @return Array of the random items.
	 */
	public ItemStack[] generateArmorSet(boolean... armorItems) {
		
		ItemStack[] randomItems = new ItemStack[4];
		for (int loopValue = 0; loopValue < 4; loopValue++) {
			List<ItemStack> loopTypeItems = getArmorTypeContents(armorTypes.get(loopValue));
			if (armorItems.length > loopValue && armorItems[loopValue] == true && loopTypeItems.size() > 0) { 
				randomItems[loopValue] = loopTypeItems.get(ThreadLocalRandom.current().nextInt(loopTypeItems.size()));
			}
			else {
				randomItems[loopValue] = new ItemStack(Material.AIR);
			}
		}
		return randomItems;
	}
	
	/**
	 * Generates a single random weapon from this object's weapon pool.
	 * @param bowItem Should the generated weapon be a bow or sword/axe?
	 * @return The randomly generated item.
	 */
	public ItemStack generateWeapon(boolean bowItem) {
		
		ItemStack randomItem = new ItemStack(Material.AIR);
		List<ItemStack> matchingWeapons = new ArrayList<>();
		for (int loopValue = 0; loopValue < equipmentPoolWeapons.size(); loopValue++) {
			
			ItemStack loopWeapon = equipmentPoolWeapons.get(loopValue);
			if (bowItem == false && !EnchantmentTarget.BOW.includes(loopWeapon)) { matchingWeapons.add(loopWeapon); }
			else if (bowItem == true && EnchantmentTarget.BOW.includes(loopWeapon)) { matchingWeapons.add(loopWeapon); }
		}
		
		if (matchingWeapons.size() > 0) { randomItem = matchingWeapons.get(ThreadLocalRandom.current().nextInt(matchingWeapons.size())).clone(); }
		return randomItem;
	}
}
