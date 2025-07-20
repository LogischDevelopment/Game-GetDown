package tv.logisch.game.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import tv.logisch.game.manager.GameManager;

import java.util.List;

public class ShopUtils {

    public static ItemStack createShopItem(Material type, String name, String description, int price) {
        ItemStack item = new ItemStack(type);
        item.editMeta(meta -> {
            meta.displayName(Component.text(name));
            meta.lore(List.of(Component.text(description), Component.text("Price: " + price + " coins")));
            meta.getPersistentDataContainer().set(GameManager.get().shopKey(), PersistentDataType.STRING, "item_buy_"+price);
        });
        return item;
    }

}
