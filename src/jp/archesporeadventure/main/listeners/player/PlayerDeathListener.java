package jp.archesporeadventure.main.listeners.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.enchantments.CustomEnchantment;
import jp.archesporeadventure.main.utils.ItemStackUtil;

public class PlayerDeathListener implements Listener{

	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent event) {
		
		List<ItemStack> eventDrops = event.getDrops();
		List<ItemStack> soulboundItems = new ArrayList<>();
		
		for (ItemStack loopItem : eventDrops) {
			if (loopItem != null) {
				if (loopItem.getEnchantments().containsKey(CustomEnchantment.SOULBOUND.getEnchant())) {
					soulboundItems.add(loopItem);
				}
			}
		}
		for (ItemStack loopItem : soulboundItems) {
			eventDrops.remove(loopItem);
			ItemStackUtil.damageItem(loopItem, (int) Math.ceil((loopItem.getType().getMaxDurability() / 2)));

			new BukkitRunnable() {
				
				public void run() {
					event.getEntity().getInventory().addItem(loopItem);

				}
			}.runTask(ArchesporeAdventureMain.getPlugin());
		}
	}
}
