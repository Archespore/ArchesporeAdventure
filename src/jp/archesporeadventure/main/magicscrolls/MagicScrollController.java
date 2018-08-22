package jp.archesporeadventure.main.magicscrolls;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Arrow.PickupStatus;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.controllers.ArtilleryBarrageController;
import jp.archesporeadventure.main.controllers.BlackholeController;
import jp.archesporeadventure.main.controllers.ChaosStormController;
import jp.archesporeadventure.main.controllers.GroundPoundController;
import jp.archesporeadventure.main.utils.LivingEntityUtil;
import jp.archesporeadventure.main.utils.ParticleUtil;
import jp.archesporeadventure.main.utils.PotionEffectUtil;

public class MagicScrollController {

	public static void magicalEffect(Player player, int effectLevel) {
		
		Location playerLocation = player.getLocation();
		World eventWorld = player.getWorld();
		PotionEffect newPotionEffect;
		ItemStack magicalScroll = player.getInventory().getItemInMainHand();
		
		switch(magicalScroll.getEnchantmentLevel(Enchantment.ARROW_DAMAGE)) {
		case 1:
			//Flurry of snow, launches 50 snowballs that damage and slow
			new BukkitRunnable() {
				
				int loopValue = 0;
				
				public void run() {
					if (loopValue >= 50) {
						Bukkit.getScheduler().cancelTask(this.getTaskId());
					}
					else {
						player.playSound(player.getLocation(), Sound.ENTITY_SNOWBALL_THROW, .5f, 1.5f);
						Snowball newSnowball = player.launchProjectile(Snowball.class, player.getLocation().getDirection().multiply(2.5));
						newSnowball.setMetadata("SLOWBALL", new FixedMetadataValue(ArchesporeAdventureMain.getPlugin(), true));
						newSnowball.setMetadata("ROCKBALL", new FixedMetadataValue(ArchesporeAdventureMain.getPlugin(), 3));
					}
					loopValue++;
				}
			}.runTaskTimer(ArchesporeAdventureMain.getPlugin(), 1, 2);
			break;
			
		case 2:
			//Barrage of Fire, launches 150 arrows that burn and deal slight damage.
			new BukkitRunnable() {
				
				int loopValue = 0;
				
				public void run() {
					if (loopValue >= 50) {
						Bukkit.getScheduler().cancelTask(this.getTaskId());
					}
					else {
						player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, .5f, 1.25f);
						Vector playerDirection = player.getLocation().getDirection().clone();
						for (int loopValue = 0; loopValue < 3; loopValue++) {
							
							double angleRadians = Math.toRadians(5 - (loopValue * 5));
							
							Vector newArrowVector = new Vector(playerDirection.getX() * Math.cos(angleRadians) - playerDirection.getZ() * Math.sin(angleRadians),
									playerDirection.getY(), playerDirection.getX() * Math.sin(angleRadians) + playerDirection.getZ() * Math.cos(angleRadians));
							
							Arrow newArrow = player.launchProjectile(Arrow.class, newArrowVector.multiply(2.25));
							newArrow.setFireTicks(300);
							newArrow.setMetadata("SKYARROW", new FixedMetadataValue(ArchesporeAdventureMain.getPlugin(), true));
							newArrow.spigot().setDamage(0);
						}
					}
					loopValue++;
				}
			}.runTaskTimer(ArchesporeAdventureMain.getPlugin(), 1, 2);
			break;
			
		case 3:
			//Storm of Chaos, creates a thunderstorm at a target area.
			ChaosStormController.addStormEffect(player.getTargetBlock(new HashSet<Material>(Arrays.asList(Material.AIR, Material.GRASS, Material.TALL_GRASS)), 64).getLocation().add(0, 1, 0), 10);
			break;
			
		case 4:
			//Blinding Flare, blinds, burns, and weakens nearby entities, the closer to the player, the longer duration.
			eventWorld.playSound(playerLocation, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1f, .75f);
			eventWorld.playSound(playerLocation, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1f, .75f);
			ParticleUtil.spawnWorldParticles(Particle.FLAME, playerLocation.clone().add(0, 3, 0), 50, 0, 0, 0, .2);
			ParticleUtil.spawnWorldParticles(Particle.FIREWORKS_SPARK, playerLocation.clone().add(0, 3, 0), 25, 0, 0, 0, .2);
			
			for (Entity entity : player.getNearbyEntities(16, 16, 16)) { 
				if (entity instanceof LivingEntity && playerLocation.distance(entity.getLocation()) <= 16) {
					
					LivingEntity entityLiving = (LivingEntity) entity;
					double entityDistance = entity.getLocation().distance(playerLocation);
					double durationModifier = (16.0 - entityDistance) / 16.0;
					
					entityLiving.setFireTicks((int)(1200 * durationModifier));
					
					newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 
							(int)(1200 * durationModifier), 1), entityLiving);
					entityLiving.addPotionEffect(newPotionEffect, true);
					
					newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 
							(int)(1200 * durationModifier), 0), entityLiving);
					entityLiving.addPotionEffect(newPotionEffect, true);
				}
			}
			break;
			
		case 5:
			//Scroll of Polymorphing, turns entities into sheep in a 8 block radius around the player.
			for (Entity entity : player.getNearbyEntities(8, 8, 8)) {
				if (entity instanceof LivingEntity && playerLocation.distance(entity.getLocation()) <= 8 && !(entity instanceof Sheep)) {
					
					LivingEntity entityLiving = (LivingEntity) entity;
					entityLiving.setHealth(0);
					entityLiving.getWorld().spawnEntity(entityLiving.getLocation(), EntityType.SHEEP);
					player.playSound(playerLocation, Sound.ENTITY_PUFFER_FISH_BLOW_OUT, .25f, .75f);
				}
			}
			break;
			
		case 6:
			//TODO: Clean up this code
			//Rain of Wealth, gives the player a random amount of emeralds.
			Firework firework = player.getWorld().spawn(playerLocation, Firework.class);
			firework.setVelocity(new Vector(0, -.25, 0));
			FireworkMeta fireworkMeta = firework.getFireworkMeta();
			fireworkMeta.setPower(0);
			fireworkMeta.addEffect(FireworkEffect.builder()
					.flicker(false)
					.trail(true)
					.with(Type.BALL)
					.withColor(Color.fromRGB(80, 190, 60))
					.withFade(Color.fromRGB(220, 255, 220))
					.build());
			firework.setFireworkMeta(fireworkMeta);
			new BukkitRunnable() {
				
				int loopValue = 0;
				int loopAmount = (8 + ThreadLocalRandom.current().nextInt(57));
				
				public void run() {
					if (loopValue >= loopAmount) {
						Bukkit.getScheduler().cancelTask(this.getTaskId());
					}
					else {
						Item emeraldRain = player.getWorld().dropItem(player.getLocation().add(0, 1.75, 0), new ItemStack(Material.EMERALD, 1));
						emeraldRain.setPickupDelay(3);
						loopValue++;
					}
				}
			}.runTaskTimer(ArchesporeAdventureMain.getPlugin(), 0, 5);
			break;
			
		case 7:
			//TODO: Needs to be reworked..
			//Ice Block, freezes the player and prevents damage.
			
			newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 5), player);
			player.addPotionEffect(newPotionEffect, true);
			newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1), player);
			player.addPotionEffect(newPotionEffect, true);
			LivingEntityUtil.stunEntity(player, 200);
			
			//Particle effects
			new BukkitRunnable() {
				
				int loopValue = 0;
				int effectDuration = 200;
				
				public void run() {
					if (loopValue >= effectDuration) {
						Bukkit.getScheduler().cancelTask(this.getTaskId());
					}
					else {
						ParticleUtil.spawnWorldParticles(Particle.BLOCK_CRACK, player.getLocation().clone().add(0, 1, 0), 2, .25, .5, .25, 0, Material.ICE.createBlockData());
						loopValue += 2;
					}
				}
			}.runTaskTimer(ArchesporeAdventureMain.getPlugin(), 0, 2);
			
			break;
			
		case 8:
			//Scroll of Life, instantly restores health and hunger.
			player.playSound(playerLocation, Sound.ITEM_TRIDENT_RETURN, .75f, 1.5f);
			LivingEntityUtil.addHealth(player, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			player.setFoodLevel(20);
			player.setSaturation(20);
			break;
			
		case 9:
			//Blossom of Arrows, launches arrows towards nearby entities.
			new BukkitRunnable() {
				
				int loopValue = 0;
				
				public void run() {
					if (loopValue >= 30) {
						Bukkit.getScheduler().cancelTask(this.getTaskId());
					}
					else {
						for (Entity entity : player.getNearbyEntities(12, 12, 12)) {
							if (entity instanceof LivingEntity && player.getLocation().distance(entity.getLocation()) <= 12) {
								
								LivingEntity entityLiving = (LivingEntity) entity;
								Arrow arrow = player.launchProjectile(Arrow.class, entityLiving.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(1.75));
								arrow.setMetadata("SKYARROW", new FixedMetadataValue(ArchesporeAdventureMain.getPlugin(), true));
								arrow.setPickupStatus(PickupStatus.CREATIVE_ONLY);
							}
						}
						loopValue++;
					}
				}
			}.runTaskTimer(ArchesporeAdventureMain.getPlugin(), 0, 10);
			break;
			
		case 10:
			//Void Walk, teleports to nearby entities and damages them.
			List<Entity> nearbyEntities = player.getNearbyEntities(16, 16, 16);
			nearbyEntities.removeIf( entity -> entity.getLocation().distance(playerLocation) > 16 || !(entity instanceof LivingEntity));
			newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10 + nearbyEntities.size() * 2, 5), player);
			player.addPotionEffect(newPotionEffect, true);
			
			new BukkitRunnable() {
				
				int loopValue = 0;
				
				public void run() {
					if (loopValue >= nearbyEntities.size()) {
						Bukkit.getScheduler().cancelTask(this.getTaskId());
						player.teleport(playerLocation);
					}
					else {
						
						Entity loopEntity = nearbyEntities.get(loopValue);
						if (loopEntity instanceof LivingEntity) {
							
							LivingEntity loopLivingEntity = (LivingEntity) loopEntity;
							player.teleport(loopLivingEntity);
							player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, .75f, 1.25f);
							loopLivingEntity.damage(player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue(), player);
						}
						loopValue++;
					}
				}
			}.runTaskTimer(ArchesporeAdventureMain.getPlugin(), 0, 2);
			break;
			
		case 11:
			//Scroll of Repairing
			for (ItemStack item : player.getInventory()) {
				if (item != null && EnchantmentTarget.BREAKABLE.includes(item.getType())) {
					item.setDurability((short) 0);
				}
			}
			player.playSound(playerLocation, Sound.BLOCK_ANVIL_USE, .75f, .9f);
			break;
			
		case 12:
			//Blackhole, creates a blackhole at the target location
			BlackholeController.addBlackholeEffect(player.getTargetBlock(new HashSet<Material>(Arrays.asList(Material.AIR, Material.GRASS, Material.TALL_GRASS)), 64).getLocation().add(0, 1, 0), 200);
			break;
			
		case 13:
			//Explosive Meteor, summons a highly explosive meteor
			LargeFireball fireball = player.getWorld().spawn(playerLocation.clone().add(playerLocation.clone().getDirection().normalize().getX(), 1, playerLocation.clone().getDirection().normalize().getZ()), LargeFireball.class);
			fireball.setDirection(new Vector(0, 0, 0));
			fireball.setYield(3.5f);
			break;
			
		case 14:
			//Imbue Knowledge, gives a random amount of xp from 698 - 1396.
			new BukkitRunnable() {
				
				int loopValue = 0;
				int loopAmount = 698 + ThreadLocalRandom.current().nextInt(699);
				
				public void run() {
					
					if (loopValue >= loopAmount) {
						Bukkit.getScheduler().cancelTask(this.getTaskId());
					}
					else {
						player.giveExp(1);
						player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .1f, (float)(1.25 + ThreadLocalRandom.current().nextDouble(.4) - .2));
						loopValue++;
					}
				}
			}.runTaskTimer(ArchesporeAdventureMain.getPlugin(), 0, 1);
			break;
			
		case 15:
			//Grenade Toss
			//TODO: Rewrite this, very ugly
			new BukkitRunnable() {
				
				int loopValue = 0;
				int loopAmount = 200;
				Map<Item, Integer> droppedTNT = new HashMap<>();
				
				public void run() {
					for (Item tntItem : droppedTNT.keySet()) {
						droppedTNT.put(tntItem, droppedTNT.get(tntItem) - 1);
						tntItem.setCustomName(String.valueOf(droppedTNT.get(tntItem)));
						if (droppedTNT.get(tntItem) <= 0) {
							tntItem.getWorld().createExplosion(tntItem.getLocation().getX(), tntItem.getLocation().getY() + 1, tntItem.getLocation().getZ(), 1.5f, false, false);
							tntItem.remove();
						}
						else {
							for (Entity entity : tntItem.getNearbyEntities(.25, .25, .25)) {
								if (entity instanceof LivingEntity) {
									entity.getWorld().createExplosion(entity.getLocation().getX(), entity.getLocation().getY() + 1.5, entity.getLocation().getZ(), 1.5f, false, false);
									droppedTNT.put(tntItem, 0);
									tntItem.remove();
								}
							}
						}
					}
					droppedTNT.entrySet().removeIf( entry -> entry.getValue() <= 0);
					if (loopValue >= loopAmount) {
						if (droppedTNT.size() <= 0) {
							Bukkit.getScheduler().cancelTask(this.getTaskId());
						}
					}
					else if (loopValue % 20 == 0){
						Item tnt = player.getWorld().dropItem(player.getLocation().add(0, 1.5, 0), new ItemStack(Material.TNT, 1));
						tnt.setPickupDelay(60);
						tnt.setVelocity(player.getLocation().getDirection().multiply(1.25));
						tnt.setInvulnerable(true);
						tnt.setCustomName("30");
						tnt.setCustomNameVisible(true);
						tnt.setMetadata("OWNER", new FixedMetadataValue(ArchesporeAdventureMain.getPlugin(), player.getDisplayName()));
						droppedTNT.put(tnt, 30);
					}
					loopValue++;
				}
			}.runTaskTimer(ArchesporeAdventureMain.getPlugin(), 0, 1);
			break;
			
		case 16:
			//Wind Enchant, After accelerating for a few seconds, gain a burst of speed and attack speed for ~ 30 seconds.
			new BukkitRunnable() {
				
				int loopValue = 0;
				public void run() {
					if (loopValue >= 4) {
						Bukkit.getScheduler().cancelTask(this.getTaskId());
					}
					else {
						PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, loopValue), player);
						player.addPotionEffect(newPotionEffect, true);
						newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 300, loopValue - 1), player);
						player.addPotionEffect(newPotionEffect, true);
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 1.0f, (float) (1.25 + (loopValue * .25)));
						ParticleUtil.spawnWorldParticles(Particle.END_ROD, player.getLocation().add(0, 1, 0), 5 + (loopValue * 5), .25, .25, .25, .1);
						loopValue++;
					}
					
				}
			}.runTaskTimer(ArchesporeAdventureMain.getPlugin(), 0, 30);
			break;
			
		case 17:
			//Artillery Barrage, creates an explosion in a small area after a few seconds.
			ArtilleryBarrageController.addArtilleryEffect(player.getTargetBlock(new HashSet<Material>(Arrays.asList(Material.AIR, Material.GRASS, Material.TALL_GRASS)), 64).getLocation().add(0, 1, 0), 6);
			//This effect gives a reduced cooldown.
			player.setCooldown(Material.PAPER, 20);
			break;
			
		case 18:
			//Teleports you to the nearest city after 10 seconds.
			break;
			
		case 19:
			//Call of the Wild, summons 2 tamed dogs with enchanced stats.
			for (int loopValue = 0; loopValue < 2; loopValue++) {
				Wolf summonedWolf = playerLocation.getWorld().spawn(playerLocation, Wolf.class);
				summonedWolf.setOwner(player);
				summonedWolf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(summonedWolf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() + 2);
				summonedWolf.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(summonedWolf.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + 2);
				summonedWolf.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(summonedWolf.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 1.2);
				LivingEntityUtil.addHealth(summonedWolf, summonedWolf.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			}
			player.playSound(playerLocation, Sound.ENTITY_WOLF_HOWL, 1f, .75f);
			break;
			
		case 20:
			//Ground Pound, leaps into the air and hits the ground with force.
			GroundPoundController.addGroundPound(player);
			player.setVelocity(player.getVelocity().add(new Vector(0, 1, 0)));
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 1), true);
			new BukkitRunnable() {
				
				int timeoutValue = 0;
				
				public void run() {
					//If we haven't touched the ground after 15 seconds, cancel the effect
					if (timeoutValue >= 300) {
						Bukkit.getScheduler().cancelTask(this.getTaskId());
						GroundPoundController.removeGroundPound(player);
					}
					else if (player.isOnGround() && GroundPoundController.doesHaveGroundPond(player)) {
						GroundPoundController.createGroundPoundEffect(player);
						Bukkit.getScheduler().cancelTask(this.getTaskId());
					}
					timeoutValue++;
				}
			}.runTaskTimer(ArchesporeAdventureMain.getPlugin(), 1, 1);
			break;
			
		case 21:
			//Phase Walk, Temporary allows the player to pass through walls.
			player.setGameMode(GameMode.SPECTATOR);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 1.0f, 1.5f);
			player.setCooldown(Material.PAPER, 180);
			new BukkitRunnable() {
				
				public void run() {
					player.setGameMode(GameMode.SURVIVAL);
					if (player.getLocation().getBlock().getType() != Material.AIR || player.getEyeLocation().getBlock().getType() != Material.AIR) { 
						
						Location playerLocation = player.getLocation();
						Location newPlayerLocation = playerLocation.getWorld().getHighestBlockAt(playerLocation.add(0, 1, 0)).getLocation();
						newPlayerLocation.setPitch(playerLocation.getPitch());
						newPlayerLocation.setYaw(playerLocation.getYaw());
						player.teleport(newPlayerLocation);
					}
				}
			}.runTaskLater(ArchesporeAdventureMain.getPlugin(), 80);
			break;
			
		case 22:
			//Implosion, draws in entities then explodes, damaging and knocking them back.
			new BukkitRunnable() {
				
				int loopValue = 0;
				public void run() {
					if (loopValue >= 5) {
						Bukkit.getScheduler().cancelTask(this.getTaskId());
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE,3.5f, .75f);
						ParticleUtil.spawnWorldParticles(Particle.EXPLOSION_LARGE, player.getLocation().add(0, 1, 0), 1);
						for (Entity entity : player.getNearbyEntities(12, 12, 12)) {
							if (entity.getLocation().distance(player.getLocation()) <= 12) {
								entity.setVelocity(entity.getVelocity().add(player.getLocation().clone().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(-2.5).add(new Vector(0, .25, 0))));
								if (entity instanceof LivingEntity) {
									((LivingEntity) entity).damage(10, player);
								}
							}
						}
					}
					else {
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EVOKER_PREPARE_SUMMON, 2.0f, (float) (1.25 + (loopValue * .2)));
						ParticleUtil.spawnWorldParticles(Particle.SPELL_INSTANT, player.getLocation().add(0, 1, 0), 5 + (loopValue * 5), .25, .25, .25, .5);
						for (Entity entity : player.getNearbyEntities(12, 12, 12)) {
							if (entity.getLocation().distance(player.getLocation()) <= 12) {
								entity.setVelocity(entity.getVelocity().add(player.getLocation().clone().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(.75)));
							}
						}
						loopValue++;
					}
					
				}
			}.runTaskTimer(ArchesporeAdventureMain.getPlugin(), 0, 20);
			break;
			
		case 23:
			//Fireworks Display, creates a bunch of random fireworks.
			new BukkitRunnable() {
				
				int loopValue = 0;
				public void run() {
					if (loopValue >= 25) {
						Bukkit.getScheduler().cancelTask(this.getTaskId());
					}
					else {
						Firework firework = playerLocation.getWorld().spawn(playerLocation.clone().add(ThreadLocalRandom.current().nextInt(2) - 1, 0, ThreadLocalRandom.current().nextInt(2) - 1), Firework.class);
						FireworkMeta fireworkMeta = firework.getFireworkMeta();
						fireworkMeta.setPower(1);
						fireworkMeta.addEffect(FireworkEffect.builder()
								.flicker(ThreadLocalRandom.current().nextInt(2) == 0 ? true : false)
								.trail(ThreadLocalRandom.current().nextInt(2) == 0 ? true : false)
								.with(Type.values()[ThreadLocalRandom.current().nextInt(Type.values().length)])
								.withColor(Color.fromRGB(ThreadLocalRandom.current().nextInt(256), ThreadLocalRandom.current().nextInt(256), ThreadLocalRandom.current().nextInt(256)))
								.withColor(Color.fromRGB(ThreadLocalRandom.current().nextInt(256), ThreadLocalRandom.current().nextInt(256), ThreadLocalRandom.current().nextInt(256)))
								.withFade(Color.fromRGB(ThreadLocalRandom.current().nextInt(256), ThreadLocalRandom.current().nextInt(256), ThreadLocalRandom.current().nextInt(256)))
								.build());
						firework.setFireworkMeta(fireworkMeta);
						loopValue++;
					}
				}
			}.runTaskTimer(ArchesporeAdventureMain.getPlugin(), 0, 20);
			break;
			
		case 24:
			//Teleports you to the nearest city after 10 seconds.
			break;
			
		default:
			break;
	}
	}
}
