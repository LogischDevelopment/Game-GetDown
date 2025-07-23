package tv.logisch.game.gui.settings;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import tv.logisch.game.GetDown;
import tv.logisch.game.manager.GameManager;

public class SettingGUIListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player player)) return;
        if(!PlainTextComponentSerializer.plainText().serialize(e.getView().title()).equals(GetDown.instance().prefix() + "Settings")) return;
        Bukkit.getScheduler().runTaskLaterAsynchronously(GetDown.instance(), () -> {
            if (player.getOpenInventory().getTopInventory().getType().equals(InventoryType.CRAFTING)) {
                if (EffectGUI.has(player)) {
                    EffectGUI gui = EffectGUI.get(player);
                    gui.close();
                } else if (ItemGUI.has(player)) {
                    ItemGUI gui = ItemGUI.get(player);
                    gui.close();
                } else if (ShoppingGUI.has(player)) {
                    ShoppingGUI gui = ShoppingGUI.get(player);
                    gui.close();
                }
            }
        }, 1L);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof org.bukkit.entity.Player p)) return;
        if(e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;
        if(!PlainTextComponentSerializer.plainText().serialize(e.getView().title()).equals(GetDown.instance().prefix() + "Settings")) return;
        if(!EffectGUI.has(p) && !ItemGUI.has(p) && !ShoppingGUI.has(p)) return;

        e.setCancelled(true);
        ItemStack clicked = e.getCurrentItem();
        NamespacedKey key = GameManager.get().settingKey();
        if(clicked.getItemMeta() == null || !clicked.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            return;
        }

        String action = clicked.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if(action == null) return;

        if(action.equalsIgnoreCase("open_effects")) {
            EffectGUI.get(p).open();
            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
            return;
        } else if(action.equalsIgnoreCase("open_items")) {
            ItemGUI.get(p).open();
            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
            return;
        } else if(action.equalsIgnoreCase("open_shopping")) {
            ShoppingGUI.get(p).open();
            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
            return;
        }

        if(action.startsWith("toggle_effect_")) {
            // TODO: Implement
        } else if(action.startsWith("toggle_item_")) {
            // TODO: Implement
        } else if(action.startsWith("toggle_arrow_")) {
            // TODO: Implement
        } else if(action.startsWith("add_time")) {
            // TODO: Implement
        } else if(action.startsWith("remove_time")) {
            // TODO: Implement
        }
    }

}
