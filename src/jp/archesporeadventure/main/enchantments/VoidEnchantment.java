package jp.archesporeadventure.main.enchantments;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import jp.archesporeadventure.main.utils.ParticleUtil;

public class VoidEnchantment extends SpecialEnchantment {

	public VoidEnchantment(NamespacedKey enchID) {
		super(enchID);
	}
	
	public String getName() {
		return "Void";
	}

	public int getMaxLevel() {
		return 1;
	}
	
	public boolean isPassive() {
		return false;
	}
	
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ALL;
	}

	public boolean enchantmentEffect(Event event) {
		if (event instanceof EntityDamageByEntityEvent) {
			
			EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
			Entity eventDefender = damageByEntityEvent.getEntity();
			
			if (eventDefender instanceof LivingEntity) {
				
				eventDefender.getWorld().playSound(eventDefender.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
				ParticleUtil.spawnWorldParticles(Particle.DRAGON_BREATH, eventDefender.getLocation().add(0, 1, 0), 15, 0, 0, 0, .025);
				eventDefender.teleport(eventDefender.getLocation().add(ThreadLocalRandom.current().nextInt(-3, 4), ThreadLocalRandom.current().nextInt(0, 3), ThreadLocalRandom.current().nextInt(-3, 4)));
				return true;
			}
		}
		return false;
	}
}
