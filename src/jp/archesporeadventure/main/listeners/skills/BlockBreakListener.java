package jp.archesporeadventure.main.listeners.skills;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.abilities.SkillAbility.AbilityActivation;
import jp.archesporeadventure.main.skills.SkillType;
import jp.archesporeadventure.main.skills.mining.DepletedOre;
import jp.archesporeadventure.main.skills.mining.MiningSkillController;
import jp.archesporeadventure.main.skills.mining.MiningSkillOre;
import jp.archesporeadventure.main.utils.ItemStackUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class BlockBreakListener implements Listener{

	@EventHandler
	public void blockBreakEvent(BlockBreakEvent event) {
		
		Player player = event.getPlayer();
		Block eventBlock = event.getBlock();
		
		if (!player.getGameMode().equals(GameMode.CREATIVE)) {
			
			if (eventBlock.getType() != Material.LARGE_FERN && eventBlock.getType() != Material.TALL_GRASS) { event.setCancelled(true); }
			PlayerInventory playerInventory = player.getInventory();
			ItemStack playerTool = playerInventory.getItemInMainHand();
			MiningSkillController miningController = (MiningSkillController) ArchesporeAdventureMain.getSkillController(SkillType.MINING);
			if (miningController.doesContainOre(eventBlock.getType())) {
				
				if (eventBlock.getDrops(playerTool).size() > 0) {
					
					MiningSkillOre minedOreInfo = miningController.getMiningOre(eventBlock.getType());
					double harvestChance = minedOreInfo.getChanceForPlayer(player);
					if (harvestChance >= 0) {
						
						ItemStackUtil.damageItem(playerTool, minedOreInfo.getToolDamage());
						if (ThreadLocalRandom.current().nextDouble(100) < harvestChance) {
							
							Material[] itemsToDrop = minedOreInfo.getBlockDrops();
							boolean slotFree = true;
							for (Material drop : itemsToDrop) {
								ItemStack itemDrop = new ItemStack(drop, 1);
								if (playerTool.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
									itemDrop.setAmount(ThreadLocalRandom.current().nextInt(playerTool.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 1));
								}
								
								if (playerInventory.firstEmpty() != -1) {
									playerInventory.addItem(itemDrop);
									slotFree = false;
								}
								else { player.getWorld().dropItemNaturally(player.getLocation(), itemDrop); }
							}
							if (slotFree == false) { player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, .25f, 2.0f); }
							
							ArchesporeAdventureMain.abilityEvent(AbilityActivation.BLOCK_BREAK, event);
							ArchesporeAdventureMain.getPlayerSkillsController().addPlayerEXP(player, SkillType.MINING, minedOreInfo.getXPReward(), true);
							player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "You successfully mined the ore! (" + String.format("%.2f", harvestChance) + ")"));
						}
						else { player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "You failed to mine the ore! (" + String.format("%.2f", harvestChance) + ")")); }
						eventBlock.setType(Material.COBBLESTONE);
						miningController.addDepletedOre(new DepletedOre(eventBlock.getLocation(), minedOreInfo.getOreMaterial(), minedOreInfo.getDefaultRefresh()));
					}
					else { player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_RED + "You are not a high enough level to mine this ore!")); }
				}
				else {
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_RED + "You cannot mine this block with your current tool!"));
				}
			}
		}
	}
}
