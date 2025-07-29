package tv.logisch.game.worlds;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Material;

import java.util.List;

@Getter
@Setter
@Accessors(fluent = true)
@AllArgsConstructor
public class ColorObject {

    private String name;
    private List<Material> materials;
    private Material floor;

}
