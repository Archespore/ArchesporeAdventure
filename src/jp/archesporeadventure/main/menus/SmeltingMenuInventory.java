package jp.archesporeadventure.main.menus;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
		switch(itemMaterial) {
		case STONE:
			Inventory exchangeMenu = Bukkit.createInventory(null, 9, "Select Amount");
			ShapelessRecipe smeltingStone = new ShapelessRecipe(new NamespacedKey(ArchesporeAdventureMain.getPlugin(), "Smelt_Stone"), new ItemStack(Material.STONE, 1))
					.addIngredient(Material.COBBLESTONE)
					.addIngredient(Material.COAL);
			InventoryMenuController menuController = ArchesporeAdventureMain.getMenuController();
			menuController.registerInventoryMenu(exchangeMenu, new InventoryExchangeMenu(smeltingStone));
			menuController.getInventoryMenu(exchangeMenu).populateInventory(player, exchangeMenu);
			player.openInventory(exchangeMenu);
			break;
		case IRON_ORE:
			player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 1));
			break;
		case LAPIS_ORE:
			player.getInventory().addItem(new ItemStack(Material.LAPIS_LAZULI, 1));
			break;
		case REDSTONE_ORE:
			player.getInventory().addItem(new ItemStack(Material.REDSTONE, 1));
			break;
		case GOLD_ORE:
			player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 1));
			break;
		case EMERALD_ORE:
			player.getInventory().addItem(new ItemStack(Material.EMERALD, 1));
			break;
		case DIAMOND_ORE:
			player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
			break;
		case BARRIER:
			player.closeInventory();
			break;
		default:
			break;
		}
	}

}
