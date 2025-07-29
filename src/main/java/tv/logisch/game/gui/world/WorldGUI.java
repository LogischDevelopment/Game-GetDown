package tv.logisch.game.gui.world;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionType;
import tv.logisch.game.GetDown;
import tv.logisch.game.manager.GameManager;
import tv.logisch.game.objects.EffectSetting;
import tv.logisch.game.objects.GameEffect;
import tv.logisch.game.worlds.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class WorldGUI {

    public static List<WorldGUI> guis = new ArrayList<>();
    public static WorldGUI get(Player player) {
        for (WorldGUI gui : guis) {
            if (gui.p.equals(player)) {
                return gui;
            }
        }
        WorldGUI newGui = new WorldGUI(player);
        guis.add(newGui);
        return newGui;
    }
    public static boolean has(Player player) {
        for(WorldGUI gui : guis) {
            if(gui.p.equals(player)) {
                return true;
            }
        }
        return false;
    }

    @Getter
    @Accessors(fluent = true)
    private final Player p;

    @Getter @Accessors(fluent = true)
    private int page;

    private Inventory inventory;

    private WorldGUI(Player player) {
        this.p = player;
        this.page = 1;
        guis.add(this);
    }

    public void open() {
        this.inventory = Bukkit.createInventory(this.p, 27, Component.text(GetDown.instance().prefix() + "Worlds"));
        update();
    }

    public void update() {
        ItemStack placeholderStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        placeholderStack.editMeta(m -> m.setHideTooltip(true));
        for(int i = 0; i < 9; i++) {
            this.inventory.setItem(i, placeholderStack);
        }
        for(int i = 18; i < 27; i++) {
            this.inventory.setItem(i, placeholderStack);
        }

        /* CATEGORIES */
        ItemStack itemStack = new ItemStack(Material.GRASS_BLOCK, 1);
        itemStack.editMeta(m -> {
            m.displayName(Component.text("§8» §f§lWorlds"));
            m.lore(List.of(
                    Component.text("§7Click to change the world.")
            ));
            m.getPersistentDataContainer().set(GameManager.get().worldsKey(), PersistentDataType.STRING, "open_worlds");
        });
        this.inventory.setItem(9, itemStack);
        ItemStack activeCategory = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        activeCategory.editMeta(m -> m.setHideTooltip(true));
        this.inventory.setItem(10, activeCategory);


        /* WORLDS */
        List<WorldObject> worlds = GameManager.get().worldManager().worlds();
        List<Integer> fields = new ArrayList<>(List.of(11, 12, 13, 14, 15, 16, 17));
        for(WorldObject w : worlds) {
            ItemStack eItem = new ItemStack(w.item(), 1);
            eItem.editMeta(m -> {
                String color = GameManager.get().worldName().equals(w.name()) ? "a" : "c";
                m.displayName(Component.text("§8» §"+color+"§l" + w.name()));
                m.lore(List.of(
                        Component.text("§7Click to select this world.")
                ));
                m.getPersistentDataContainer().set(GameManager.get().worldsKey(), PersistentDataType.STRING, "select_world_" + w.name());
            });
            if(!fields.isEmpty()) {
                this.inventory.setItem(fields.getFirst(), eItem);
                fields.removeFirst();
            }
        }


        this.p.openInventory(this.inventory);
    }

    public void close() {
        guis.remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorldGUI gui)) return false;
        return p.equals(gui.p);
    }

    @Override
    public int hashCode() {
        return ("getdown_worlds_gui_" + p.getUniqueId().toString()).hashCode();
    }

}
