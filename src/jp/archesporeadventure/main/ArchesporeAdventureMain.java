package jp.archesporeadventure.main;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import jp.archesporeadventure.main.abilities.SkillAbility.AbilityActivation;
import jp.archesporeadventure.main.commands.CommandAddSkillEXP;
import jp.archesporeadventure.main.commands.CommandAddSkillLevels;
import jp.archesporeadventure.main.commands.CommandCustomItem;
import jp.archesporeadventure.main.commands.CommandEnchantItem;
import jp.archesporeadventure.main.commands.CommandFishing;
import jp.archesporeadventure.main.commands.CommandGiveLootPool;
import jp.archesporeadventure.main.commands.CommandMining;
import jp.archesporeadventure.main.commands.CommandTestGen;
import jp.archesporeadventure.main.commands.CommandTestRemove;
import jp.archesporeadventure.main.commands.CommandWoodcutting;
import jp.archesporeadventure.main.commands.magicitems.CommandGiveAllMagicItems;
import jp.archesporeadventure.main.commands.magicitems.CommandGiveMagicItem;
import jp.archesporeadventure.main.commands.magicitems.CommandRegisterMagicItem;
import jp.archesporeadventure.main.controllers.EquipmentPoolController;
import jp.archesporeadventure.main.controllers.LootPoolController;
import jp.archesporeadventure.main.controllers.MagicalItemsController;
import jp.archesporeadventure.main.enchantments.CustomEnchantmentsController;
import jp.archesporeadventure.main.generation.generators.chests.ChestGenerator;
import jp.archesporeadventure.main.generation.generators.chests.WorldChestGenerator;
import jp.archesporeadventure.main.listeners.combat.DamageByEntityListener;
import jp.archesporeadventure.main.listeners.combat.EntityDamageListener;
import jp.archesporeadventure.main.listeners.combat.EntityDeathListener;
import jp.archesporeadventure.main.listeners.combat.ProjectileHitListener;
import jp.archesporeadventure.main.listeners.combat.ProjectileLaunchListener;
import jp.archesporeadventure.main.listeners.combat.ShootBowListener;
import jp.archesporeadventure.main.listeners.entity.EntityDismountListener;
import jp.archesporeadventure.main.listeners.entity.EntityMountListener;
import jp.archesporeadventure.main.listeners.entity.EntityRegainHealthListener;
import jp.archesporeadventure.main.listeners.entity.EntitySpawnListener;
import jp.archesporeadventure.main.listeners.player.InventoryClickListener;
import jp.archesporeadventure.main.listeners.player.InventoryCloseListener;
import jp.archesporeadventure.main.listeners.player.InventoryOpenListener;
import jp.archesporeadventure.main.listeners.player.ItemConsumeListener;
import jp.archesporeadventure.main.listeners.player.PlayerJoinListener;
import jp.archesporeadventure.main.listeners.player.PlayerTeleportListener;
import jp.archesporeadventure.main.listeners.player.PlayerToggleSneakListener;
import jp.archesporeadventure.main.listeners.skills.BlockBreakListener;
import jp.archesporeadventure.main.listeners.skills.CraftItemListener;
import jp.archesporeadventure.main.listeners.skills.PlayerFishListener;
import jp.archesporeadventure.main.menus.FishingMenuInventory;
import jp.archesporeadventure.main.menus.MiningMenuInventory;
import jp.archesporeadventure.main.skills.PlayerSkillController;
import jp.archesporeadventure.main.skills.SkillController;
import jp.archesporeadventure.main.skills.SkillType;
import jp.archesporeadventure.main.skills.fishing.FishingSkillController;
import jp.archesporeadventure.main.skills.mining.MiningSkillController;
import jp.archesporeadventure.main.listeners.player.PlayerDeathListener;
import jp.archesporeadventure.main.listeners.player.PlayerInteractListener;
import jp.archesporeadventure.main.tasks.TaskController;
import net.md_5.bungee.api.ChatColor;

public class ArchesporeAdventureMain extends JavaPlugin {
	
	private static TaskController timeController;
	
	private static MagicalItemsController magicItemController = new MagicalItemsController();
	private static LootPoolController lootPoolController;
	private static EquipmentPoolController equipmentPoolController;
	private static PlayerSkillController playerSkillsController;
	private static Map<World, WorldChestGenerator> openWorldChestGeneratorMap = new HashMap<>();
	private static Map<SkillType, SkillController> skillControllerMap = new HashMap<>();
	
	private static MiningMenuInventory miningMenu;
	private static FishingMenuInventory fishingMenu;
	
	public void onEnable(){
		
		getServer().getPluginManager().registerEvents(new DamageByEntityListener(), this);
		getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
		getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
		getServer().getPluginManager().registerEvents(new ShootBowListener(), this);
		getServer().getPluginManager().registerEvents(new ProjectileHitListener(), this);
		getServer().getPluginManager().registerEvents(new ProjectileLaunchListener(), this);
		getServer().getPluginManager().registerEvents(new ItemConsumeListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
		getServer().getPluginManager().registerEvents(new EntityMountListener(), this);
		getServer().getPluginManager().registerEvents(new EntityDismountListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerToggleSneakListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerTeleportListener(), this);
		getServer().getPluginManager().registerEvents(new EntityRegainHealthListener(), this);
		getServer().getPluginManager().registerEvents(new EntitySpawnListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryOpenListener(), this);
		getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerFishListener(), this);
		getServer().getPluginManager().registerEvents(new CraftItemListener(), this);
		this.getCommand("enchantitem").setExecutor(new CommandEnchantItem());
		this.getCommand("givemagicitem").setExecutor(new CommandGiveMagicItem());
		this.getCommand("givemagicitemsall").setExecutor(new CommandGiveAllMagicItems());
		this.getCommand("registermagicitem").setExecutor(new CommandRegisterMagicItem());
		this.getCommand("testgen").setExecutor(new CommandTestGen());
		this.getCommand("removegen").setExecutor(new CommandTestRemove());
		this.getCommand("givelootpool").setExecutor(new CommandGiveLootPool());
		this.getCommand("addskilllevels").setExecutor(new CommandAddSkillLevels());
		this.getCommand("addskillexp").setExecutor(new CommandAddSkillEXP());
		this.getCommand("mining").setExecutor(new CommandMining());
		this.getCommand("woodcutting").setExecutor(new CommandWoodcutting());
		this.getCommand("herbalism").setExecutor(new CommandMining());
		this.getCommand("enchanting").setExecutor(new CommandMining());
		this.getCommand("fishing").setExecutor(new CommandFishing());
		this.getCommand("customitem").setExecutor(new CommandCustomItem());
		
		timeController = new TaskController(true);
		
		CustomEnchantmentsController.initializeEnchantments();
		magicItemController.loadMagicItems();
		lootPoolController = new LootPoolController(this);
		equipmentPoolController = new EquipmentPoolController(this);
		
		skillControllerMap.put(SkillType.MINING, new MiningSkillController(this));
		skillControllerMap.put(SkillType.FISHING, new FishingSkillController(this));
		
		playerSkillsController = new PlayerSkillController(this);
		
		miningMenu = new MiningMenuInventory();
		fishingMenu = new FishingMenuInventory();
		
		openWorldChestGeneratorMap.put(Bukkit.getWorld("ServerWorld"), new WorldChestGenerator());
		
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "ArchesporeAdventure is enabled!");
	}
	
	public void onDisable(){
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "ArchesporeAdventure is disabled!");
		magicItemController.saveMagicItems();
		lootPoolController.saveItems();
		equipmentPoolController.saveItems();
		((MiningSkillController)skillControllerMap.get(SkillType.MINING)).resetOres();
		playerSkillsController.saveStats();
		for (ChestGenerator generator : openWorldChestGeneratorMap.values()) {
			generator.removeAllGeneratedChests();
		}
	}
	
	/**
	 * Gets this plugin.
	 * @return returns this plugin.
	 */
	public static ArchesporeAdventureMain getPlugin() {
		return JavaPlugin.getPlugin(ArchesporeAdventureMain.class);
	}
	
	public static void abilityEvent(AbilityActivation eventType, Event event) {
		for (SkillController skillController : skillControllerMap.values()) {
			skillController.abilityEvent(eventType, event);
		}
	}
	
	/**
	 * Gets the TaskController for this plugin.
	 */
	public static TaskController getTaskController() {
		return timeController;
	}
	
	public static MiningMenuInventory getMiningMenu() {
		return miningMenu;
	}
	
	public static FishingMenuInventory getFishingMenu() {
		return fishingMenu;
	}
	
	public static SkillController getSkillController(SkillType skillType) {
		return skillControllerMap.get(skillType);
	}
	
	/**
	 * Gets the MagicItemController for this plugin.
	 */
	public static MagicalItemsController getMagicItemController() {
		return magicItemController;
	}
	
	public static LootPoolController getLootPoolController() {
		return lootPoolController;
	}
	
	public static EquipmentPoolController getEquipmentPoolController() {
		return equipmentPoolController;
	}
	
	public static PlayerSkillController getPlayerSkillsController() {
		return playerSkillsController;
	}
	
	public static WorldChestGenerator getChestGenerator(World world) {
		return openWorldChestGeneratorMap.get(world);
	}
}
