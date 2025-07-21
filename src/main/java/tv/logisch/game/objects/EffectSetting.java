package tv.logisch.game.objects;

import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class EffectSetting {

    public static final List<GameEffect> effects = List.of(
            new GameEffect(PotionEffectType.SPEED, 7, 1, true, true),
            new GameEffect(PotionEffectType.REGENERATION, 10, 2, true, true),
            new GameEffect(PotionEffectType.SLOW_FALLING, 10, 1, true, true),
            new GameEffect(PotionEffectType.JUMP_BOOST, 10, 2, true, true),
            new GameEffect(PotionEffectType.RESISTANCE, 8, 1, true, true),
            new GameEffect(PotionEffectType.ABSORPTION,  16, 4, true, true),
            new GameEffect(PotionEffectType.POISON, 12, 1, false, true),
            new GameEffect(PotionEffectType.WITHER, 10, 1, false, true),
            new GameEffect(PotionEffectType.BLINDNESS, 10, 1, false, true),
            new GameEffect(PotionEffectType.LEVITATION, 4, 1, false, true),
            new GameEffect(PotionEffectType.SLOWNESS, 7, 1, false, true),
            new GameEffect(PotionEffectType.NAUSEA, 10, 2, false, true)
    );

    public static GameEffect getRandomEffect() {
        if(disabled()) return null;
        return effects.stream()
                .filter(GameEffect::enabled)
                .skip((int) (Math.random() * effects.stream().filter(GameEffect::enabled).count()))
                .findFirst()
                .orElse(null);
    }

    public static boolean disabled() {
        return effects.stream().noneMatch(GameEffect::enabled);
    }

}
