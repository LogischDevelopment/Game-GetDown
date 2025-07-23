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
import tv.logisch.game.objects.EffectSetting;
import tv.logisch.game.objects.GameEffect;

import java.util.ArrayList;
import java.util.List;

public class EffectGUI {

    public static List<EffectGUI> guis = new ArrayList<>();
    public static EffectGUI get(Player player) {
        for (EffectGUI gui : guis) {
            if (gui.p.equals(player)) {
                return gui;
            }
        }
        EffectGUI newGui = new EffectGUI(player);
        guis.add(newGui);
        return newGui;
    }
    public static boolean has(Player player) {
        for(EffectGUI gui : guis) {
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

    private EffectGUI(Player player) {
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
        this.inventory.setItem(17, placeholderStack);
        this.inventory.setItem(26, placeholderStack);
        this.inventory.setItem(35, placeholderStack);

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
        ItemStack activeCategory = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        activeCategory.editMeta(m -> m.setHideTooltip(true));
        this.inventory.setItem(10, activeCategory);

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
            m.getPersistentDataContainer().set(GameManager.get().shopKey(), PersistentDataType.STRING, "open_shopping");
        });
        this.inventory.setItem(27, itemStack);
        this.inventory.setItem(28, placeholderStack);


        /* SETTINGS */
        List<GameEffect> effects = EffectSetting.effects;
        List<Integer> fields = new ArrayList<>(List.of(12, 13, 14, 15, 16, 21, 22, 23, 24, 25, 30, 31, 32, 33, 34));
        for(GameEffect effect : effects) {
            ItemStack eItem = new ItemStack(Material.GREEN_DYE, 1);
            if(!effect.enabled()) eItem = eItem.withType(Material.RED_DYE);
            eItem.editMeta(m -> {
                m.displayName(Component.text("§8» §f§l" + effect.type().getKey().getKey()));
                m.lore(List.of(
                        Component.text("§7Click to toggle this effect."),
                        Component.text("§7Current state: " + (effect.enabled() ? "§aEnabled" : "§cDisabled"))
                ));
                m.getPersistentDataContainer().set(GameManager.get().settingKey(), PersistentDataType.STRING, "toggle_effect_" + effect.type().getKey().getKey());
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
        if (!(o instanceof EffectGUI gui)) return false;
        return p.equals(gui.p);
    }

    @Override
    public int hashCode() {
        return ("getdown_settings_gui_" + p.getUniqueId().toString()).hashCode();
    }

}
