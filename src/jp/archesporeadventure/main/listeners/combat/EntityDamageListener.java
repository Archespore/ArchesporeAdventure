package jp.archesporeadventure.main.listeners.combat;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.enchantments.CustomEnchantment;
import jp.archesporeadventure.main.enchantments.SpecialEnchantment;
import jp.archesporeadventure.main.generation.generators.boss.BossGenerator;
import jp.archesporeadventure.main.utils.EnchantmentUtil;
import jp.archesporeadventure.main.utils.LivingEntityUtil;
import jp.archesporeadventure.main.utils.MagicalItemsUtil;

public class EntityDamageListener implements Listener{

	@EventHandler
	public void entityDamageEvent(EntityDamageEvent event) {
		
		Entity eventDefender = event.getEntity();
		
		if (eventDefender instanceof LivingEntity) {
			
			LivingEntity eventLivingDefender = (LivingEntity) eventDefender;
			
			if ((eventLivingDefender.getNoDamageTicks() <= (eventLivingDefender.getMaximumNoDamageTicks() / 2) ) || (eventLivingDefender.getLastDamage() < event.getFinalDamage()) ) {
				
				if (eventLivingDefender.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) && eventLivingDefender.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getAmplifier() >= 5) {
					event.setCancelled(true);
					return;
				}
				
				ItemStack[] armorItems = eventLivingDefender.getEquipment().getArmorContents();
				List<SpecialEnchantment> enchantmentsList = EnchantmentUtil.getUniqueArmorEnchantments(armorItems);
				for (SpecialEnchantment enchant : enchantmentsList) {
					enchant.enchantmentEffect(event);
				}
				
				if (eventDefender instanceof Player) {
					
					Player playerDefender = (Player) eventDefender;
					DamageCause eventCause = event.getCause();
					ItemStack playerHelmet = playerDefender.getEquipment().getHelmet();
					
					//If player has an ender chest open when taking damage, close it.
					if (playerDefender.getOpenInventory().getType().equals(InventoryType.ENDER_CHEST)){
						playerDefender.closeInventory();
					}
					
					//Mob Tears, plays a random mob hurt effect when taking damage.
					if (MagicalItemsUtil.doesContainMagicItem(Material.GHAST_TEAR, playerDefender, true)) {
						List<String> livingEntityTypes = LivingEntityUtil.getLivingEntityTypes();
						playerDefender.getWorld().playSound(playerDefender.getLocation(), Sound.valueOf("ENTITY_" + livingEntityTypes.get(ThreadLocalRandom.current().nextInt(livingEntityTypes.size())).toString().toUpperCase() + "_HURT"), 1.0f, 1.0f);
					}
					
					//Lively Sunflower, Reduces damage if it's day and direct access to sun.
					if (MagicalItemsUtil.doesContainMagicItem(Material.SUNFLOWER, playerDefender)) {
						if ( (playerDefender.getLocation().getBlock().getLightFromSky() == 15) && (playerDefender.getWorld().getTime() >= 0 && playerDefender.getWorld().getTime() <= 12000) ){
							event.setDamage(event.getDamage() - 1);
						}
					}
					
					//Eye of Night, Reduces damage if it's night and direct access to moon.
					if (MagicalItemsUtil.doesContainMagicItem(Material.FERMENTED_SPIDER_EYE, playerDefender)) {
						if ( (playerDefender.getLocation().getBlock().getLightFromSky() == 15) && (playerDefender.getWorld().getTime() >= 12000 && playerDefender.getWorld().getTime() <= 24000) ){
							event.setDamage(event.getDamage() - 1);
						}
					}
					
					//Soul of the Ocean, Reduces damage if player is in water.
					if (MagicalItemsUtil.doesContainMagicItem(Material.PRISMARINE_SHARD, playerDefender)) {
						if (playerDefender.getLocation().getBlock().getType().equals(Material.WATER)){
							event.setDamage(event.getDamage() - 1);
						}
					}
					
					//Miner's Blessing, Reduces damage if there is no skylight.
					if (MagicalItemsUtil.doesContainMagicItem(Material.CLAY_BALL, playerDefender)) {
						if (playerDefender.getLocation().getBlock().getLightFromSky() == 0){
							event.setDamage(event.getDamage() - 1);
						}
					}
					
					//Tough Leather magical item, reduces all damage by .5
					if (MagicalItemsUtil.doesContainMagicItem(Material.LEATHER, playerDefender)){
						event.setDamage(event.getDamage() - .5);
					}
					
					switch (eventCause) {
						case FALL:
							//Feather o' Falling reduces all fall damage to 1/2 heart.
							if (MagicalItemsUtil.doesContainMagicItem(Material.FEATHER, playerDefender)){
								event.setDamage(Math.min(1, event.getDamage()));
							}
							break;
							
						case MAGIC:
							//Zombie Head, heals from instant damage
							if (playerHelmet != null && playerHelmet.getType().equals(Material.ZOMBIE_HEAD) && playerHelmet.getEnchantments().containsKey(CustomEnchantment.MAGICAL.getEnchant())) {
								LivingEntityUtil.addHealth(playerDefender, event.getDamage());
								event.setCancelled(true);
							}
							break;
							
						case POISON:
							//Zombie Head, prevents poison damage
							if (playerHelmet != null && playerHelmet.getType().equals(Material.ZOMBIE_HEAD) && playerHelmet.getEnchantments().containsKey(CustomEnchantment.MAGICAL.getEnchant())) {
								event.setCancelled(true);
								break;
							}
							
						case WITHER:
							//Holy melon, reduces wither and poison damage by half.
							if (MagicalItemsUtil.doesContainMagicItem(Material.GLISTERING_MELON_SLICE, playerDefender)){
								event.setDamage(event.getDamage() / 2);
							}
							break;
							
						case FIRE:
						case FIRE_TICK:
						case LAVA:			
							//Boiling Blood & Wither Skeleton Skull, prevents all fire damage.
							if (MagicalItemsUtil.doesContainMagicItem(Material.MAGMA_CREAM, playerDefender) || (playerHelmet != null && playerHelmet.getType().equals(Material.WITHER_SKELETON_SKULL) && playerHelmet.getEnchantments().containsKey(CustomEnchantment.MAGICAL.getEnchant()))){
								event.setCancelled(true);
							}
							break;
							
						case BLOCK_EXPLOSION:
						case ENTITY_EXPLOSION:
							//Reduces explosion damage by 33%.
							if (MagicalItemsUtil.doesContainMagicItem(Material.GUNPOWDER, playerDefender)){
								event.setDamage((event.getDamage() / 3) * 2);
							}
							break;
							
						case THORNS:
						case CONTACT:
							//Resistance crystal, reduces contact and thorns damage by half.
							if (MagicalItemsUtil.doesContainMagicItem(Material.QUARTZ, playerDefender)){
								event.setDamage(event.getDamage() / 2);
							}
							break;
							
						default:
							break;
					}
					
					//Zombie Head, reduces damage by 1% for each nearby living entity.
					if (playerHelmet != null && playerHelmet.getType().equals(Material.ZOMBIE_HEAD) && playerHelmet.getEnchantments().containsKey(CustomEnchantment.MAGICAL.getEnchant())) {
						
						int reductionAmount = 0;
						for(Entity entity : playerDefender.getNearbyEntities(12, 12, 12)){
							if (entity.getLocation().distance(playerDefender.getLocation()) <= 12 && entity instanceof LivingEntity) {
								reductionAmount = Math.min(reductionAmount + 1, 50);
							}
						}
						event.setDamage(event.getDamage() * (1 - (reductionAmount * .01)));
					}
				}
			}
			
			new BukkitRunnable() {
				
				public void run() {
					BossGenerator bossGenerator = ArchesporeAdventureMain.getBossGenerator();
					if (bossGenerator.isBoss(eventLivingDefender)) { bossGenerator.getBoss(eventLivingDefender).updateBarProgress(); }
				}
				
			}.runTask(ArchesporeAdventureMain.getPlugin());
		}
	}
}
