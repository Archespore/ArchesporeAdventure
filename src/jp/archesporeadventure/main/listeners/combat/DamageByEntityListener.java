package jp.archesporeadventure.main.listeners.combat;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.enchantments.CustomEnchantment;
import jp.archesporeadventure.main.enchantments.SpecialEnchantment;
import jp.archesporeadventure.main.utils.MagicalItemsUtil;
import jp.archesporeadventure.main.utils.PotionEffectUtil;

public class DamageByEntityListener implements Listener{

	@EventHandler
	public void damageByEntityEvent(EntityDamageByEntityEvent event) {
		
		Entity eventAttacker = event.getDamager();
		Entity eventDefender = event.getEntity();
		
		if (eventAttacker instanceof LivingEntity && eventDefender instanceof LivingEntity) {
			
			LivingEntity eventLivingAttacker = (LivingEntity) eventAttacker;
			LivingEntity eventLivingDefender = (LivingEntity) eventDefender;
			ItemStack attackerWeapon = eventLivingAttacker.getEquipment().getItemInMainHand();
			//boolean used to determine if this event is a support event.
			boolean supportEffect = false;
			
			//Cancels this event and returns if the attacker has blindness and misses or has the support enchantment.
			if (eventLivingAttacker.hasPotionEffect(PotionEffectType.BLINDNESS) && ThreadLocalRandom.current().nextInt(4) <= eventLivingAttacker.getPotionEffect(PotionEffectType.BLINDNESS).getAmplifier()) {
				event.setCancelled(true);
				return;
			}
			
			//If the event is a support, set the boolean.
			if (attackerWeapon.getEnchantments().containsKey(CustomEnchantment.SUPPORT.getEnchant())) { 
				supportEffect = true;
				event.setCancelled(true);
			}
			
			//Item Enchantments
			for (Enchantment enchantment : attackerWeapon.getEnchantments().keySet()) {
				if (enchantment instanceof SpecialEnchantment) {
					SpecialEnchantment specialEnchantment = (SpecialEnchantment) enchantment;
					if (!specialEnchantment.isPassive() && specialEnchantment.isSupport() == supportEffect) {
						specialEnchantment.enchantmentEffect(event);
					}
				}
			}
			
			//TODO: Move and clean
			if (eventLivingAttacker.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
				new BukkitRunnable() {
					
					public void run() {
						eventLivingDefender.setNoDamageTicks(eventLivingDefender.getNoDamageTicks() - (eventLivingAttacker.getPotionEffect(PotionEffectType.FAST_DIGGING).getAmplifier() + 1));
					}
				}.runTask(ArchesporeAdventureMain.getPlugin());
			}
			
			if (eventAttacker instanceof Player) {
				
				Player playerAttacker = (Player) eventAttacker;
				
				//Heart of a Cannibal, gives a chance to restore hunger upon attacking.
				boolean effectHeartOfCannibal = false;
				for (int loopValue = 0; loopValue < MagicalItemsUtil.containsMagicItemAmount(Material.ROSE_RED, playerAttacker); loopValue++) {
					if (ThreadLocalRandom.current().nextDouble(100) < 20.0) {
						if (playerAttacker.getFoodLevel() >= 20) { playerAttacker.setSaturation(Math.min(playerAttacker.getSaturation() + 2, 20)); }
						else { playerAttacker.setFoodLevel(Math.min(playerAttacker.getFoodLevel() + 2, 20)); }
						effectHeartOfCannibal = true;
					}
				}
				//If the effect activated at least once.
				if (effectHeartOfCannibal) { playerAttacker.playSound(playerAttacker.getLocation(), Sound.ENTITY_GENERIC_EAT, .75f, .5f); }
				
				//Lively Sunflower, Increases damage if it's day and direct access to sun.
				if (MagicalItemsUtil.doesContainMagicItem(Material.SUNFLOWER, playerAttacker)) {
					if ( (playerAttacker.getLocation().getBlock().getLightFromSky() == 15) && (playerAttacker.getWorld().getTime() >= 0 && playerAttacker.getWorld().getTime() <= 12000) ){
						event.setDamage(event.getDamage() + 1);
					}
				}
				
				//Eye of Night, Increases damage if it's night and direct access to moon.
				if (MagicalItemsUtil.doesContainMagicItem(Material.FERMENTED_SPIDER_EYE, playerAttacker)) {
					if ( (playerAttacker.getLocation().getBlock().getLightFromSky() == 15) && (playerAttacker.getWorld().getTime() >= 12000 && playerAttacker.getWorld().getTime() <= 24000) ){
						event.setDamage(event.getDamage() + 1);
					}
				}
				
				//Soul of the Ocean, Increases damage if player is in water.
				if (MagicalItemsUtil.doesContainMagicItem(Material.PRISMARINE_SHARD, playerAttacker)) {
					if (playerAttacker.getLocation().getBlock().getType().equals(Material.WATER)){
						event.setDamage(event.getDamage() + 1);
					}
				}
				
				//Miner's Blessing, Increases damage if there is no skylight.
				if (MagicalItemsUtil.doesContainMagicItem(Material.CLAY_BALL, playerAttacker)) {
					if (playerAttacker.getLocation().getBlock().getLightFromSky() == 0){
						event.setDamage(event.getDamage() + 1);
					}
				}
				
				//Binding string, chance to slow entities.
				if (ThreadLocalRandom.current().nextDouble(100) < (MagicalItemsUtil.containsMagicItemAmount(Material.STRING, playerAttacker) * 10.0)) {
					PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.SLOW, 50, 0), eventLivingDefender);
					eventLivingDefender.addPotionEffect(newPotionEffect, true);
				}
				
				//Blinding sac, has a chance to blind entities.
				if (ThreadLocalRandom.current().nextDouble(100) < (MagicalItemsUtil.containsMagicItemAmount(Material.INK_SAC, playerAttacker) * 10.0)) {
					PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 50, 0), eventLivingDefender);
					eventLivingDefender.addPotionEffect(newPotionEffect, true);
				}
				
				//Chase down, gives speed upon hitting an entity.
				if (MagicalItemsUtil.doesContainMagicItem(Material.FURNACE_MINECART, playerAttacker)) {
					PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.SPEED, 30, 0), playerAttacker);
					playerAttacker.addPotionEffect(newPotionEffect, true);
				}
				
				//Nugget of Strength, Increases damage when below 50% health.
				if (playerAttacker.getHealth() <= playerAttacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() / 2) {
					event.setDamage(event.getDamage() + (.25 * MagicalItemsUtil.containsMagicItemAmount(Material.GOLD_NUGGET, playerAttacker)));
				}
				
				//Wolf's Bone, prevents you from damaging your own wolves.
				if (eventDefender instanceof Wolf) {
					Wolf eventWolfDefender = (Wolf) eventDefender;
					if (MagicalItemsUtil.doesContainMagicItem(Material.BONE, playerAttacker)) {
						if (eventWolfDefender.isTamed() && eventWolfDefender.getOwner().equals(playerAttacker)) {
							event.setCancelled(true);
						}
					}
				}
				
				
				if (playerAttacker.getEquipment().getHelmet() != null) {
					
					ItemStack playerHelmet = playerAttacker.getEquipment().getHelmet();
					
					//Wither Skull, Applies wither to hit entities.
					if (playerHelmet.getType().equals(Material.WITHER_SKELETON_SKULL) && playerHelmet.getEnchantments().containsKey(CustomEnchantment.MAGICAL.getEnchant())) {
						PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 0), eventLivingDefender);
						eventLivingDefender.addPotionEffect(newPotionEffect, true);
					}
					
					//Archespore's Head, kills entities below 25% health.
					if (playerHelmet.getType().equals(Material.PLAYER_HEAD) && playerHelmet.getEnchantments().containsKey(CustomEnchantment.MAGICAL.getEnchant())) {
						if (eventLivingDefender.getHealth() < (eventLivingDefender.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())/4) {
							eventLivingDefender.setHealth(0);
						}
					}
				}
			}
			
			if (eventDefender instanceof Player) {
				
				Player playerDefender = (Player) eventDefender;
				
				//Countering Stone, has a chance to deal damage back to the attacker.
				if (ThreadLocalRandom.current().nextDouble(100) < MagicalItemsUtil.containsMagicItemAmount(Material.FLINT, playerDefender) * 7.5) {
					double reflectDamage = event.getDamage() / 3;
					if (eventLivingAttacker instanceof Player && MagicalItemsUtil.doesContainMagicItem(Material.QUARTZ, (Player)eventLivingAttacker)) {
						reflectDamage /= 2;
					}
					eventLivingAttacker.damage(reflectDamage, playerDefender);
				}
			}
		}
		
		//If the entity was damaged by a projectile.
		if (eventAttacker instanceof Projectile) {
			
			Projectile eventProjectile = (Projectile) eventAttacker;
			if (eventProjectile.hasMetadata("SUPPORT")) { 
				event.setDamage(0);
			}
		}
	}
}
