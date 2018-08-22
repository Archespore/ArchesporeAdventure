package jp.archesporeadventure.main.listeners.skills;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import jp.archesporeadventure.main.ArchesporeAdventureMain;
import jp.archesporeadventure.main.abilities.SkillAbility.AbilityActivation;

public class CraftItemListener implements Listener {

	@EventHandler
	public void craftItemEvent(CraftItemEvent event) {
		ArchesporeAdventureMain.abilityEvent(AbilityActivation.ITEM_CRAFT, event);
	}
}
