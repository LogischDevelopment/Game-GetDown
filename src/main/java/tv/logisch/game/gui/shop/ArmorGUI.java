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

public class ArmorGUI {

    public static List<ArmorGUI> guis = new ArrayList<>();
    public static ArmorGUI get(Player player) {
        for (ArmorGUI gui : guis) {
            if (gui.p.equals(player)) {
                return gui;
            }
        }
        ArmorGUI newGui = new ArmorGUI(player);
        guis.add(newGui);
        return newGui;
    }
    public static boolean has(Player player) {
        for(ArmorGUI gui : guis) {
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

    private ArmorGUI(Player player) {
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
        this.inventory.setItem(10, placeholderStack);

        itemStack = new ItemStack(Material.IRON_CHESTPLATE, 1);
        itemStack.editMeta(m -> {
            m.displayName(Component.text("§8» §f§lArmor"));
            m.lore(List.of(
                Component.text("§7Click to view available armor.")
            ));
            m.getPersistentDataContainer().set(GameManager.get().shopKey(), PersistentDataType.STRING, "open_armor");
        });
        this.inventory.setItem(18, itemStack);
        ItemStack activeCategory = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        activeCategory.editMeta(m -> m.setHideTooltip(true));
        this.inventory.setItem(19, activeCategory);

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
        ItemStack item = ShopUtils.createShopItem(Material.CHAINMAIL_HELMET, "§8» §fChainmail Helmet", "§7A basic helmet for protection.", 100);
        this.inventory.setItem(11, item);

        item = ShopUtils.createShopItem(Material.CHAINMAIL_CHESTPLATE, "§8» §fChainmail Chestplate", "§7A basic chestplate for protection.", 250);
        this.inventory.setItem(20, item);

        item = ShopUtils.createShopItem(Material.CHAINMAIL_LEGGINGS, "§8» §fChainmail Leggings", "§7A basic leggings for protection.", 200);
        this.inventory.setItem(29, item);

        item = ShopUtils.createShopItem(Material.CHAINMAIL_BOOTS, "§8» §fChainmail Boots", "§7A basic boots for protection.", 50);
        this.inventory.setItem(38, item);

        item = ShopUtils.createShopItem(Material.IRON_HELMET, "§8» §fIron Helmet", "§7A sturdy helmet for better protection.", 150);
        this.inventory.setItem(13, item);

        item = ShopUtils.createShopItem(Material.IRON_CHESTPLATE, "§8» §fIron Chestplate", "§7A sturdy chestplate for better protection.", 400);
        this.inventory.setItem(22, item);

        item = ShopUtils.createShopItem(Material.IRON_LEGGINGS, "§8» §fIron Leggings", "§7Sturdy leggings for better protection.", 350);
        this.inventory.setItem(31, item);

        item = ShopUtils.createShopItem(Material.IRON_BOOTS, "§8» §fIron Boots", "§7Sturdy boots for better protection.", 100);
        this.inventory.setItem(40, item);

        item = ShopUtils.createShopItem(Material.DIAMOND_HELMET, "§8» §fDiamond Helmet", "§7A powerful helmet for maximum protection.", 300);
        this.inventory.setItem(15, item);

        item = ShopUtils.createShopItem(Material.DIAMOND_CHESTPLATE, "§8» §fDiamond Chestplate", "§7A powerful chestplate for maximum protection.", 800);
        this.inventory.setItem(24, item);

        item = ShopUtils.createShopItem(Material.DIAMOND_LEGGINGS, "§8» §fDiamond Leggings", "§7Powerful leggings for maximum protection.", 600);
        this.inventory.setItem(33, item);

        item = ShopUtils.createShopItem(Material.DIAMOND_BOOTS, "§8» §fDiamond Boots", "§7Powerful boots for maximum protection.", 300);
        this.inventory.setItem(42, item);

        item = ShopUtils.createShopItem(Material.NETHERITE_HELMET, "§8» §fNetherite Helmet", "§7The ultimate helmet for protection.", 450);
        this.inventory.setItem(17, item);

        item = ShopUtils.createShopItem(Material.NETHERITE_CHESTPLATE, "§8» §fNetherite Chestplate", "§7The ultimate chestplate for protection.", 1200);
        this.inventory.setItem(26, item);

        item = ShopUtils.createShopItem(Material.NETHERITE_LEGGINGS, "§8» §fNetherite Leggings", "§7The ultimate leggings for protection.", 900);
        this.inventory.setItem(35, item);

        item = ShopUtils.createShopItem(Material.NETHERITE_BOOTS, "§8» §fNetherite Boots", "§7The ultimate boots for protection.", 450);
        this.inventory.setItem(44, item);


        this.p.openInventory(this.inventory);
    }

    public void close() {
        guis.remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArmorGUI shopGUI)) return false;
        return p.equals(shopGUI.p);
    }

    @Override
    public int hashCode() {
        return ("getdown_shop_gui_" + p.getUniqueId().toString()).hashCode();
    }

}
