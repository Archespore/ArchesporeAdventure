package jp.archesporeadventure.main.listeners.combat;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.utils.ParticleUtil;
import jp.archesporeadventure.main.utils.PotionEffectUtil;

public class ProjectileHitListener implements Listener{

	@EventHandler
	public void projectileHitEvent(ProjectileHitEvent event) {
		
		Projectile projectile = event.getEntity();
		Entity eventHitEntity = event.getHitEntity();
		
		if (eventHitEntity instanceof LivingEntity) {
			
			LivingEntity eventHitLivingEntity = (LivingEntity) eventHitEntity;
			if (projectile instanceof Arrow) {
				
				Arrow arrow = (Arrow) event.getEntity();
				
				if ( (eventHitLivingEntity.getNoDamageTicks()) <= (eventHitLivingEntity.getMaximumNoDamageTicks() / 2) ) {
					if (arrow.hasMetadata("TRUEOWNER")) { 
						arrow.setShooter((ProjectileSource) Bukkit.getEntity(UUID.fromString(arrow.getMetadata("TRUEOWNER").get(0).asString())));
					}
				}
				
				if (arrow.hasMetadata("HEAVYIMPACT")) { effectHeavyImpact(arrow, eventHitLivingEntity); }
				if (arrow.hasMetadata("ELECTRICAL")) { effectElectrical(arrow, eventHitLivingEntity); }
				if (arrow.hasMetadata("PIERCE")) { effectPierce(arrow, eventHitLivingEntity); }
				if (arrow.hasMetadata("RAINOFARROWS")) { effectRainOfArrows(arrow, eventHitLivingEntity); }
				
			}
			else if (projectile instanceof Snowball) {
				
				Snowball snowball = (Snowball) event.getEntity();
				
				if (snowball.hasMetadata("SLOWBALL")) { effectSlowball(snowball, eventHitLivingEntity); }
				
				if (snowball.hasMetadata("ROCKBALL") && projectile.getShooter() instanceof Entity) { 
					eventHitLivingEntity.damage(snowball.getMetadata("ROCKBALL").get(0).asInt(), (Entity) projectile.getShooter());
				}
			}
		}
		
		//Firework Arrows, creates fireworks when the projectile hits the ground or entity.
		if (projectile.hasMetadata("FIREWORK")){
			Firework firework = projectile.getWorld().spawn(projectile.getLocation(), Firework.class);
			FireworkMeta fireworkMeta = firework.getFireworkMeta();
			fireworkMeta.setPower(ThreadLocalRandom.current().nextInt(2));
			fireworkMeta.addEffect(FireworkEffect.builder()
					.flicker(false)
					.trail(true)
					.with(Type.BALL)
					.withColor(Color.fromRGB(ThreadLocalRandom.current().nextInt(256), ThreadLocalRandom.current().nextInt(256), ThreadLocalRandom.current().nextInt(256)))
					.withColor(Color.fromRGB(ThreadLocalRandom.current().nextInt(256), ThreadLocalRandom.current().nextInt(256), ThreadLocalRandom.current().nextInt(256)))
					.withFade(Color.fromRGB(ThreadLocalRandom.current().nextInt(256), ThreadLocalRandom.current().nextInt(256), ThreadLocalRandom.current().nextInt(256)))
					.build());
			firework.setFireworkMeta(fireworkMeta);
		}
		
		if (projectile.hasMetadata("SPAWNEGG")) { projectile.getWorld().spawnEntity(projectile.getLocation(), EntityType.valueOf(projectile.getMetadata("SPAWNEGG").get(0).asString())); }
		if (projectile.hasMetadata("SKYARROW")) projectile.remove();
	}
	
	/**
	 * Heavy Impact enchantment effect, hurts other entities around the hit target.
	 * @param arrow arrow that hit the entity
	 * @param entity entity that was hit by arrow
	 */
	private void effectHeavyImpact(Arrow arrow, LivingEntity entity){
		
		int effectLevel = arrow.getMetadata("HEAVYIMPACT").get(0).asInt();
		double arrowDamage = arrow.spigot().getDamage();
		
		for (Entity nearbyEntity : entity.getNearbyEntities(effectLevel, effectLevel, effectLevel)) {
			if ( (nearbyEntity instanceof LivingEntity) && (entity.getLocation().distance(nearbyEntity.getLocation()) <= effectLevel) ){
				((LivingEntity) nearbyEntity).damage(arrowDamage / Math.max((2 / effectLevel), 1));
				if (effectLevel >= 3 && arrow.getFireTicks() > 0) {
					((LivingEntity) nearbyEntity).setFireTicks(100);
				}
			}
		}
	}
	
	/**
	 * Electrical enchantment effect, chance to strike lightning at the hit target.
	 * @param arrow arrow that hit the entity
	 * @param entity entity that was hit by arrow
	 */
	private void effectElectrical(Arrow arrow, LivingEntity entity){
		
		int effectLevel = arrow.getMetadata("ELECTRICAL").get(0).asInt();
		
		if (ThreadLocalRandom.current().nextInt(3) < effectLevel) {
			entity.getWorld().strikeLightning(entity.getLocation());
		}
	}
	
	/**
	 * Pierce enchantment effect, causes an arrow to travel through entities.
	 * @param arrow arrow that hit the entity
	 * @param entity entity that was hit by arrow
	 */
	private void effectPierce(Arrow arrow, LivingEntity entity){
		
		int effectLevel = arrow.getMetadata("PIERCE").get(0).asInt();
		
		Arrow newArrow = entity.launchProjectile(Arrow.class, arrow.getVelocity().clone());
		newArrow.setFireTicks(arrow.getFireTicks());
		newArrow.setCritical(arrow.isCritical());
		newArrow.spigot().setDamage(arrow.spigot().getDamage());
		newArrow.setPickupStatus(Arrow.PickupStatus.ALLOWED);
		if (effectLevel >= 2){
			newArrow.setMetadata("PIERCE", new FixedMetadataValue(ArchesporeAdventureMain.getPlugin(), 2));
			newArrow.setPickupStatus(Arrow.PickupStatus.CREATIVE_ONLY);
		}
		
		//?
		if (arrow.getShooter() instanceof LivingEntity) {
			newArrow.setMetadata("TRUEOWNER", new FixedMetadataValue(ArchesporeAdventureMain.getPlugin(), ((LivingEntity)arrow.getShooter()).getUniqueId().toString()));
		}
		
		arrow.remove();
	}
	
	/**
	 * Rain of arrows enchantment effect, causes multiple arrows to rain upon hitting an entity.
	 * @param arrow arrow that hit the entity
	 * @param entity entity that was hit by arrow
	 */
	private void effectRainOfArrows(Arrow arrow, LivingEntity entity){
		
		int effectLevel = arrow.getMetadata("RAINOFARROWS").get(0).asInt();
		double arrowDamage = arrow.spigot().getDamage();
		
		for(int loopValue = 0; loopValue < effectLevel * 3; loopValue++){
			new BukkitRunnable() {
				
				@Override
				public void run() {
					double angle = Math.random() * 360;
					double radius = Math.random() * 2;
					Location ArrowLocation = entity.getLocation().clone().add(Math.cos(angle) * radius, 16, Math.sin(angle) * radius);
					Arrow rainedArrow = arrow.getWorld().spawnArrow(ArrowLocation, new Vector(0, -1, 0), 1.0f, 0);
					rainedArrow.setShooter(arrow.getShooter());
					rainedArrow.setCritical(arrow.isCritical());
					rainedArrow.spigot().setDamage(arrowDamage);
					rainedArrow.setPickupStatus(Arrow.PickupStatus.CREATIVE_ONLY);
					rainedArrow.setMetadata("SKYARROW", new FixedMetadataValue(ArchesporeAdventureMain.getPlugin(), true));
					if (arrow.getShooter() instanceof Player){
						Player player = (Player) arrow.getShooter();
						player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 0.75f, 1.5f);
					}
				}
			}.runTaskLater(ArchesporeAdventureMain.getPlugin(), loopValue * 3);
		}
	}
	
	/**
	 * Slowball effect that applies slowness to an entity.
	 * @param snowball snowball that hit the entity
	 * @param entity entity that was hit by snowball
	 */
	private void effectSlowball(Snowball snowball, LivingEntity entity){
		
		PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.SLOW, 150, 0), entity);
		entity.addPotionEffect(newPotionEffect, true);
		
		ParticleUtil.spawnWorldParticles(Particle.SLIME, entity.getLocation(), 10, .25, .25, .25);
	}
}
