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
import tv.logisch.game.enums.GameState;
import tv.logisch.game.manager.GameManager;
import tv.logisch.game.objects.ArrowSetting;
import tv.logisch.game.objects.EffectSetting;
import tv.logisch.game.objects.GameEffect;
import tv.logisch.game.objects.ItemSetting;

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
            String effectKey = action.replaceFirst("toggle_effect_", "");
            GameEffect gE = EffectSetting.effects.stream().filter(gameEffect -> gameEffect.type().getKey().getKey().equalsIgnoreCase(effectKey)).findFirst().orElse(null);
            if(gE == null) return;
            gE.enabled(!gE.enabled());
            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            EffectGUI.guis.forEach(EffectGUI::update);
            return;
        } else if(action.startsWith("toggle_item_")) {
            String itemName = action.replaceFirst("toggle_item_", "");
            ItemSetting.items.stream().filter(gameItem -> gameItem.name().equals(itemName)).forEach(gI -> gI.enabled(!gI.enabled()));
            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            ItemGUI.guis.forEach(ItemGUI::update);
            return;
        } else if(action.startsWith("toggle_arrow_")) {
            String arrowId = action.replaceFirst("toggle_arrow_", "");
            ArrowSetting.arrows.stream().filter(gA -> gA.id().equals(arrowId)).forEach(gA -> gA.enabled(!gA.enabled()));
            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            ItemGUI.guis.forEach(ItemGUI::update);
            return;
        } else if(action.startsWith("add_time")) {
            if(!GameManager.get().state().equals(GameState.WAITING) && !GameManager.get().state().equals(GameState.STARTING) && !GameManager.get().state().equals(GameState.RUNNING)) {
                return;
            }
            if(e.isShiftClick()) {
                GameManager.get().shoppingTime(GameManager.get().shoppingTime()+30);
            } else {
                GameManager.get().shoppingTime(GameManager.get().shoppingTime()+10);
            }
            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            ShoppingGUI.guis.forEach(ShoppingGUI::update);
            return;
        } else if(action.startsWith("remove_time")) {
            if(!GameManager.get().state().equals(GameState.WAITING) && !GameManager.get().state().equals(GameState.STARTING) && !GameManager.get().state().equals(GameState.RUNNING)) {
                return;
            }
            if(e.isShiftClick()) {
                if(GameManager.get().shoppingTime() <= 30) return;
                GameManager.get().shoppingTime(GameManager.get().shoppingTime()-30);
            } else {
                if(GameManager.get().shoppingTime() <= 10) return;
                GameManager.get().shoppingTime(GameManager.get().shoppingTime()-10);
            }
            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            ShoppingGUI.guis.forEach(ShoppingGUI::update);
            return;
        }
    }

}
