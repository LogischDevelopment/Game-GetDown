package tv.logisch.game.utils;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tv.logisch.oneBlockRace.OneBlockRace;
import tv.logisch.oneBlockRace.manager.GameManager;

public class AnimationUtils {

    private static final String[] COLOR_GRADIENT = {
            "#41CFFF", "#3CC8F1", "#37C2E2", "#32BCD4", "#2DB6C6",
            "#29AFB7", "#24A9A9", "#1FA39B", "#1A8C8C", "#15807E"
    };

    private static double animationProgress = 0.0;
    private static BukkitRunnable animationTask;

    @Getter
    @Setter
    private static boolean isRunning;

    private AnimationUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static void startAnimation() {
        isRunning = true;
        animationTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (isRunning) {
                    sendBossbar();
                    animationProgress = (animationProgress + 0.03) % 1.0;
                } else {
                    this.cancel();
                }
            }
        };
        animationTask.runTaskTimer(OneBlockRace.instance(), 0, 1);
    }

    public static void stopAnimation() {
        isRunning = false;
        if (animationTask != null) {
            animationTask.cancel();
        }
    }

    public static void sendBossbar() {
        long timeLeft = GameManager.get().timeLeft();
        String text = Format.time(timeLeft);
        Component animatedComponent = animateGradientComponent(text);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendActionBar(animatedComponent);
        }
    }

    private static Component animateGradientComponent(String text) {
        TextComponent.Builder builder = Component.text();

        int gradientLength = COLOR_GRADIENT.length;
        int extendedLength = Math.max(text.length(), gradientLength);

        for (int i = 0; i < text.length(); i++) {
            double position = (animationProgress + (double) i / extendedLength) % 1.0;
            int index = (int) (position * gradientLength);
            int nextIndex = (index + 1) % gradientLength;

            double fraction = (position * gradientLength) % 1.0;
            String interpolated = interpolateColor(COLOR_GRADIENT[index], COLOR_GRADIENT[nextIndex], fraction);

            TextColor color = TextColor.fromHexString(interpolated);
            builder.append(
                    Component.text(String.valueOf(text.charAt(i)))
                            .color(color)
                            .decorate(TextDecoration.BOLD)
            );
        }

        return builder.build();
    }

    private static String interpolateColor(String color1, String color2, double fraction) {
        int r1 = Integer.parseInt(color1.substring(1, 3), 16);
        int g1 = Integer.parseInt(color1.substring(3, 5), 16);
        int b1 = Integer.parseInt(color1.substring(5, 7), 16);

        int r2 = Integer.parseInt(color2.substring(1, 3), 16);
        int g2 = Integer.parseInt(color2.substring(3, 5), 16);
        int b2 = Integer.parseInt(color2.substring(5, 7), 16);

        int r = (int) (r1 + fraction * (r2 - r1));
        int g = (int) (g1 + fraction * (g2 - g1));
        int b = (int) (b1 + fraction * (b2 - b1));

        return String.format("#%02X%02X%02X", r, g, b);
    }

}
