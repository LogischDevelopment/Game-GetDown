package tv.logisch.game.gui.shop;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionType;
import tv.logisch.game.GetDown;
import tv.logisch.game.manager.GameManager;
import tv.logisch.game.utils.ShopUtils;

import java.util.ArrayList;
import java.util.List;

public class EnchantingGUI {

    public static List<EnchantingGUI> guis = new ArrayList<>();
    public static EnchantingGUI get(Player player) {
        for (EnchantingGUI gui : guis) {
            if (gui.p.equals(player)) {
                return gui;
            }
        }
        EnchantingGUI newGui = new EnchantingGUI(player);
        guis.add(newGui);
        return newGui;
    }
    public static boolean has(Player player) {
        for(EnchantingGUI gui : guis) {
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

    private EnchantingGUI(Player player) {
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
        ItemStack activeCategory = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        activeCategory.editMeta(m -> m.setHideTooltip(true));
        this.inventory.setItem(37, activeCategory);


        /* ITEMS */

        ItemStack stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lBlast Protection I", "§7Grants protection against explosions.", 150);
        EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.BLAST_PROTECTION, 1, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(11, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lBlast Protection II", "§7Grants protection against explosions.", 250);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.BLAST_PROTECTION, 2, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(12, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lThorns I", "§7Grants a chance to damage attackers.", 150);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.THORNS, 1, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(13, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lDepth Strider III", "§7Increases underwater movement speed.", 300);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.DEPTH_STRIDER, 3, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(14, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lFire Aspect I", "§7Sets targets on fire.", 150);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.FIRE_ASPECT, 1, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(15, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lFire Aspect II", "§7Sets targets on fire.", 250);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.FIRE_ASPECT, 2, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(16, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lKnockback II", "§7Increases knockback dealt to targets.", 200);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.KNOCKBACK, 2, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(17, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lFire Protection I", "§7Grants protection against fire.", 150);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.FIRE_PROTECTION, 1, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(20, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lFire Protection II", "§7Grants protection against fire.", 250);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.FIRE_PROTECTION, 2, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(21, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lPower I", "§7Increases bow damage.", 200);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.POWER, 1, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(22, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lPower II", "§7Increases bow damage.", 300);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.POWER, 2, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(23, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lSharpness III", "§7Increases sword damage.", 250);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.SHARPNESS, 3, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(24, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lSharpness IV", "§7Increases sword damage.", 400);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.SHARPNESS, 4, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(25, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lSweeping Edge III", "§7Increases sweeping damage.", 250);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.SWEEPING_EDGE, 3, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(26, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lProjectile Protection I", "§7Grants protection against projectiles.", 150);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.PROJECTILE_PROTECTION, 1, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(29, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lProjectile Protection II", "§7Grants protection against projectiles.", 250);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.PROJECTILE_PROTECTION, 2, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(30, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lPunch II", "§7Increases bow knockback.", 250);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.PUNCH, 2, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(31, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lFlame", "§7Sets arrows on fire.", 350);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.FLAME, 1, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(32, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lUnbreaking I", "§7Increases item durability.", 100);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.UNBREAKING, 1, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(33, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lUnbreaking II", "§7Increases item durability.", 150);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.UNBREAKING, 2, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(34, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lUnbreaking III", "§7Increases item durability.", 250);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.UNBREAKING, 3, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(35, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lProtection I", "§7Grants general protection.", 150);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.PROTECTION, 1, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(38, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lProtection II", "§7Grants general protection.", 250);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.PROTECTION, 2, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(39, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lMultishot", "§7Allows shooting multiple arrows at once.", 250);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.MULTISHOT, 1, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(40, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lQuick Charge III", "§7Reduces crossbow reload time.", 200);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.QUICK_CHARGE, 3, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(41, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lPiercing II", "§7Allows arrows to pass through multiple targets.", 300);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.PIERCING, 2, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(42, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lLoyalty III", "§7Causes thrown tridents to return.", 300);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.LOYALTY, 3, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(43, stack);

        stack = ShopUtils.createShopItem(Material.ENCHANTED_BOOK, "§8» §f§lRiptide III", "§7Allows tridents to be thrown while underwater.", 300);
        bookMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        bookMeta.addStoredEnchant(Enchantment.RIPTIDE, 3, true);
        stack.setItemMeta(bookMeta);
        this.inventory.setItem(44, stack);


        this.p.openInventory(this.inventory);
    }

    public void close() {
        guis.remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnchantingGUI shopGUI)) return false;
        return p.equals(shopGUI.p);
    }

    @Override
    public int hashCode() {
        return ("getdown_shop_gui_" + p.getUniqueId().toString()).hashCode();
    }

}
