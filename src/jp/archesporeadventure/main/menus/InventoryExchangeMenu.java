package jp.archesporeadventure.main.menus;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import net.md_5.bungee.api.ChatColor;

public class InventoryExchangeMenu extends InventoryMenu {
	
	private final ShapelessRecipe recipe;

	public InventoryExchangeMenu(Inventory inventory, ShapelessRecipe shapelessRecipe) {
		super(inventory);
		recipe = shapelessRecipe;
	}

	public void populateInventory(Player player) {
		int inventorySlot = 0;
		
		for (int loopValue = 0; loopValue < 4; loopValue++) {
			Double stackAmount = Math.pow(4, loopValue);
			inventoryMenu.setItem(inventorySlot, createMenuItem(recipe.getResult().getType(), stackAmount.intValue(), ChatColor.GREEN + "Create " + stackAmount.intValue(), false,
					Arrays.asList(ChatColor.GRAY + "Create this item.")));
			inventorySlot += 2;
		}

		inventoryMenu.setItem(8, createMenuItem(Material.BARRIER, ChatColor.RED + "Close Menu", 
				Arrays.asList(ChatColor.GRAY + "Close the menu.")));
	}

	public void clickActions(Inventory inventory, Player player, ItemStack itemStack) {
		if (itemStack.getType().equals(Material.BARRIER)) {
			player.closeInventory();
		}
		else {
			int exchangeAmount = itemStack.getAmount();
			Inventory playerInventory = player.getInventory();
			for (int loopValue = 0; loopValue < exchangeAmount; loopValue++) {
				boolean hasMaterials = true;
				for (ItemStack ingredient : recipe.getIngredientList()) {
					if (!playerInventory.containsAtLeast(ingredient, 1)) {
						hasMaterials = false;
						player.closeInventory();
						player.sendMessage(ChatColor.RED + "You don't have enough materials to continue...");
						break;
					}
				}
				if (hasMaterials) { 
					playerInventory.removeItem(recipe.getIngredientList().toArray(new ItemStack[recipe.getIngredientList().size()]));
					playerInventory.addItem(recipe.getResult());
				}
				else {
					break;
				}
			}
			player.updateInventory();
			player.closeInventory();
		}
	}

}
