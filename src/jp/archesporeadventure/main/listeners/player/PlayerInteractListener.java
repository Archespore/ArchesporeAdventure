package jp.archesporeadventure.main.listeners.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.controllers.LootPoolController;
import jp.archesporeadventure.main.controllers.MagicalItemsController;
import jp.archesporeadventure.main.enchantments.CustomEnchantment;
import jp.archesporeadventure.main.generation.itempools.DefaultPoolFiles;
import jp.archesporeadventure.main.magicscrolls.MagicScrollController;
import jp.archesporeadventure.main.menus.InventoryMenuController;
import jp.archesporeadventure.main.menus.SmeltingMenuInventory;
import jp.archesporeadventure.main.utils.ItemStackUtil;
import jp.archesporeadventure.main.utils.LivingEntityUtil;
import jp.archesporeadventure.main.utils.MagicalItemsUtil;
import jp.archesporeadventure.main.utils.TreasureMapUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerInteractListener implements Listener {

	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent event){
		
		Player player = event.getPlayer();
		Action interactAction = event.getAction();
		ItemStack eventItem = event.getItem();
		
		//TODO: Move and clean
		if (player.hasPotionEffect(PotionEffectType.SLOW) && player.getPotionEffect(PotionEffectType.SLOW).getAmplifier() >= 6) {
			TextComponent message = new TextComponent(ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + "You cannot do that, you are stunned!");
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
			event.setCancelled(true);
			return;
		}
		
		if (interactAction.equals(Action.RIGHT_CLICK_AIR) || interactAction.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.FURNACE)) {
				event.setCancelled(true);
				Inventory smeltingInventory = Bukkit.createInventory(null, 9, "Smelting Menu");
				InventoryMenuController menuController = ArchesporeAdventureMain.getMenuController();
				menuController.registerInventoryMenu(smeltingInventory, new SmeltingMenuInventory());
				menuController.getInventoryMenu(smeltingInventory).populateInventory(player, smeltingInventory);
				player.openInventory(smeltingInventory);
			}
			if (eventItem != null) {
				if (eventItem.getType().isEdible() || eventItem.getType().equals(Material.POTION)) {
					for (ItemStack bowl : player.getInventory().all(Material.BOWL).values()) {
						if (bowl.getEnchantments().containsKey(CustomEnchantment.MAGICAL.getEnchant())) {
							if (player.getFoodLevel() >= 20) {
								player.setFoodLevel(19);
								new BukkitRunnable() {
									
									public void run() {
										player.setFoodLevel(20);
									}
								}.runTask(ArchesporeAdventureMain.getPlugin());
							}
						}
					}
				}
				
				//Skeleton Head, allows you to fire arrows without charging.
				else if (eventItem.getType().equals(Material.BOW)) {
					ItemStack playerHelmet = player.getInventory().getHelmet();
					if (playerHelmet != null && playerHelmet.getType() == Material.SKELETON_SKULL && playerHelmet.getEnchantments().containsKey(CustomEnchantment.MAGICAL.getEnchant())) {
						if (player.getInventory().contains(Material.ARROW)) {
							Arrow newArrow = player.launchProjectile(Arrow.class, event.getPlayer().getLocation().getDirection().multiply(2.75));
							ItemStackUtil.damageItem(eventItem, 1);
							event.getPlayer().getInventory().removeItem(new ItemStack(Material.ARROW, 1));
							newArrow.setCritical(true);
							event.setCancelled(true);
						}
					}
				}
				
				else if (eventItem.getType() == Material.MAP && eventItem.containsEnchantment(Enchantment.LOOT_BONUS_MOBS) && player.getCooldown(Material.MAP) <= 0) {
					if ( (event.getAction() == Action.RIGHT_CLICK_AIR) ||(event.getAction() == Action.RIGHT_CLICK_BLOCK) ) {
						ItemStack treasureMap = event.getItem();
						ItemMeta teasureMapMeta = treasureMap.getItemMeta();
						if (!teasureMapMeta.hasLocalizedName()) {
							ItemStack newTreasureMap = TreasureMapUtil.generateMap(Bukkit.getWorld("ServerWorld"), treasureMap);
							ItemStackUtil.removeAmount(treasureMap, 1);
							player.sendMessage(ChatColor.GREEN + "You unroll the treasure map...");
							if (player.getInventory().firstEmpty() == -1) {
								player.getWorld().dropItem(player.getLocation(), newTreasureMap);
							}
							else {
								player.getInventory().addItem(newTreasureMap);
							}
						}
						else {
							player.setCooldown(Material.MAP, 200);
							String mapCoords = teasureMapMeta.getLocalizedName();
							String[] mapCoordsValues = mapCoords.split(",");
							Location mapLocation = new Location(event.getPlayer().getWorld(), Integer.valueOf(mapCoordsValues[0]), Integer.valueOf(mapCoordsValues[1]), Integer.valueOf(mapCoordsValues[2]));
							event.getPlayer().sendMessage(ChatColor.BLUE + "You are: " + Math.round(event.getPlayer().getLocation().distance(mapLocation)) + " blocks away.");
							if (Math.round(event.getPlayer().getLocation().distance(mapLocation)) <= 5) {
								LootPoolController lootPool = ArchesporeAdventureMain.getLootPoolController();
								String[] allLootPools = lootPool.getLootPoolMap().keySet().toArray(new String[0]);
								ItemStack[] mapLoot = lootPool.getRegisteredLootPool(allLootPools[ThreadLocalRandom.current().nextInt(allLootPools.length)]).generateItems(treasureMap.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS));
								Inventory playerInventory = player.getInventory();
								for (ItemStack loot : mapLoot) {
									if (playerInventory.firstEmpty() > 0) { 
										playerInventory.addItem(loot);
										player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, .25f, 2.0f);
									}
									else { player.getWorld().dropItemNaturally(player.getLocation(), loot); }
								}
								ItemStackUtil.removeAmount(treasureMap, 1);
							}
						}
						event.setCancelled(true);
						event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
					}
				}
				
				else if (eventItem.getType() == Material.CHEST && eventItem.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS) && player.getCooldown(Material.CHEST) <= 0) {
					if ( (event.getAction() == Action.RIGHT_CLICK_AIR) ||(event.getAction() == Action.RIGHT_CLICK_BLOCK) ) {
						List<ItemStack> lootedItems;
						switch (eventItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)) {
							case 1:
								lootedItems = Arrays.asList(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_GOLD").generateItems(1));
								break;
							case 2:
								lootedItems = Arrays.asList(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_DIAMOND").generateItems(1));
								break;
							case 3:
								lootedItems = Arrays.asList(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_EMERALD").generateItems(1));
								break;
							case 4:
								lootedItems = new ArrayList<>();
								for (int loopValue = 0; loopValue < 6; loopValue++) {
									ItemStack generatedMap = new ItemStack(Material.MAP);
									ItemMeta generatedMapMeta = generatedMap.getItemMeta();
									generatedMapMeta.addEnchant(Enchantment.LOOT_BONUS_MOBS, ThreadLocalRandom.current().nextInt(4) + 1, true);
									switch (generatedMapMeta.getEnchantLevel(Enchantment.LOOT_BONUS_MOBS)) {
										case 1:
											generatedMapMeta.setDisplayName(ChatColor.BLUE + "Treasure Map (Easy)");
											break;
										case 2:
											generatedMapMeta.setDisplayName(ChatColor.YELLOW + "Treasure Map (Medium)");
											break;
										case 3:
											generatedMapMeta.setDisplayName(ChatColor.DARK_RED + "Treasure Map (Hard)");
											break;
										case 4:
											generatedMapMeta.setDisplayName(ChatColor.DARK_AQUA + "Treasure Map (Extreme)");
											break;
										default:
											break;
									}
									generatedMapMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
									generatedMap.setItemMeta(generatedMapMeta);
									lootedItems.add(generatedMap);
								}
								break;
							case 5:
								lootedItems = new ArrayList<>();
								MagicalItemsController magicController = ArchesporeAdventureMain.getMagicItemController();
								Set<String> magicalItems = magicController.magicalItemKeys();
								int magicalScrollsAmount = magicController.getRegisteredScrollsAmount();
								lootedItems.add(magicController.generateItem(magicalItems.toArray(new String[magicalItems.size()])[ThreadLocalRandom.current().nextInt(magicalItems.size())], false));
								for (int loopValue = 0; loopValue < 4; loopValue++) {
									lootedItems.add(magicController.generateScroll(ThreadLocalRandom.current().nextInt(magicalScrollsAmount) + 1));
								}
								break;
							case 6:
								lootedItems = new ArrayList<>();
								LootPoolController lootPool = ArchesporeAdventureMain.getLootPoolController();
								String[] allLootPools = lootPool.getLootPoolMap().keySet().toArray(new String[0]);
								for (int loopValue = 0; loopValue < 24; loopValue++) {
									lootedItems.add(lootPool.getRegisteredLootPool(allLootPools[ThreadLocalRandom.current().nextInt(allLootPools.length)]).generateItems(1)[0]);
								}
								break;
							default:
								lootedItems = Arrays.asList(ArchesporeAdventureMain.getLootPoolController().getRegisteredLootPool("DEFAULT_MISC").generateItems(1));
								break;
						}
						Inventory playerInventory = player.getInventory();
						for (ItemStack loot : lootedItems) {
							if (playerInventory.firstEmpty() > 0) { 
								playerInventory.addItem(loot);
								player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, .25f, 2.0f);
							}
							else { player.getWorld().dropItemNaturally(player.getLocation(), loot); }
						}
						player.setCooldown(Material.CHEST, 20);
						event.setCancelled(true);
						event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_CHEST_OPEN, .75f, 1.0f);
						ItemStackUtil.removeAmount(eventItem, 1);
					}
				}
				
				else if (ItemStackUtil.isSpawnEgg(eventItem) && interactAction.equals(Action.RIGHT_CLICK_AIR)) {
					ItemStack mobEgg = event.getItem();
					Snowball spawnProjectile = event.getPlayer().launchProjectile(Snowball.class, event.getPlayer().getLocation().getDirection().multiply(1));
					spawnProjectile.setMetadata("SPAWNEGG", new FixedMetadataValue(ArchesporeAdventureMain.getPlugin(), eventItem.getType().toString().toUpperCase().replace("_SPAWN_EGG", "")));
					mobEgg.setAmount(mobEgg.getAmount() - 1);
					event.setCancelled(true);
				}
				
				else if (eventItem.containsEnchantment(Enchantment.ARROW_INFINITE) && player.isSneaking() && eventItem.getType().equals(Material.NETHER_STAR) && event.getClickedBlock().getType().equals(Material.CHEST)) {
					Chest chest = (Chest) event.getClickedBlock().getState();
					List<ItemStack> containterItems = new ArrayList<>();
					for (ItemStack item : chest.getInventory()) {
						if (item != null) {
							containterItems.add(item);
						}
					}
					ArchesporeAdventureMain.getLootPoolController().getYMLConfig(DefaultPoolFiles.CUSTOM).set("ItemPools", containterItems);
					player.sendMessage(ChatColor.GREEN.toString() + containterItems.size() + " items saved!");
					ArchesporeAdventureMain.getLootPoolController().saveItems();
				}
				
				/*
				else if (eventItem.containsEnchantment(Enchantment.ARROW_INFINITE) && player.isSneaking()) {
					switch (eventItem.getType()) {
						case OAK_PLANKS:
							if (event.getClickedBlock().getType().equals(Material.CHEST)) {
								int itemsCount = 0;
								Chest chest = (Chest) event.getClickedBlock().getState();
								for (ItemStack item : chest.getInventory()) {
									if (item != null) {
										ArchesporeAdventureMain.getPoolController().getWoodPool().addItemsToPool(item);
										itemsCount++;
									}
								}
								ArchesporeAdventureMain.getPoolController().getYMLWood().set("ItemPool", ArchesporeAdventureMain.getPoolController().getWoodPool().getContents());
								player.sendMessage(ChatColor.GREEN.toString() + itemsCount + " items registered!");
							}
							break;
							
						case COAL:
							if (event.getClickedBlock().getType().equals(Material.CHEST)) {
								int itemsCount = 0;
								Chest chest = (Chest) event.getClickedBlock().getState();
								for (ItemStack item : chest.getInventory()) {
									if (item != null) {
										ArchesporeAdventureMain.getPoolController().getCoalPool().addItemsToPool(item);
										itemsCount++;
									}
								}
								ArchesporeAdventureMain.getPoolController().getYMLCoal().set("ItemPool", ArchesporeAdventureMain.getPoolController().getCoalPool().getContents());
								player.sendMessage(ChatColor.GREEN.toString() + itemsCount + " items registered!");
							}
							break;
							
						case IRON_INGOT:
							if (event.getClickedBlock().getType().equals(Material.CHEST)) {
								int itemsCount = 0;
								Chest chest = (Chest) event.getClickedBlock().getState();
								for (ItemStack item : chest.getInventory()) {
									if (item != null) {
										ArchesporeAdventureMain.getPoolController().getIronPool().addItemsToPool(item);
										itemsCount++;
									}
								}
								ArchesporeAdventureMain.getPoolController().getYMLIron().set("ItemPool", ArchesporeAdventureMain.getPoolController().getIronPool().getContents());
								player.sendMessage(ChatColor.GREEN.toString() + itemsCount + " items registered!");
							}
							break;
							
						case LAPIS_LAZULI:
							if (event.getClickedBlock().getType().equals(Material.CHEST)) {
								int itemsCount = 0;
								Chest chest = (Chest) event.getClickedBlock().getState();
								for (ItemStack item : chest.getInventory()) {
									if (item != null) {
										ArchesporeAdventureMain.getPoolController().getLapisPool().addItemsToPool(item);
										itemsCount++;
									}
								}
								ArchesporeAdventureMain.getPoolController().getYMLLapis().set("ItemPool", ArchesporeAdventureMain.getPoolController().getLapisPool().getContents());
								player.sendMessage(ChatColor.GREEN.toString() + itemsCount + " items registered!");
							}
							break;
							
						case REDSTONE:
							if (event.getClickedBlock().getType().equals(Material.CHEST)) {
								int itemsCount = 0;
								Chest chest = (Chest) event.getClickedBlock().getState();
								for (ItemStack item : chest.getInventory()) {
									if (item != null) {
										ArchesporeAdventureMain.getPoolController().getRedstonePool().addItemsToPool(item);
										itemsCount++;
									}
								}
								ArchesporeAdventureMain.getPoolController().getYMLRedstone().set("ItemPool", ArchesporeAdventureMain.getPoolController().getRedstonePool().getContents());
								player.sendMessage(ChatColor.GREEN.toString() + itemsCount + " items registered!");
							}
							break;
							
						case GOLD_INGOT:
							if (event.getClickedBlock().getType().equals(Material.CHEST)) {
								int itemsCount = 0;
								Chest chest = (Chest) event.getClickedBlock().getState();
								for (ItemStack item : chest.getInventory()) {
									if (item != null) {
										ArchesporeAdventureMain.getPoolController().getGoldPool().addItemsToPool(item);
										itemsCount++;
									}
								}
								ArchesporeAdventureMain.getPoolController().getYMLGold().set("ItemPool", ArchesporeAdventureMain.getPoolController().getGoldPool().getContents());
								player.sendMessage(ChatColor.GREEN.toString() + itemsCount + " items registered!");
							}
							break;
							
						case DIAMOND:
							if (event.getClickedBlock().getType().equals(Material.CHEST)) {
								int itemsCount = 0;
								Chest chest = (Chest) event.getClickedBlock().getState();
								for (ItemStack item : chest.getInventory()) {
									if (item != null) {
										ArchesporeAdventureMain.getPoolController().getDiamondPool().addItemsToPool(item);
										itemsCount++;
									}
								}
								ArchesporeAdventureMain.getPoolController().getYMLDiamond().set("ItemPool", ArchesporeAdventureMain.getPoolController().getDiamondPool().getContents());
								player.sendMessage(ChatColor.GREEN.toString() + itemsCount + " items registered!");
							}
							break;
							
						case EMERALD:
							if (event.getClickedBlock().getType().equals(Material.CHEST)) {
								int itemsCount = 0;
								Chest chest = (Chest) event.getClickedBlock().getState();
								for (ItemStack item : chest.getInventory()) {
									if (item != null) {
										ArchesporeAdventureMain.getPoolController().getEmeraldPool().addItemsToPool(item);
										itemsCount++;
									}
								}
								ArchesporeAdventureMain.getPoolController().getYMLEmerald().set("ItemPool", ArchesporeAdventureMain.getPoolController().getEmeraldPool().getContents());
								player.sendMessage(ChatColor.GREEN.toString() + itemsCount + " items registered!");
							}
							break;
							
						default:
							break;
					}
				}
				*/
				
				else {
					if (eventItem.getEnchantments().containsKey(CustomEnchantment.MAGICAL.getEnchant())) {
						
						//First we cancel the event to make sure the player doesn't "use" it by accident.
						event.setCancelled(true);
						
						switch (eventItem.getType()) {
							case GLOWSTONE:
								//Trades gold to gamble
								if (player.getInventory().contains(Material.GOLD_INGOT)) {
									if (player.getLevel() > 0 && player.getCooldown(Material.GLOWSTONE) <= 0){
										
										player.setLevel(player.getLevel() - 1);
										player.setCooldown(Material.GLOWSTONE, 30);
										player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 1));
										
										player.getWorld().playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1.0f, 1.5f);
										new BukkitRunnable() {
											
											int loopValue = 0;
											List<Material> prizes = new ArrayList<>(Arrays.asList(Material.GOLD_NUGGET, Material.IRON_INGOT, Material.COAL, Material.DIRT, Material.DIAMOND));
											
											@Override
											public void run() {
												if (loopValue >= 6) {
													player.getWorld().dropItemNaturally(player.getLocation().add(0, 1, 0), new ItemStack(prizes.get(ThreadLocalRandom.current().nextInt(prizes.size())), 1));
													Bukkit.getScheduler().cancelTask(this.getTaskId());
												}
												else {
													player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, .33f, .75f + (.25f * loopValue));
													loopValue++;
												}
											}
										}.runTaskTimer(ArchesporeAdventureMain.getPlugin(), 2, 4);
									}
								}
								break;
							
							case BLAZE_ROD:
								//Turns iron nuggets to gold nuggets via health and hunger.
								if (player.getInventory().contains(Material.IRON_NUGGET)) {
									
									//How much to damage the player
									double damage = .25;
									
									//One line if statements to help clean up lines.
									if (player.getSaturation() > 0) { player.setSaturation(Math.max(0, player.getSaturation() - 1)); }
									else if (player.getFoodLevel() > 0) { player.setFoodLevel(Math.max(0, player.getFoodLevel() - 1)); }
									else { damage *= 2; }
									
									LivingEntityUtil.removeHealth(player, damage);
									player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, .25f, 2.0f);
									player.getInventory().removeItem(new ItemStack(Material.IRON_NUGGET, 1));
									player.getWorld().dropItemNaturally(player.getLocation().add(0, 1, 0), new ItemStack(Material.GOLD_NUGGET, 1));
								}
								break;
								
							case SHULKER_SHELL:
								//Opens player's enderchest.
								player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, .5f, 1.0f);
								player.openInventory(player.getEnderChest());
								break;
								
							case DANDELION:
								//Removes all potion effects from player in exchange for a level.
								Collection<PotionEffect> potionEffects = player.getActivePotionEffects();
								if (player.getLevel() > 0 && potionEffects.size() > 0){
									player.setLevel(player.getLevel() - 1);
									event.getPlayer().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, .75f, 1.5f);
									potionEffects.forEach(effect -> player.removePotionEffect(effect.getType()));
								}
								break;
								
							case OAK_SAPLING:
								//Trades life and levels in exchange for a buff area.
								if (eventItem.getEnchantments().containsKey(CustomEnchantment.MAGICAL.getEnchant()) && player.getLevel() >= 3 && player.isOnGround()) {
									if (player.getLocation().getBlock().getType().equals(Material.AIR) || player.getLocation().getBlock().getType().equals(Material.GRASS)) {
										player.giveExpLevels(-3);
										LivingEntityUtil.removeHealth(player, 5);
										MagicalItemsUtil.addSapling(player.getLocation().clone(), 30);
										player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, .75f);
										player.getLocation().getBlock().setType(Material.OAK_SAPLING);
									}
								}
								break;
								
							case STICK:
								//Grants the player pseudo-flight for a short period.
								if (player.getCooldown(Material.STICK) <= 0) {
									player.setCooldown(Material.STICK, 300);
									
									new BukkitRunnable() {
										
										int loopValue = 0;
										
										public void run() {
											player.setFallDistance(0);
											if (loopValue >= 10) {
												Bukkit.getScheduler().cancelTask(this.getTaskId());
											}
											else {
												player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, .5f, 1.5f);
												player.setVelocity(player.getLocation().getDirection().multiply(1.5));
												loopValue++;
											}
										}
									}.runTaskTimer(ArchesporeAdventureMain.getPlugin(), 1, 4);
								}
								break;
								
							default:
								break;
						}
						
						//For toggle items, check lore to see if item is enabled or disabled. Little messy, but works.
						//TODO: Maybe clean this up sometime or just look over it again.
						ItemMeta eventItemMeta = eventItem.getItemMeta();
						String eventItemName = eventItemMeta.getDisplayName();
						List<String> eventItemLore = eventItemMeta.getLore();
						
						if (ChatColor.stripColor(eventItemMeta.getLore().get(0)).equals("Enabled")) {
							eventItemLore.set(0, ChatColor.RED + "Disabled");
							eventItemMeta.setLore(eventItemLore);
							eventItemMeta.setDisplayName(eventItemName.split(" - ")[0] + " - Disabled");
							eventItem.setItemMeta(eventItemMeta);
						}
						else if (ChatColor.stripColor(eventItemMeta.getLore().get(0)).equals("Disabled")) {
							eventItemLore.set(0, ChatColor.BLUE + "Enabled");
							eventItemMeta.setLore(eventItemLore);
							eventItemMeta.setDisplayName(eventItemName.split(" - ")[0] + " - Enabled");
							eventItem.setItemMeta(eventItemMeta);
						}
					}

					else if (eventItem.getType().equals(Material.PAPER) && eventItem.containsEnchantment(Enchantment.ARROW_DAMAGE) && player.getCooldown(Material.PAPER) <= 0) {
						
						player.setCooldown(Material.PAPER, 100);
						MagicScrollController.magicalEffect(player, eventItem.getEnchantmentLevel(Enchantment.ARROW_DAMAGE));
						//After the effect, remove an item.
						eventItem.setAmount(eventItem.getAmount() - 1);
					}
				}
			}
		}
	}
}
