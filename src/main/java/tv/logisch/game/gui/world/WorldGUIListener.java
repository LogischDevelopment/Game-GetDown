package tv.logisch.game.gui.world;

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
import tv.logisch.game.worlds.ColorObject;
import tv.logisch.game.worlds.WorldObject;

public class WorldGUIListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player player)) return;
        if(!PlainTextComponentSerializer.plainText().serialize(e.getView().title()).equals(GetDown.instance().prefix() + "Worlds")) return;
        Bukkit.getScheduler().runTaskLaterAsynchronously(GetDown.instance(), () -> {
            if (player.getOpenInventory().getTopInventory().getType().equals(InventoryType.CRAFTING)) {
                if (WorldGUI.has(player)) {
                    WorldGUI gui = WorldGUI.get(player);
                    gui.close();
                }
            }
        }, 1L);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player p)) return;
        if(e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;
        if(!PlainTextComponentSerializer.plainText().serialize(e.getView().title()).equals(GetDown.instance().prefix() + "Worlds")) return;
        if(!WorldGUI.has(p)) return;

        e.setCancelled(true);
        ItemStack clicked = e.getCurrentItem();
        NamespacedKey key = GameManager.get().worldsKey();
        if(clicked.getItemMeta() == null || !clicked.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            return;
        }

        String action = clicked.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if(action == null) return;

        if(action.equalsIgnoreCase("open_worlds")) {
            WorldGUI.get(p).open();
            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
            return;
        } else if(action.equals("open_colors")) {
            ColorGUI.get(p).open();
            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
            return;
        }

        if(action.startsWith("select_world_")) {
            if(!GameManager.get().state().equals(GameState.WAITING)) {
                p.sendMessage(GetDown.instance().prefix() + "§cYou can only select a world when the game is waiting.");
                return;
            }
            String worldName = action.replaceFirst("select_world_", "");
            WorldObject world = GameManager.get().worldManager().worlds().stream().filter(w -> w.name().equals(worldName)).findFirst().orElse(null);
            if(world == null) return;
            GameManager.get().worldName(world.name());
            if(!world.colors().isEmpty()) {
                GameManager.get().colorNames().clear();
                GameManager.get().colorNames().addAll(world.colors());
            }
            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            WorldGUI.guis.forEach(WorldGUI::update);
            return;
        } else if(action.startsWith("select_color_")) {
            if(!GameManager.get().state().equals(GameState.WAITING)) {
                p.sendMessage(GetDown.instance().prefix() + "§cYou can only select a color when the game is waiting.");
                return;
            }
            String colorName = action.replaceFirst("select_color_", "");
            ColorObject color = GameManager.get().worldManager().colors().stream()
                    .filter(c -> c.name().equals(colorName))
                    .findFirst()
                    .orElse(null);
            if(color == null) return;
            if(GameManager.get().colorNames().contains(color.name())) {
                if(GameManager.get().colorNames().size() <= 1) return;
                GameManager.get().colorNames().remove(color.name());
                p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                ColorGUI.guis.forEach(ColorGUI::update);
            } else {
                GameManager.get().worldManager().worlds().stream()
                        .filter(w -> w.name().equals(GameManager.get().worldName()))
                        .filter(w -> w.colors().contains(color.name()))
                        .findAny()
                        .ifPresent(worldObject -> {
                            GameManager.get().colorNames().add(color.name());
                            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                            ColorGUI.guis.forEach(ColorGUI::update);
                        });
            }
        }
    }

}
