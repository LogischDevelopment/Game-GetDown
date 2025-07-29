package tv.logisch.game.worlds;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Accessors(fluent = true)
@AllArgsConstructor
public class WorldObject {

    private String name;
    private String difficulty;
    private List<String> colors;
    private Location spawnPoint;
    private List<String> builders;
    private Map<Material, Double> materials;
    private Location loc1;
    private Location loc2;
    private Material placeholder;
    private Material floorPlaceholder;

}
