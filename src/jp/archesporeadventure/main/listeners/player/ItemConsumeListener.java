package jp.archesporeadventure.main.listeners.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.enchantments.CustomEnchantment;
import jp.archesporeadventure.main.enchantments.SpecialEnchantment;
import jp.archesporeadventure.main.utils.MagicalItemsUtil;
import jp.archesporeadventure.main.utils.PotionEffectUtil;

public class ItemConsumeListener implements Listener{

	@EventHandler
	public void itemConsumeEvent(PlayerItemConsumeEvent event) {
		
		ItemStack itemConsumed = event.getItem();
		Player player = event.getPlayer();
		
		if (!itemConsumed.getType().equals(Material.POTION)) {
			
			for(Enchantment enchantment : itemConsumed.getEnchantments().keySet()) {
				if (enchantment instanceof SpecialEnchantment) {
					SpecialEnchantment specialEnchantment = (SpecialEnchantment) enchantment;
					if (!specialEnchantment.isPassive()) {
						specialEnchantment.enchantmentEffect(event);
					}
					//If item has magical enchant, return the food to the player.
					if (specialEnchantment.equals(CustomEnchantment.MAGICAL.getEnchant())) {
						new BukkitRunnable() {
							
							@Override
							public void run() {
								ItemStack itemToAdd = itemConsumed.clone();
								itemToAdd.setAmount(1);
								player.getInventory().addItem(itemToAdd);
							}
						}.runTask(ArchesporeAdventureMain.getPlugin());
					}
				}
			}
			
			//Sugar Rush, grants attack speed and resistance upon eating.
			if (MagicalItemsUtil.doesContainMagicItem(Material.SUGAR, player)) {
				
				PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 0), player);
				player.addPotionEffect(newPotionEffect, true);
				
				newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0), player);
				player.addPotionEffect(newPotionEffect, true);
				
			}
		}
		//Mysterious Potion, grants random potion effect upon drinking.
		else if (itemConsumed.getType().equals(Material.POTION) && itemConsumed.getEnchantments().containsKey(CustomEnchantment.MAGICAL.getEnchant())){
			
			List<PotionEffectType> potionEffects = new ArrayList<>(Arrays.asList(PotionEffectType.values()));
			PotionEffectType newEffect = potionEffects.get(ThreadLocalRandom.current().nextInt(potionEffects.size()));
			int amplifier = ThreadLocalRandom.current().nextInt(0, 2);
			int duration = ThreadLocalRandom.current().nextInt(600, 1200);
			if (newEffect.equals(PotionEffectType.HEAL) || newEffect.equals(PotionEffectType.HARM)){
				amplifier = 0;
				duration = duration/2;
			}
			
			PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(newEffect, duration, amplifier), player);
			player.addPotionEffect(newPotionEffect, true);
			event.setCancelled(true);
		}
	}
}
