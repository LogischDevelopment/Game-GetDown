package tv.logisch.game.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import tv.logisch.game.GetDown;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Getter
@Setter
@Accessors(fluent = true)
@AllArgsConstructor
public class WorldManager {

    private String name;
    private Location l1;
    private Location l2;

    public CompletableFuture<Boolean> replacePlaceholders(Material placeholder, List<Material> materials) {
        List<Block> candidates = new ArrayList<>();
        for (int x = this.l1.getBlockX(); x <= this.l2.getBlockX(); x++) {
            for (int y = this.l1.getBlockY(); y <= this.l2.getBlockY(); y++) {
                for (int z = this.l1.getBlockZ(); z <= this.l2.getBlockZ(); z++) {
                    Block block = this.l1.getWorld().getBlockAt(x, y, z);

                    // Optional: Wenn du nur innerhalb eines Radius willst
                    double centerX = (this.l1.getX() + this.l2.getX()) / 2.0;
                    double centerZ = (this.l1.getZ() + this.l2.getZ()) / 2.0;
                    double radius = (this.l2.getX() - this.l1.getX()) / 2.0;

                    double dx = x - centerX;
                    double dz = z - centerZ;
                    if (dx * dx + dz * dz > radius * radius) continue;

                    if (block.getType() == placeholder) {
                        candidates.add(block);
                    }
                }
            }
        }

        Collections.shuffle(candidates);

        // Wahrscheinlichkeiten
        Map<Material, Double> probabilities = Map.of(
                Material.GOLD_BLOCK, 0.1, // 10%
                Material.DIAMOND_BLOCK, 0.03, // 3%
                Material.OBSIDIAN, 0.4, // 4%
                Material.IRON_BLOCK, 0.06, // 6%
                Material.LAPIS_BLOCK, 0.08 // 8%
        );

        int total = candidates.size();
        int index = 0;

        for (Map.Entry<Material, Double> entry : probabilities.entrySet()) {
            int count = (int) Math.round(entry.getValue() * total);
            for (int i = 0; i < count && index < total; i++) {
                candidates.get(index++).setType(entry.getKey());
            }
        }

        for (int i = index; i < total; i++) {
            candidates.get(i).setType(Material.SLIME_BLOCK);
        }

        GetDown.instance().logger().info("Replaced " + total + " placeholder blocks.");
        return CompletableFuture.completedFuture(true);
    }

}
