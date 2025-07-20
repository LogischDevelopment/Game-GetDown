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
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionType;
import tv.logisch.game.GetDown;
import tv.logisch.game.manager.GameManager;
import tv.logisch.game.utils.ShopUtils;

import java.util.ArrayList;
import java.util.List;

public class UtilityGUI {

    public static List<UtilityGUI> guis = new ArrayList<>();
    public static UtilityGUI get(Player player) {
        for (UtilityGUI gui : guis) {
            if (gui.p.equals(player)) {
                return gui;
            }
        }
        UtilityGUI newGui = new UtilityGUI(player);
        guis.add(newGui);
        return newGui;
    }
    public static boolean has(Player player) {
        for(UtilityGUI gui : guis) {
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

    private UtilityGUI(Player player) {
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
        ItemStack activeCategory = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        activeCategory.editMeta(m -> m.setHideTooltip(true));
        this.inventory.setItem(28, activeCategory);

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
        ItemStack item = ShopUtils.createShopItem(Material.TURTLE_HELMET, "§8» §f§lTurtle Helmet", "§7Just a silly looking helmet.", 1000);
        this.inventory.setItem(11, item);

        item = ShopUtils.createShopItem(Material.POTION, "§8» §f§lHealing Potion", "§7A potion that heals you for 4 hearts.", 300);
        PotionMeta potionMeta1 = (PotionMeta) item.getItemMeta();
        potionMeta1.setBasePotionType(PotionType.HEALING);
        item.setItemMeta(potionMeta1);
        this.inventory.setItem(20, item);

        // splash potion of harming
        item = ShopUtils.createShopItem(Material.SPLASH_POTION, "§8» §f§lSplash Potion of Harming", "§7A potion that deals 2 hearts of damage to players.", 450);
        PotionMeta potionMeta2 = (PotionMeta) item.getItemMeta();
        potionMeta2.setBasePotionType(PotionType.HARMING);
        item.setItemMeta(potionMeta2);
        this.inventory.setItem(29, item);

        // suspicious stew
        item = ShopUtils.createShopItem(Material.SUSPICIOUS_STEW, "§8» §f§lSuspicious Stew", "§7A stew that gives you random effects.", 350);
        this.inventory.setItem(38, item);
        // TODO Suspicious Stew 50/50 Chance zwischen Strength 2 30s bzw. Poison 2 30s

        // golden apple
        item = ShopUtils.createShopItem(Material.GOLDEN_APPLE, "§8» §f§lGolden Apple", "§7A golden apple that heals you for 4 hearts and gives you absorption.", 400);
        this.inventory.setItem(13, item);

        // apple
        item = ShopUtils.createShopItem(Material.APPLE, "§8» §f§lApple", "§7A simple apple that heals you for 2 hearts.", 25);
        this.inventory.setItem(22, item);

        // bread
        item = ShopUtils.createShopItem(Material.BREAD, "§8» §f§lBread", "§7A loaf of bread that heals you for 3 hearts.", 50);
        this.inventory.setItem(31, item);

        // chorus fruit
        item = ShopUtils.createShopItem(Material.CHORUS_FRUIT, "§8» §f§lChorus Fruit", "§7A fruit that teleports you to a random location.", 50);
        this.inventory.setItem(40, item);

        // 8 bricks
        item = ShopUtils.createShopItem(Material.BRICKS, "§8» §f§lBricks", "§7Some bricks that can be used to build structures.", 50);
        item.setAmount(8);
        this.inventory.setItem(15, item);

        // 4 tnt
        item = ShopUtils.createShopItem(Material.TNT, "§8» §f§lTNT", "§7A block of TNT that can be used to blow up blocks.", 100);
        item.setAmount(4);
        this.inventory.setItem(24, item);

        // 1 cobweb
        item = ShopUtils.createShopItem(Material.COBWEB, "§8» §f§lCobweb", "§7A cobweb that can be used to trap players.", 50);
        this.inventory.setItem(33, item);

        // totem
        item = ShopUtils.createShopItem(Material.TOTEM_OF_UNDYING, "§8» §f§lTotem of Undying", "§7A totem that saves you from death.", 800);
        this.inventory.setItem(42, item);

        // water bucket
        item = ShopUtils.createShopItem(Material.WATER_BUCKET, "§8» §f§lWater Bucket", "§7A bucket of water that can be used to extinguish yourself or create water sources.", 50);
        this.inventory.setItem(17, item);

        // bucket of pufferfish
        item = ShopUtils.createShopItem(Material.PUFFERFISH_BUCKET, "§8» §f§lPufferfish Bucket", "§7A bucket of pufferfish that can be used to poison players.", 75);
        this.inventory.setItem(26, item);

        // lava bucket
        item = ShopUtils.createShopItem(Material.LAVA_BUCKET, "§8» §f§lLava Bucket", "§7A bucket of lava that can be used to create fire or destroy blocks.", 100);
        this.inventory.setItem(35, item);

        // golden carrot with knockback 10
        item = ShopUtils.createShopItem(Material.GOLDEN_CARROT, "§8» §f§lGolden Carrot", "§7Just a golden carrot with knockback 10.", 400);
        item.editMeta(m -> {
            m.addEnchant(Enchantment.KNOCKBACK, 10, true);
        });
        this.inventory.setItem(44, item);


        this.p.openInventory(this.inventory);
    }

    public void close() {
        guis.remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UtilityGUI shopGUI)) return false;
        return p.equals(shopGUI.p);
    }

    @Override
    public int hashCode() {
        return ("getdown_shop_gui_" + p.getUniqueId().toString()).hashCode();
    }

}
