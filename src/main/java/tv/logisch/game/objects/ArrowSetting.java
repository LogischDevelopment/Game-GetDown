package tv.logisch.game.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArrowSetting {

    public static final List<GameArrow> arrows = List.of(
            new GameArrow("normal_arrow", "Arrow", "", true),
            new GameArrow("knockback_arrow_1", "Knockback Arrow 1", "", true),
            new GameArrow("knockback_arrow_3", "Knockback Arrow 3", "", true),
            new GameArrow("damage_arrow_1", "Damage Arrow 1", "", true),
            new GameArrow("damage_arrow_2", "Damage Arrow 2", "", true),
            new GameArrow("damage_arrow_3", "Damage Arrow 3", "", true),
            new GameArrow("levitation_arrow", "Levitation Arrow", "", true),
            new GameArrow("poison_arrow", "Poison Arrow", "", true),
            new GameArrow("fire_arrow", "Fire Arrow", "", true),
            new GameArrow("blindness_arrow", "Blindness Arrow", "", true)
    );

    public static ItemStack getRandomArrow() {
        if( arrows.stream().noneMatch(GameArrow::enabled)) return new ItemStack(Material.ARROW, 1);
        GameArrow arrow = arrows.stream()
                .filter(GameArrow::enabled)
                .skip((int) (Math.random() * arrows.stream().filter(GameArrow::enabled).count()))
                .findFirst()
                .orElse(new GameArrow("normal_arrow", "Arrow", "", true));
        ItemStack stack = new ItemStack(Material.ARROW, 1);
        stack.editMeta(m -> {
            m.displayName(Component.text(arrow.name()));
            m.lore(Arrays.stream(arrow.description.split("\n")).map(Component::text).collect(Collectors.toList()));
            m.getPersistentDataContainer().set(new NamespacedKey("logisch", "custom_arrow"), PersistentDataType.STRING, arrow.id());
        });
        return stack;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Accessors(fluent = true)
    public static class GameArrow {

        private String id;
        private String name;
        private String description;
        private boolean enabled;

    }

}
