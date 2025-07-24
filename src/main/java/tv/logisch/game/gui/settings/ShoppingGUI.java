package tv.logisch.game.gui.settings;

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
import tv.logisch.game.utils.Format;

import java.util.ArrayList;
import java.util.List;

public class ShoppingGUI {

    public static List<ShoppingGUI> guis = new ArrayList<>();
    public static ShoppingGUI get(Player player) {
        for (ShoppingGUI gui : guis) {
            if (gui.p.equals(player)) {
                return gui;
            }
        }
        ShoppingGUI newGui = new ShoppingGUI(player);
        guis.add(newGui);
        return newGui;
    }
    public static boolean has(Player player) {
        for(ShoppingGUI gui : guis) {
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

    private ShoppingGUI(Player player) {
        this.p = player;
        this.page = 1;
        guis.add(this);
    }

    public void open() {
        this.inventory = Bukkit.createInventory(this.p, 45, Component.text(GetDown.instance().prefix() + "Settings"));
        update();
    }

    public void update() {
        ItemStack placeholderStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        placeholderStack.editMeta(m -> m.setHideTooltip(true));
        for(int i = 0; i < 9; i++) {
            this.inventory.setItem(i, placeholderStack);
        }
        for(int i = 36; i < 45; i++) {
            this.inventory.setItem(i, placeholderStack);
        }

        /* CATEGORIES */
        ItemStack itemStack = new ItemStack(Material.POTION, 1);
        itemStack.editMeta(m -> {
            m.displayName(Component.text("§8» §f§lEffects"));
            m.lore(List.of(
                    Component.text("§7Click to setup the effects")
            ));
            m.getPersistentDataContainer().set(GameManager.get().settingKey(), PersistentDataType.STRING, "open_effects");
        });
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.setBasePotionType(PotionType.FIRE_RESISTANCE);
        itemStack.setItemMeta(potionMeta);
        this.inventory.setItem(9, itemStack);
        this.inventory.setItem(10, placeholderStack);

        itemStack = new ItemStack(Material.CROSSBOW, 1);
        itemStack.editMeta(m -> {
            m.displayName(Component.text("§8» §f§lItems"));
            m.lore(List.of(
                    Component.text("§7Click to setup the items")
            ));
            m.getPersistentDataContainer().set(GameManager.get().settingKey(), PersistentDataType.STRING, "open_items");
        });
        this.inventory.setItem(18, itemStack);
        this.inventory.setItem(19, placeholderStack);

        itemStack = new ItemStack(Material.CLOCK, 1);
        itemStack.editMeta(m -> {
            m.displayName(Component.text("§8» §f§lShopping Time"));
            m.lore(List.of(
                    Component.text("§7Click to change the shopping time")
            ));
            m.getPersistentDataContainer().set(GameManager.get().settingKey(), PersistentDataType.STRING, "open_shopping");
        });
        this.inventory.setItem(27, itemStack);
        ItemStack activeCategory = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        activeCategory.editMeta(m -> m.setHideTooltip(true));
        this.inventory.setItem(28, activeCategory);


        /* SETTINGS */
        int addTime = 25;
        int removeTime = 21;

        ItemStack clock = new ItemStack(Material.CLOCK, 1);
        clock.editMeta(m -> {
            m.displayName(Component.text("§8» §f§l Shopping Time"));
            m.lore(List.of(
                    Component.text("§7Current time: §f" + Format.time(GameManager.get().shoppingTime()))
            ));
        });
        this.inventory.setItem(23, clock);

        ItemStack addItem = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
        addItem.editMeta(m -> {
            m.displayName(Component.text("§8» §f§l Add Time"));
            m.lore(List.of(
                    Component.text("§7Click to add 10 seconds to the shopping time."),
                    Component.text("§7Shift + Click to add 30 seconds."),
                    Component.text("§7Current time: §f" + GameManager.get().shoppingTime() + " seconds")
            ));
            m.getPersistentDataContainer().set(GameManager.get().settingKey(), PersistentDataType.STRING, "add_time");
        });
        this.inventory.setItem(addTime, addItem);

        ItemStack removeItem = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        removeItem.editMeta(m -> {
            m.displayName(Component.text("§8» §f§l Remove Time"));
            m.lore(List.of(
                    Component.text("§7Click to remove 10 seconds to the shopping time."),
                    Component.text("§7Shift + Click to remove 30 seconds."),
                    Component.text("§7Current time: §f" + GameManager.get().shoppingTime() + " seconds")
            ));
            m.getPersistentDataContainer().set(GameManager.get().settingKey(), PersistentDataType.STRING, "remove_time");
        });
        this.inventory.setItem(removeTime, removeItem);

        this.p.openInventory(this.inventory);
    }

    public void close() {
        guis.remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShoppingGUI gui)) return false;
        return p.equals(gui.p);
    }

    @Override
    public int hashCode() {
        return ("getdown_settings_gui_" + p.getUniqueId().toString()).hashCode();
    }

}
