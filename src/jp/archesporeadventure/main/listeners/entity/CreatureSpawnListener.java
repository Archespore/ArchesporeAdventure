package jp.archesporeadventure.main.listeners.entity;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.generation.itempools.EquipmentPool;
import net.md_5.bungee.api.ChatColor;

public class CreatureSpawnListener implements Listener{

	static List<ChatColor> displayColors = Arrays.asList(ChatColor.WHITE, ChatColor.BLUE, ChatColor.GREEN, ChatColor.YELLOW, ChatColor.GOLD, ChatColor.RED);
	
	@EventHandler
	public void livingEntitySpawn(CreatureSpawnEvent event){
		if (event.getEntityType().isAlive()){
			if (event.getEntity().getLocation().getBlock().getLightFromSky() <= 0){
				event.setCancelled(true);
				return;
			}
			LivingEntity spawnedEntity = (LivingEntity) event.getEntity();
			double mobLevel = Math.round(spawnedEntity.getLocation().distance(new Location(spawnedEntity.getWorld(), 0, spawnedEntity.getLocation().getY(), 0)) / 25.6);
			int mobLevelFloor = (int) Math.min(Math.floor(mobLevel / 10) * 10, 60);
			EquipmentPool levelEquipment = ArchesporeAdventureMain.getEquipmentPoolController().getRegisteredEquipmentPool("MOB_LEVEL_" + mobLevelFloor);
			boolean skeleton = false;
			if (event.getEntityType().equals(EntityType.SKELETON) || event.getEntityType().equals(EntityType.STRAY)) { skeleton = true; }
			ItemStack weapon = levelEquipment.generateWeapon(skeleton);
			ItemStack[] armorItems = levelEquipment.generateArmorSet(ThreadLocalRandom.current().nextBoolean(), ThreadLocalRandom.current().nextBoolean(), ThreadLocalRandom.current().nextBoolean(), ThreadLocalRandom.current().nextBoolean());
			spawnedEntity.getEquipment().setItemInMainHand(weapon);
			spawnedEntity.getEquipment().setArmorContents(armorItems);
			spawnedEntity.setCustomName(String.format(displayColors.get((int) Math.min(5, Math.floor(mobLevel / 10))).toString() + spawnedEntity.getName() + " Level: %.0f", mobLevel));
			spawnedEntity.setCustomNameVisible(true);
			spawnedEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(spawnedEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + mobLevel);
			spawnedEntity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(spawnedEntity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getBaseValue() + (mobLevel / 100));
			spawnedEntity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(spawnedEntity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).getBaseValue() + (mobLevel / 6.25));
			spawnedEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(spawnedEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * (1 + (mobLevel / 125.0)));
			spawnedEntity.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, Integer.MAX_VALUE, (int)mobLevel, true, false), true);
			spawnedEntity.setHealth(spawnedEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			//spawnedEntity.setMetadata("MOBLEVEL", new FixedMetadataValue(ArchesporeAdventureMain.getPlugin(), mobLevel));
		}
	}
}
