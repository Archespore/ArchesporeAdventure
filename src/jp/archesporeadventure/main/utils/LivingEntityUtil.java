package jp.archesporeadventure.main.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.EntityEffect;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import jp.archesporeadventure.main.ArchesporeAdventureMain;

public class LivingEntityUtil {
	
	//How many ticks per cycle of bleed.
	//TODO: Move bleeding effects to bleeding controller
	private final static double BLEED_TICKS_CYCLE = 40;
	
	private static List<String> aliveEntities = new ArrayList<>(Arrays.asList("BAT", "BLAZE", "CAT", "CHICKEN", "COD", "COW", "CREEPER", 
			"DOLPHIN", "DONKEY", "DROWNED", "ELDER_GUARDIAN", "ENDER_DRAGON", "ENDERMAN", "ENDERMITE", "EVOKER", "GHAST", "GUARDIAN",
			"HORSE", "HUSK", "ILLUSIONER", "IRON_GOLEM", "LLAMA", "MAGMA_CUBE", "MULE", "PARROT", "PHANTOM", "PIG", "POLAR_BEAR", 
			"PUFFER_FISH", "RABBIT", "SHEEP", "SHULKER", "SILVERFISH", "SKELETON", "SKELETON_HORSE", "SLIME", "SNOW_GOLEM", "SPIDER", 
			"SQUID", "STRAY", "TURTLE", "VEX", "VILLAGER", "VINDICATOR", "WITCH", "WITHER", "WITHER_SKELETON", "WOLF", "ZOMBIE", 
			"ZOMBIE_HORSE", "ZOMBIE_PIGMAN", "ZOMBIE_VILLAGER"));
	
	private static Map<LivingEntity, Integer> bleedingEntities = new HashMap<>();

	/**
	 * Adds bleeding to an entity.
	 * @param entity the entity to add bleeding.
	 * @param duration the amount of times the entity should bleed.
	 */
	public static void addBleed(LivingEntity entity, int duration){
		bleedingEntities.put(entity, duration);
	}
	
	/**
	 * Checks if an entity is bleeding.
	 * @param entity the entity to check.
	 * @return true if the entity is bleeding.
	 */
	public static boolean isBleeding(LivingEntity entity){
		return bleedingEntities.containsKey(entity);
	}
	
	/**
	 * Removes bleeding from an entity.
	 * @param entity entity to remove bleeding from.
	 */
	public static void removeBleed(LivingEntity entity){
		bleedingEntities.remove(entity);
	}
	
	/**
	 * Adds health to an entity, takes max health into account.
	 * @param livingEntity entity to heal.
	 * @param amount amount to heal.
	 */
	public static void addHealth(LivingEntity livingEntity, double amount){
		livingEntity.setHealth(Math.max(0, Math.min(livingEntity.getHealth() + amount, livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())));
	}
	
	/**
	 * Removes health from an entity, ignores armor and no damage ticks and takes into account max and min health values.
	 * @param livingEntity entity to damage.
	 * @param amount the amount of damage.
	 */
	public static void removeHealth(LivingEntity livingEntity, double amount){
		livingEntity.setHealth(Math.max(0, Math.min(livingEntity.getHealth() - amount, livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - amount)));
		livingEntity.playEffect(EntityEffect.HURT);
	}
	
	/**
	 * Stuns an entity, prevents movement, jumping, and attacking.
	 * @param livingEntity entity to stun.
	 * @param duration duration in ticks to stun the entity.
	 */
	public static void stunEntity(LivingEntity livingEntity, int duration){
		livingEntity.addPotionEffect(PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, 6), livingEntity), true);
		livingEntity.addPotionEffect(PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.JUMP, duration, 250), livingEntity), true);
		livingEntity.addPotionEffect(PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 3), livingEntity), true);
		
		//If the entity we are stunning is a player, we need to set it's hunger to <= 6 to stop sprinting.
		if (livingEntity instanceof Player) {
			
			Player player = (Player) livingEntity;
			int playerFoodLevel = player.getFoodLevel();
			player.setFoodLevel(6);
			
			//Reset our hunger after stopping the sprinting.
			new BukkitRunnable() {
				
				public void run() {
					player.setFoodLevel(playerFoodLevel);
				}
			}.runTask(ArchesporeAdventureMain.getPlugin());
		}
	}
	
	/**
	 * Returns a list of all living entity names.
	 * @return a list of strings of all living entity names.
	 */
	public static List<String> getLivingEntityTypes() {
		return aliveEntities;
	}
	
	/**
	 * Runs the bleed effect for bleeding entities.
	 * @param tickCount Used to determine if this effect should run.
	 */
	public static void entityBleedEffect(int tickCount){
		
		//List of all entities that should stop bleeding after this tick.
		List<LivingEntity> entitiesToRemove = new ArrayList<>();
		
		//If this tick is a bleed, loop through all entities in the bleed map and damage them.
		if (tickCount % BLEED_TICKS_CYCLE == 0) {
			for (LivingEntity entity : bleedingEntities.keySet()) {
				int bleedLength = bleedingEntities.get(entity) - 1;
				removeHealth(entity, 1);
				bleedingEntities.put(entity, bleedLength);
				if (bleedLength <= 0) {
					entitiesToRemove.add(entity);
				}
			}
			
			//Remove entities that stopped bleeding.
			for (LivingEntity entity : entitiesToRemove) {
				bleedingEntities.remove(entity);
			}
		}
	}
}
