package tv.logisch.game.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;

import java.util.Arrays;
import java.util.List;

public class ItemSetting {

    public static final List<GameItem> items = List.of(
            new GameItem(Material.CROSSBOW, "Crossbow", "", true),
            new GameItem(Material.TRIDENT, "Trident", "", true)
    );

    public static ItemStack getRandomItem() {
        if(!isItemEnabled()) return null;
        GameItem itemSetting = items.get((int) (Math.random() * items.size()));
        while (!itemSetting.enabled()) {
            itemSetting = items.get((int) (Math.random() * items.size()));
        }
        ItemStack stack = new ItemStack(itemSetting.material(), 1);
        GameItem finalItemSetting = itemSetting;
        stack.editMeta(meta -> {
            meta.displayName(Component.text(finalItemSetting.name()));
            meta.lore(Arrays.stream(finalItemSetting.description.split("\n"))
                .map(Component::text)
                .toList());
            if (meta instanceof CrossbowMeta crossbowMeta) {
                crossbowMeta.setChargedProjectiles(List.of(ArrowSetting.getRandomArrow()));
            }
        });
        return stack;
    }

    public static boolean isItemEnabled() {
        return items.stream().anyMatch(GameItem::enabled);
    }

    @Getter
    @Accessors(fluent = true)
    @AllArgsConstructor
    public static class GameItem {
        private Material material;
        private String name;
        private String description;
        private boolean enabled;
    }

}
