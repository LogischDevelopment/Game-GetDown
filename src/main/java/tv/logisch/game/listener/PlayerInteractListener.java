package tv.logisch.game.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.view.AnvilView;
import org.bukkit.persistence.PersistentDataType;
import tv.logisch.game.gui.shop.WeaponGUI;
import tv.logisch.game.manager.GameManager;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(!e.getAction().isRightClick()) return;
        if(e.getItem() == null) return;
        if(!e.getItem().getPersistentDataContainer().has(GameManager.get().shopKey())) return;

        String s = e.getItem().getPersistentDataContainer().get(GameManager.get().shopKey(), PersistentDataType.STRING);
        if(s == null || s.isEmpty()) return;

        if(s.equalsIgnoreCase("open")) {
            WeaponGUI.get(e.getPlayer()).open();
            return;
        }
        if(s.equalsIgnoreCase("anvil")) {
            AnvilView anvilView = MenuType.ANVIL.create(e.getPlayer());
            e.getPlayer().openInventory(anvilView);
            return;
        }

    }

}
