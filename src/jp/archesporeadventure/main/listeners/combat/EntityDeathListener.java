package jp.archesporeadventure.main.listeners.combat;

import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.netty.util.internal.ThreadLocalRandom;
import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.abilities.SkillAbility.AbilityActivation;
import jp.archesporeadventure.main.generation.generators.boss.BossGenerator;
import jp.archesporeadventure.main.utils.LivingEntityUtil;
import jp.archesporeadventure.main.utils.MagicalItemsUtil;
import jp.archesporeadventure.main.utils.PotionEffectUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class EntityDeathListener implements Listener {

	@EventHandler
	public void entityDeathEvent(EntityDeathEvent event) {
		
		LivingEntity eventEntity = event.getEntity();
		Player eventKiller = eventEntity.getKiller();
		
		if (LivingEntityUtil.isBleeding(eventEntity)) {
			LivingEntityUtil.removeBleed(eventEntity);
		}
		
		if (eventKiller != null) {
			
			//Lucky Emerald, chance to drop emeralds when killing an entity.
			if (ThreadLocalRandom.current().nextDouble(100) < MagicalItemsUtil.containsMagicItemAmount(Material.EMERALD, eventKiller) * 5.0) {
				eventEntity.getWorld().dropItemNaturally(eventEntity.getLocation(), new ItemStack(Material.EMERALD, 1));
			}
			
			//Knowledge Fragment, gives 33% increased xp.
			if (MagicalItemsUtil.doesContainMagicItem(Material.PAPER, eventKiller)) {
				
				double bonusXP = event.getDroppedExp() / 3.0;
				int bonusXPMultiplier = MagicalItemsUtil.containsMagicItemAmount(Material.PAPER, eventKiller);
				bonusXP = Math.floor(bonusXP * bonusXPMultiplier);
				event.setDroppedExp(event.getDroppedExp() + (int)bonusXP);
			}
			
			//Steroids, gives strength for a short period on a kill.
			if (MagicalItemsUtil.doesContainMagicItem(Material.BLAZE_POWDER, eventKiller)) {
				PotionEffect newPotionEffect = PotionEffectUtil.comparePotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0), eventKiller);
				eventKiller.addPotionEffect(newPotionEffect, true);
			}
			
			//Ball of Slime, gives items to killer instead of dropping them.
			if (MagicalItemsUtil.doesContainMagicItem(Material.SLIME_BALL, eventKiller)) {
				for (ItemStack itemStack : event.getDrops()) {
					if (eventKiller.getInventory().firstEmpty() != -1) { eventKiller.getInventory().addItem(itemStack); }
					else { eventKiller.getWorld().dropItemNaturally(eventKiller.getLocation(), itemStack); }
				}
				event.getDrops().clear();
			}
			
			if (eventEntity.hasPotionEffect(PotionEffectType.LUCK)) {
				eventKiller.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED.toString() + ChatColor.BOLD + "You killed a level " + eventEntity.getPotionEffect(PotionEffectType.LUCK).getAmplifier() + " mob."));
				if (eventEntity instanceof Animals) {
					ArchesporeAdventureMain.getPlayerSkillsController().addPlayerRenown(eventKiller, 1);
				}
				else if (eventEntity instanceof Monster) {
					ArchesporeAdventureMain.getPlayerSkillsController().addPlayerRenown(eventKiller, eventEntity.getPotionEffect(PotionEffectType.LUCK).getAmplifier());
				}
			}
			
			ArchesporeAdventureMain.abilityEvent(AbilityActivation.ENTITY_KILL, event);

		}
		
		BossGenerator bossGenerator = ArchesporeAdventureMain.getBossGenerator();
		if (bossGenerator.isBoss(eventEntity)) { bossGenerator.removeBoss(eventEntity, false); }
	}
}
