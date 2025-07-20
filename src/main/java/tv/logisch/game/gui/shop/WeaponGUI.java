package tv.logisch.game.gui.shop;

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
import tv.logisch.game.utils.ShopUtils;

import java.util.ArrayList;
import java.util.List;

public class WeaponGUI {

    public static List<WeaponGUI> guis = new ArrayList<>();
    public static WeaponGUI get(Player player) {
        for (WeaponGUI gui : guis) {
            if (gui.p.equals(player)) {
                return gui;
            }
        }
        WeaponGUI newGui = new WeaponGUI(player);
        guis.add(newGui);
        return newGui;
    }
    public static boolean has(Player player) {
        for(WeaponGUI gui : guis) {
            if(gui.p.equals(player)) {
                return true;
            }
        }
        return false;
    }

    @Getter @Accessors(fluent = true)
    private final Player p;

    @Getter @Accessors(fluent = true)
    private int page;

    private Inventory inventory;

    private WeaponGUI(Player player) {
        this.p = player;
        this.page = 1;
        guis.add(this);
    }

    public void open() {
        this.inventory = Bukkit.createInventory(this.p, 54, Component.text(GetDown.instance().prefix() + "Shop"));
        update();
    }

    public void update() {
        ItemStack placeholderStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        placeholderStack.editMeta(m -> m.setHideTooltip(true));
        for(int i = 0; i < 9; i++) {
            this.inventory.setItem(i, placeholderStack);
        }
        for(int i = 45; i < 54; i++) {
            this.inventory.setItem(i, placeholderStack);
        }

        /* CATEGORIES */
        ItemStack itemStack = new ItemStack(Material.IRON_SWORD, 1);
        itemStack.editMeta(m -> {
            m.displayName(Component.text("§8» §f§lWeapons"));
            m.lore(List.of(
                Component.text("§7Click to view available weapons.")
            ));
            m.getPersistentDataContainer().set(GameManager.get().shopKey(), PersistentDataType.STRING, "open_weapons");
        });
        this.inventory.setItem(9, itemStack);
        ItemStack activeCategory = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        activeCategory.editMeta(m -> m.setHideTooltip(true));
        this.inventory.setItem(10, activeCategory);

        itemStack = new ItemStack(Material.IRON_CHESTPLATE, 1);
        itemStack.editMeta(m -> {
            m.displayName(Component.text("§8» §f§lArmor"));
            m.lore(List.of(
                Component.text("§7Click to view available armor.")
            ));
            m.getPersistentDataContainer().set(GameManager.get().shopKey(), PersistentDataType.STRING, "open_armor");
        });
        this.inventory.setItem(18, itemStack);
        this.inventory.setItem(19, placeholderStack);

        itemStack = new ItemStack(Material.POTION, 1);
        itemStack.editMeta(m -> {
            m.displayName(Component.text("§8» §f§lUtilities"));
            m.lore(List.of(
                Component.text("§7Click to view available utilities.")
            ));
            m.getPersistentDataContainer().set(GameManager.get().shopKey(), PersistentDataType.STRING, "open_utilities");
        });
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.setBasePotionType(PotionType.HEALING);
        itemStack.setItemMeta(potionMeta);
        this.inventory.setItem(27, itemStack);
        this.inventory.setItem(28, placeholderStack);

        itemStack = new ItemStack(Material.ENCHANTING_TABLE, 1);
        itemStack.editMeta(m -> {
            m.displayName(Component.text("§8» §f§lEnchantments"));
            m.lore(List.of(
                Component.text("§7Click to view available enchantments.")
            ));
            m.getPersistentDataContainer().set(GameManager.get().shopKey(), PersistentDataType.STRING, "open_enchantments");
        });
        this.inventory.setItem(36, itemStack);
        this.inventory.setItem(37, placeholderStack);


        /* ITEMS */
        ItemStack item = ShopUtils.createShopItem(Material.NETHERITE_SWORD, "§8» §6§lNetherite Sword", "§7A powerful sword made of netherite.", 300);
        this.inventory.setItem(12, item);

        item = ShopUtils.createShopItem(Material.DIAMOND_SWORD, "§8» §b§lDiamond Sword", "§7A strong sword made of diamond.", 200);
        this.inventory.setItem(21, item);

        item = ShopUtils.createShopItem(Material.IRON_SWORD, "§8» §7§lIron Sword", "§7A reliable sword made of iron.", 100);
        this.inventory.setItem(30, item);

        item = ShopUtils.createShopItem(Material.SHIELD, "§8» §f§lShield", "§7A sturdy shield to protect yourself.", 250);
        this.inventory.setItem(39, item);

        item = ShopUtils.createShopItem(Material.NETHERITE_AXE, "§8» §6§lNetherite Axe", "§7A powerful axe made of netherite.", 400);
        this.inventory.setItem(14, item);

        item = ShopUtils.createShopItem(Material.DIAMOND_AXE, "§8» §b§lDiamond Axe", "§7A strong axe made of diamond.", 300);
        this.inventory.setItem(23, item);

        item = ShopUtils.createShopItem(Material.STONE_AXE, "§8» §7§lStone Axe", "§7A reliable axe made of stone.", 150);
        this.inventory.setItem(32, item);

        item = ShopUtils.createShopItem(Material.GOLDEN_AXE, "§8» §6§lGolden Axe", "§7A fast but fragile axe made of gold.", 100);
        this.inventory.setItem(41, item);

        item = ShopUtils.createShopItem(Material.CROSSBOW, "§8» §f§lCrossbow", "§7A ranged weapon that can be charged for powerful shots.", 150);
        this.inventory.setItem(16, item);

        item = ShopUtils.createShopItem(Material.BOW, "§8» §b§lBow", "§7A classic ranged weapon for quick shots.", 100);
        this.inventory.setItem(25, item);

        item = ShopUtils.createShopItem(Material.TRIDENT, "§8» §9§lTrident", "§7A versatile weapon that can be thrown or used in melee.", 150);
        this.inventory.setItem(34, item);

        item = ShopUtils.createShopItem(Material.ARROW, "§8» §7§lArrow", "§7A basic projectile for ranged weapons.", 100);
        item.setAmount(16);
        this.inventory.setItem(43, item);


        this.p.openInventory(this.inventory);
    }

    public void close() {
        guis.remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeaponGUI shopGUI)) return false;
        return p.equals(shopGUI.p);
    }

    @Override
    public int hashCode() {
        return ("getdown_shop_gui_" + p.getUniqueId().toString()).hashCode();
    }

}
