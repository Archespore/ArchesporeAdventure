package jp.archesporeadventure.main.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.netty.util.internal.ThreadLocalRandom;
import jp.archesporeadventure.main.enchantments.CustomEnchantment;
import net.md_5.bungee.api.ChatColor;

public class MagicalItemsUtil {
	
	//How many ticks per cycle of Nature's Blessing.
	//TODO: Move nature's blessing to a seperate controller.
	private final static int TWENTY_TICKS_CYCLE = 20;
	private final static int FIFTY_TICKS_CYCLE = 50;
	private final static int HUNDRED_TICKS_CYCLE = 100;
	private final static int TWO_HUNDRED_TICKS_CYCLE = 200;
	
	//List of items for Enchanter's Stone
	private static List<Material> enchanterItems = new ArrayList<>(Arrays.asList(Material.LAPIS_LAZULI, Material.BOOK, Material.EXPERIENCE_BOTTLE));
	private static Map<Location, Integer> magicalSaplingLocations = new HashMap<>();
	
	public static void addSapling(Location saplingLocation, int saplingLength) {
		magicalSaplingLocations.put(saplingLocation, saplingLength);
	}
	
	public static void removeSapling(Location saplingLocation) {
		magicalSaplingLocations.remove(saplingLocation);
	}
	
	public static int getSapling(Location saplingLocation) {
		return magicalSaplingLocations.get(saplingLocation);
	}
	
	/**
	 * Checks a player's inventory to see if they contain at least one magical enchanted item.
	 * @param magicItem the material of the magical item to check for.
	 * @param player the player whose inventory to check.
	 * @return true of false on whether they contain the magic item.
	 */
	public static boolean doesContainMagicItem(Material magicItem, Player player) {
		
		for (ItemStack magicalItem : player.getInventory().all(magicItem).values()){
			if (magicalItem.getEnchantments().containsKey(CustomEnchantment.MAGICAL.getEnchant())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks a player's inventory to see if they contain at least one magical enchanted item.
	 * @param magicItem the material of the magical item to check for.
	 * @param player the player whose inventory to check.
	 * @param checkEnable checks if the magical item is enabled.
	 * @return true of false on whether they contain the magic item.
	 */
	public static boolean doesContainMagicItem(Material magicItem, Player player, boolean checkEnable) {
		
		for (ItemStack magicalItem : player.getInventory().all(magicItem).values()){
			if (magicalItem.getEnchantments().containsKey(CustomEnchantment.MAGICAL.getEnchant())){
				if (checkEnable == false || ChatColor.stripColor(magicalItem.getItemMeta().getLore().get(0)).equals("Enabled")) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns the amount of magical items in a player's inventory that match the specified material.
	 * @param magicItem the material of the magical item to check for.
	 * @param player the player whose inventory to check.
	 * @return the amount of magic items found in the inventory.
	 */
	public static int containsMagicItemAmount(Material magicItem, Player player) {
		
		int magicalItemCount = 0;
		
		for (ItemStack magicalItem : player.getInventory().all(magicItem).values()){
			if (magicalItem.getEnchantments().containsKey(CustomEnchantment.MAGICAL.getEnchant())){
				magicalItemCount++;
			}
		}
		return magicalItemCount;
	}
	
	/**
	 * Returns the amount of magical items in a player's inventory that match the specified material and are enabled.
	 * @param magicItem the material of the magical item to check for.
	 * @param player the player whose inventory to check.
	 * @param checkEnable checks if the magical item is enabled.
	 * @return the amount of magic items found in the inventory.
	 */
	public static int containsMagicItemAmount(Material magicItem, Player player, boolean checkEnable) {
		
		int magicalItemCount = 0;
		
		for (ItemStack magicalItem : player.getInventory().all(magicItem).values()){
			if (magicalItem.getEnchantments().containsKey(CustomEnchantment.MAGICAL.getEnchant())){
				if (checkEnable == false || ChatColor.stripColor(magicalItem.getItemMeta().getLore().get(0)).equals("Enabled")) {
					magicalItemCount++;
				}
			}
		}
		return magicalItemCount;
	}
	
	/**
	 * Runs the effects for all magical items.
	 * @param tickCount Used to determine which effects to run
	 */
	public static void magicalItemEffects(int tickCount){
		
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			
			//Gem of hope, heals the player in exchange for stored xp.
			if (doesContainMagicItem(Material.DIAMOND, player, true) && player.getHealth() < player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
				if (player.getExp() > 0 || player.getLevel() > 0) {
					player.giveExp(-2);
					LivingEntityUtil.addHealth(player, .05);
					player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .1f, (float)(1.25 + ThreadLocalRandom.current().nextDouble(.4) - .2));
				}
			}
			
			//Particle effects, creates a swirl of particles around the player.
			int particleAnimation = (tickCount % 50);
			double particleAngle = Math.toRadians((360.0/50.0) * particleAnimation);
			
			if (doesContainMagicItem(Material.REDSTONE, player, true)) {
				player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(Math.cos(particleAngle), (particleAnimation/50.0) * 2.0, Math.sin(particleAngle)), 2, 0, 0, 0, 0, new Particle.DustOptions(Color.RED, .75f));
				player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().subtract(Math.cos(particleAngle), -(particleAnimation/50.0) * 2.0, Math.sin(particleAngle)), 2, 0, 0, 0, 0, new Particle.DustOptions(Color.RED, .75f));
			}
			
			if (tickCount % TWENTY_TICKS_CYCLE == 0) {
				
				//All-Seeing Eye, grants night vision and glows nearby entities.
				if (doesContainMagicItem(Material.ENDER_EYE, player, true)) {
					
					PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 300, 0), player);
					player.addPotionEffect(newPotionEffect, true);

					for (Entity entity : player.getNearbyEntities(16, 16, 16)){
						if (entity instanceof LivingEntity && entity.getLocation().distance(player.getLocation()) <= 16) {
							LivingEntity livingEntity = (LivingEntity) entity;
							newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.GLOWING, 30, 0), livingEntity);
							livingEntity.addPotionEffect(newPotionEffect, true);
						}
					}
				}
				
				//Nature's Blessing, creates a buffing field.
				ArrayList<Location> locationsToRemove = new ArrayList<>();
				for (Location saplingLocation : magicalSaplingLocations.keySet()) {
					Collection<Entity> nearbyEntites = saplingLocation.getWorld().getNearbyEntities(saplingLocation, 8, 8, 8);
					for (Entity entity : nearbyEntites){
						if (entity instanceof Player){
							Player loopPlayer = (Player) entity;
							if (loopPlayer.getLocation().distance(saplingLocation) <= 8){
								loopPlayer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 0));
								loopPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 0));
								loopPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 0));
								loopPlayer.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 60, 0));
							}
						}
					}
					for (int loopValue = 0; loopValue < 64; loopValue++) {
						double angle = (360.0/64.0) * loopValue;
						Location particleLocation = saplingLocation.clone().add(Math.cos(Math.toRadians(angle)) * 8, .5, Math.sin(Math.toRadians(angle)) * 8);
						ParticleUtil.spawnWorldParticles(Particle.VILLAGER_HAPPY, particleLocation, 1, 0, 0, 0, 0);
					}
					
					int ticksLeft = getSapling(saplingLocation);
					addSapling(saplingLocation, ticksLeft - 1);
					if (ticksLeft - 1 <= 0) {
						locationsToRemove.add(saplingLocation);
					}
				}
				if (locationsToRemove.size() != 0){
					for (Location saplingLocation : locationsToRemove){
						removeSapling(saplingLocation);
						saplingLocation.getBlock().setType(Material.AIR);
					}
				}
			}
			
			if (tickCount % FIFTY_TICKS_CYCLE == 0) {
				
				//Lively Sunflower, Eye of Night, Miner's Blessing, and Soul of the Ocean effects. Heal the player every 2.5 seconds if certain conditions are met.
				if (player.getHealth() < player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
					
					//Lively Sunflower, heals if it's day and direct access to sun.
					if (MagicalItemsUtil.doesContainMagicItem(Material.SUNFLOWER, player)) {
						if ( (player.getLocation().getBlock().getLightFromSky() == 15) && (player.getWorld().getTime() >= 0 && player.getWorld().getTime() <= 12000) ){
							player.playSound(player.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, .75f, 1.25f);
							LivingEntityUtil.addHealth(player, 1);
						}
					}
					
					//Eye of Night, heals if it's night and direct access to moon.
					if (MagicalItemsUtil.doesContainMagicItem(Material.FERMENTED_SPIDER_EYE, player)) {
						if ( (player.getLocation().getBlock().getLightFromSky() == 15) && (player.getWorld().getTime() >= 12000 && player.getWorld().getTime() <= 24000) ){
							player.playSound(player.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, .75f, 1.25f);
							LivingEntityUtil.addHealth(player, 1);
						}
					}
					
					//Soul of the Ocean, heals if player is in water.
					if (MagicalItemsUtil.doesContainMagicItem(Material.PRISMARINE_SHARD, player)) {
						if (player.getLocation().getBlock().getType().equals(Material.WATER)){
							player.playSound(player.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, .75f, 1.25f);
							player.setHealth(Math.min(player.getHealth() + 1, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
						}
					}
					
					//Miner's Blessing, heals if there is no skylight.
					if (MagicalItemsUtil.doesContainMagicItem(Material.CLAY_BALL, player)) {
						if (player.getLocation().getBlock().getLightFromSky() == 0){
							player.playSound(player.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, .75f, 1.25f);
							player.setHealth(Math.min(player.getHealth() + 1, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
						}
					}
				}
				
				//Wolf's Bone, gives friendly nearby wolves speed.
				if (MagicalItemsUtil.doesContainMagicItem(Material.BONE, player)) {
					
					List<Entity> nearbyEntities = player.getNearbyEntities(16, 16, 16);
					for (Entity entity : nearbyEntities){
						if (entity instanceof Wolf) {
							Wolf wolf = (Wolf) entity;
							if (wolf.getLocation().distance(player.getLocation()) <= 16 && wolf.isTamed() && wolf.getOwner().equals(player)){
								wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 0), true);
							}
						}
					}
				}
				
				//Hellish Aura, damages nearby entities every 2.5 seconds.
				if (doesContainMagicItem(Material.NETHER_BRICK, player, true)) {
					for (Entity entity : player.getNearbyEntities(4, 4, 4)){
						if (entity instanceof LivingEntity && entity.getLocation().distance(player.getLocation()) <= 4) {
							((LivingEntity) entity).damage(1);
						}
					}
				}
			}
			
			if (tickCount % HUNDRED_TICKS_CYCLE == 0) {
				
				//Photosynthetic Seeds, slowly restores hunger and saturation
				int effectPhotosyntheticSeeds = 0;
				for(int loopValue = 0; loopValue < MagicalItemsUtil.containsMagicItemAmount(Material.WHEAT_SEEDS, player); loopValue++){
					effectPhotosyntheticSeeds++;
					if (player.getFoodLevel() < 20) { player.setFoodLevel(Math.min(player.getFoodLevel() + 1, 20)); }
					else if (player.getSaturation() < 20) { player.setSaturation(Math.min(player.getSaturation() + .5f, 20)); }
					else { effectPhotosyntheticSeeds -= 1; }
				}
				//If the effect activated at least once.
				if (effectPhotosyntheticSeeds != 0) { player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, .75f, 1.0f); }
				
				//Creeper Head, explodes the nearest entity.
				if (player.getEquipment().getHelmet() != null){
					
					ItemStack playerHelmet = player.getEquipment().getHelmet();
					
					if (playerHelmet.getType() == Material.CREEPER_HEAD && playerHelmet.getEnchantments().containsKey(CustomEnchantment.MAGICAL.getEnchant())){
						
						double closestDistance = 12.0;
						LivingEntity closestEntity = null;
						List<Entity> nearbyEntities = player.getNearbyEntities(12, 12, 12);
						
						for (Entity entity : nearbyEntities) {
							if (entity instanceof LivingEntity && entity.getLocation().distance(player.getLocation()) <= closestDistance){
								closestDistance = entity.getLocation().distance(player.getLocation());
								closestEntity = (LivingEntity) entity;
							}
						}
						if (closestEntity != null){
							closestEntity.getWorld().createExplosion(closestEntity.getLocation().getX(), closestEntity.getLocation().add(0, 1, 0).getY(), closestEntity.getLocation().getZ(), 1.25f, false, false);
							closestEntity.damage(0, player);
						}
					}
				}
			}
				
			if (tickCount % TWO_HUNDRED_TICKS_CYCLE == 0) {
				
				//Portable Farm, generates wheat and bread over time
				boolean effectPortableFarm = false;
				for(int loopValue = 0; loopValue < MagicalItemsUtil.containsMagicItemAmount(Material.WHEAT, player); loopValue++){
					if (player.getInventory().firstEmpty() != -1 && ThreadLocalRandom.current().nextDouble(100) < 50.0) {
						effectPortableFarm = true;
						if (ThreadLocalRandom.current().nextDouble(100) < 25.0) { player.getInventory().addItem(new ItemStack(Material.BREAD, 1)); }
						else { player.getInventory().addItem(new ItemStack(Material.WHEAT, 1)); }
					}
				}
				//If the effect activated at least once.
				if (effectPortableFarm) { player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, .75f, 1.0f); }
				
				//Iron Generator, slowly generates iron ingots
				boolean effectIronGenerator = false;
				for(int loopValue = 0; loopValue < MagicalItemsUtil.containsMagicItemAmount(Material.IRON_BLOCK, player); loopValue++){
					if (player.getInventory().firstEmpty() != -1 && ThreadLocalRandom.current().nextDouble(100) < 20.0) { 
						player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 1)); 
						effectIronGenerator = true;
					}
				}
				//If the effect activated at least once.
				if (effectIronGenerator) { player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, .75f, 1.25f); }
				
				//Transmutation Table, iron to gold, and gold to emeralds.
				boolean effectTransmutationTable = false;
				for (int loopValue = 0; loopValue < containsMagicItemAmount(Material.END_PORTAL_FRAME, player, true); loopValue++) {
					
					Inventory playerInventory = player.getInventory();
					if (playerInventory.contains(Material.GOLD_INGOT) && ThreadLocalRandom.current().nextDouble(100) < 10.0 && playerInventory.firstEmpty() != -1) {
						playerInventory.removeItem(new ItemStack(Material.GOLD_INGOT, 1));
						playerInventory.addItem(new ItemStack(Material.EMERALD, 1));
						effectTransmutationTable= true;
					}
					if (playerInventory.contains(Material.IRON_INGOT) && ThreadLocalRandom.current().nextDouble(100) < 25.0 && playerInventory.firstEmpty() != -1) {
						playerInventory.removeItem(new ItemStack(Material.IRON_INGOT, 1));
						playerInventory.addItem(new ItemStack(Material.GOLD_INGOT, 1));
						effectTransmutationTable= true;
					}
				}
				//If the effect activated at least once.
				if (effectTransmutationTable) { player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, .5f, 1.5f); }
				
				//Enchanter's Stone, generates xp and lapis
				boolean effectEnchantersStone = false;
				for(int loopValue = 0; loopValue < MagicalItemsUtil.containsMagicItemAmount(Material.LAPIS_BLOCK, player); loopValue++){
					if (player.getInventory().firstEmpty() != -1 && ThreadLocalRandom.current().nextDouble(100) < 100.0 / 3.0) { 
						player.getInventory().addItem(new ItemStack(enchanterItems.get(ThreadLocalRandom.current().nextInt(enchanterItems.size())), ThreadLocalRandom.current().nextInt(2) + 1)); 
						effectEnchantersStone = true;
					}
				}
				//If the effect activated at least once.
				if (effectEnchantersStone) { player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, .75f, 1.25f); }
			}
		}
	}
	
}
