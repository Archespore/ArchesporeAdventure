package jp.archesporeadventure.main.enchantments;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import jp.archesporeadventure.main.utils.ParticleUtil;

public class ParticleEnchantment extends SpecialEnchantment {
	
	public ParticleEnchantment(NamespacedKey enchID) {
		super(enchID);
	}

	public String getName() {
		return "Particle";
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
			ParticleUtil.spawnWorldParticles(Particle.REDSTONE, damageByEntityEvent.getEntity().getLocation().add(0, 1, 0), 25, .5, .5, .5, 0, new Particle.DustOptions(Color.fromRGB(ThreadLocalRandom.current().nextInt(256), ThreadLocalRandom.current().nextInt(256), ThreadLocalRandom.current().nextInt(256)), .75f));
			return true;
		}
		return false;
	}
}
