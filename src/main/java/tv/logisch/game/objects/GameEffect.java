package tv.logisch.game.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.potion.PotionEffectType;

@Getter
@Setter
@AllArgsConstructor
@Accessors(fluent = true)
public class GameEffect {

    private PotionEffectType type;
    private int duration; // in seconds
    private int amplifier;
    private boolean positive;
    private boolean enabled;

}
