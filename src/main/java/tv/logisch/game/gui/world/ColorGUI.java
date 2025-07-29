package tv.logisch.game.gui.world;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import tv.logisch.game.GetDown;
import tv.logisch.game.manager.GameManager;
import tv.logisch.game.worlds.ColorObject;

import java.util.ArrayList;
import java.util.List;

public class ColorGUI {

    public static List<ColorGUI> guis = new ArrayList<>();
    public static ColorGUI get(Player player) {
        for (ColorGUI gui : guis) {
            if (gui.p.equals(player)) {
                return gui;
            }
        }
        ColorGUI newGui = new ColorGUI(player);
        guis.add(newGui);
        return newGui;
    }
    public static boolean has(Player player) {
        for(ColorGUI gui : guis) {
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

    private ColorGUI(Player player) {
        this.p = player;
        this.page = 1;
        guis.add(this);
    }

    public void open() {
        this.inventory = Bukkit.createInventory(this.p, 36, Component.text(GetDown.instance().prefix() + "Worlds"));
        update();
    }

    public void update() {
        ItemStack placeholderStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        placeholderStack.editMeta(m -> m.setHideTooltip(true));
        for(int i = 0; i < 9; i++) {
            this.inventory.setItem(i, placeholderStack);
        }
        for(int i = 27; i < 36; i++) {
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
        this.inventory.setItem(10, placeholderStack);

        itemStack = new ItemStack(Material.LIGHT_BLUE_DYE, 1);
        itemStack.editMeta(m -> {
            m.displayName(Component.text("§8» §f§lColors"));
            m.lore(List.of(
                    Component.text("§7Click to change the color of the world.")
            ));
            m.getPersistentDataContainer().set(GameManager.get().worldsKey(), PersistentDataType.STRING, "open_colors");
        });
        this.inventory.setItem(18, itemStack);
        ItemStack activeCategory = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        activeCategory.editMeta(m -> m.setHideTooltip(true));
        this.inventory.setItem(19, activeCategory);


        /* WORLDS */
        List<ColorObject> colors = GameManager.get().worldManager().colors();
        List<Integer> fields = new ArrayList<>(List.of(11, 12, 13, 14, 15, 16, 17));
        for(ColorObject c : colors) {
            ItemStack eItem = new ItemStack(c.item(), 1);
            eItem.editMeta(m -> {
                String color = GameManager.get().colorNames().contains(c.name()) ? "a" : "c";
                m.displayName(Component.text("§8» §"+color+"§l" + c.name()));
                m.lore(List.of(
                        Component.text("§7Click to select this color.")
                ));
                m.getPersistentDataContainer().set(GameManager.get().worldsKey(), PersistentDataType.STRING, "select_color_" + c.name());
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
        if (!(o instanceof ColorGUI gui)) return false;
        return p.equals(gui.p);
    }

    @Override
    public int hashCode() {
        return ("getdown_worlds_gui_" + p.getUniqueId().toString()).hashCode();
    }

}
