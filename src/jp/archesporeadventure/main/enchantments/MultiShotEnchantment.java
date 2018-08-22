package jp.archesporeadventure.main.enchantments;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Arrow.PickupStatus;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import jp.archesporeadventure.main.ArchesporeAdventureMain;

public class MultiShotEnchantment extends SpecialEnchantment {
	
	final double ANGLE_MAX_OFFSET = .075;

	public MultiShotEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Multi-Shot";
	}

	public int getMaxLevel() {
		return 3;
	}

	public boolean isPassive() {
		return false;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.BOW;
	}
	
	/**
	 * Shoots additional arrows when firing a fully charged bow.
	 */
	public boolean enchantmentEffect(Event event) {
		if (event instanceof EntityShootBowEvent) {
			
			EntityShootBowEvent shootBowEvent = (EntityShootBowEvent) event;
			
			//Is this a fully charged shot?
			if (shootBowEvent.getForce() >= 1) {
				
				LivingEntity shooter = shootBowEvent.getEntity();
				Entity eventProjectile = shootBowEvent.getProjectile();
				ItemStack shooterBow = shootBowEvent.getBow();
				Vector arrowVector = eventProjectile.getVelocity();
				int enchantmentLevel = shooterBow.getEnchantments().get(this);
				
				if (eventProjectile instanceof Arrow) {
					
					Arrow eventArrow = (Arrow) eventProjectile;
					new BukkitRunnable() {
						
						int multishotCount = enchantmentLevel;
						
						public void run() {
							if (multishotCount <= 0) {
								Bukkit.getScheduler().cancelTask(this.getTaskId());
							}
							else {
								if (shooter instanceof Player) {
									Player shooterPlayer = (Player) shooter;
									if (shooterPlayer.getInventory().contains(Material.ARROW)) {
										shooterPlayer.playSound(shooterPlayer.getLocation(), Sound.ENTITY_ARROW_SHOOT, .5f, 1);
										
										Vector newArrowVector = arrowVector.clone();
										newArrowVector.add(new Vector(ThreadLocalRandom.current().nextDouble(-ANGLE_MAX_OFFSET, ANGLE_MAX_OFFSET), ThreadLocalRandom.current().nextDouble(-ANGLE_MAX_OFFSET, ANGLE_MAX_OFFSET), 
												ThreadLocalRandom.current().nextDouble(-ANGLE_MAX_OFFSET, ANGLE_MAX_OFFSET)));
										Arrow newArrow = shooter.launchProjectile(Arrow.class, newArrowVector);
										newArrow.setCritical(eventArrow.isCritical());
										newArrow.setShooter(shooterPlayer);
										newArrow.setPickupStatus(PickupStatus.CREATIVE_ONLY);
										if (!shooterBow.containsEnchantment(Enchantment.ARROW_INFINITE)) {
											shooterPlayer.getInventory().removeItem(new ItemStack(Material.ARROW, 1));
											newArrow.setPickupStatus(PickupStatus.ALLOWED);
										}
									}
									else {
										Bukkit.getScheduler().cancelTask(this.getTaskId());
									}
								}
								multishotCount--;
							}
						}
					}.runTaskTimer(ArchesporeAdventureMain.getPlugin(), 2, 2);
				}
			}
		}
		return false;
	}
}
