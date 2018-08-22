package jp.archesporeadventure.main.enchantments;

import java.lang.reflect.Field;

import org.bukkit.enchantments.Enchantment;

import jp.archesporeadventure.main.enchantments.CustomEnchantment;

//TODO: Make an easy way to add enchantments to items so that it adds both enchant and the lore required
public class CustomEnchantmentsController {
	
	//Called when the server starts to create our enchantments
	public static void initializeEnchantments() {
		
		try {
			//Get the field that prevents us from adding new enchantments and set it to true;
			Field acceptingNewField = Enchantment.class.getDeclaredField("acceptingNew");
			acceptingNewField.setAccessible(true);
			acceptingNewField.set(null, true);
			
			//Add our enchantments to the system and then update the field again.
			for (CustomEnchantment enchant : CustomEnchantment.values()) {
				Enchantment.registerEnchantment(enchant.getEnchant());
			}
			Enchantment.stopAcceptingRegistrations();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
