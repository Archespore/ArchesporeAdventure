package jp.archesporeadventure.main.menus;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.skills.SkillType;
import jp.archesporeadventure.main.skills.mining.MiningSkillController;
import net.md_5.bungee.api.ChatColor;

public class SmeltingMenuInventory implements InventoryMenu {
	
	public void populateInventory(Player player, Inventory inventory) {
		int inventorySlot = 0;
		MiningSkillController miningController = (MiningSkillController) ArchesporeAdventureMain.getSkillController(SkillType.MINING);
		
		List<Material> registeredOres = miningController.getRegisteredOres();
		registeredOres.removeAll(Arrays.asList(Material.COAL_ORE, Material.OBSIDIAN));
		for (Material smeltingOre : registeredOres) {
			inventory.setItem(inventorySlot, createMenuItem(smeltingOre, miningController.getMiningOre(smeltingOre).getDisplayName()));
			inventorySlot++;
		}

		inventory.setItem(8, createMenuItem(Material.BARRIER, ChatColor.RED + "Close Menu", 
				Arrays.asList(ChatColor.GRAY + "Close the menu.")));
	}

	public void clickActions(Inventory inventory, Player player, ItemStack itemStack) {
		Material itemMaterial = itemStack.getType();
		InventoryMenuController menuController = ArchesporeAdventureMain.getMenuController();
		Inventory exchangeMenu;
		switch(itemMaterial) {
		case STONE:
			exchangeMenu = Bukkit.createInventory(null, 9, "Select Amount");
			ShapelessRecipe smeltingStone = ArchesporeAdventureMain.getFurnaceController().getSmeltingRecipe("SMELT_STONE");
			menuController.registerInventoryMenu(exchangeMenu, new InventoryExchangeMenu(smeltingStone));
			menuController.getInventoryMenu(exchangeMenu).populateInventory(player, exchangeMenu);
			player.openInventory(exchangeMenu);
			break;
		case IRON_ORE:
			exchangeMenu = Bukkit.createInventory(null, 9, "Select Amount");
			ShapelessRecipe smeltingIron = ArchesporeAdventureMain.getFurnaceController().getSmeltingRecipe("SMELT_IRON");
			menuController.registerInventoryMenu(exchangeMenu, new InventoryExchangeMenu(smeltingIron));
			menuController.getInventoryMenu(exchangeMenu).populateInventory(player, exchangeMenu);
			player.openInventory(exchangeMenu);
			break;
		case LAPIS_ORE:
			exchangeMenu = Bukkit.createInventory(null, 9, "Select Amount");
			ShapelessRecipe smeltingLapis = ArchesporeAdventureMain.getFurnaceController().getSmeltingRecipe("SMELT_LAPIS");
			menuController.registerInventoryMenu(exchangeMenu, new InventoryExchangeMenu(smeltingLapis));
			menuController.getInventoryMenu(exchangeMenu).populateInventory(player, exchangeMenu);
			player.openInventory(exchangeMenu);
			break;
		case REDSTONE_ORE:
			exchangeMenu = Bukkit.createInventory(null, 9, "Select Amount");
			ShapelessRecipe smeltingRedstone = ArchesporeAdventureMain.getFurnaceController().getSmeltingRecipe("SMELT_REDSTONE");
			menuController.registerInventoryMenu(exchangeMenu, new InventoryExchangeMenu(smeltingRedstone));
			menuController.getInventoryMenu(exchangeMenu).populateInventory(player, exchangeMenu);
			player.openInventory(exchangeMenu);
			break;
		case GOLD_ORE:
			exchangeMenu = Bukkit.createInventory(null, 9, "Select Amount");
			ShapelessRecipe smeltingGold = ArchesporeAdventureMain.getFurnaceController().getSmeltingRecipe("SMELT_GOLD");
			menuController.registerInventoryMenu(exchangeMenu, new InventoryExchangeMenu(smeltingGold));
			menuController.getInventoryMenu(exchangeMenu).populateInventory(player, exchangeMenu);
			player.openInventory(exchangeMenu);
			break;
		case EMERALD_ORE:
			exchangeMenu = Bukkit.createInventory(null, 9, "Select Amount");
			ShapelessRecipe smeltingEmerald = ArchesporeAdventureMain.getFurnaceController().getSmeltingRecipe("SMELT_EMERALD");
			menuController.registerInventoryMenu(exchangeMenu, new InventoryExchangeMenu(smeltingEmerald));
			menuController.getInventoryMenu(exchangeMenu).populateInventory(player, exchangeMenu);
			player.openInventory(exchangeMenu);
			break;
		case DIAMOND_ORE:
			exchangeMenu = Bukkit.createInventory(null, 9, "Select Amount");
			ShapelessRecipe smeltingDiamond = ArchesporeAdventureMain.getFurnaceController().getSmeltingRecipe("SMELT_DIAMOND");
			menuController.registerInventoryMenu(exchangeMenu, new InventoryExchangeMenu(smeltingDiamond));
			menuController.getInventoryMenu(exchangeMenu).populateInventory(player, exchangeMenu);
			player.openInventory(exchangeMenu);
			break;
		case BARRIER:
			player.closeInventory();
			break;
		default:
			break;
		}
	}

}
