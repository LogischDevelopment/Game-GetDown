package tv.logisch.game.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import tv.logisch.game.enums.GameState;
import tv.logisch.game.manager.GameManager;

import java.util.ArrayList;
import java.util.List;

public class Scoreboard extends ScoreboardManager {

    public static List<Scoreboard> scoreboards = new ArrayList<>();

    public Scoreboard(Player player) {
        super(player, "    ①    ");
    }

    @Override
    public void createScoreboard() {
        this.update();
//        setScore("§0", 16);
//        setScore("§3§l» Top 3", 15);
//        setScore("§71§8. §fN/A §8(§f-§8)", 14);
//        setScore("§72§8. §fN/A §8(§f-§8)", 13);
//        setScore("§73§8. §fN/A §8(§f-§8)", 12);
//        setScore("§74§8. §fN/A §8(§f-§8)", 11);
//        setScore("§75§8. §fN/A §8(§f-§8)", 10);
//        setScore("§76§8. §fN/A §8(§f-§8)", 9);
//        setScore("§77§8. §fN/A §8(§f-§8)", 8);
//        setScore("§78§8. §fN/A §8(§f-§8)", 7);
//        setScore("§79§8. §fN/A §8(§f-§8)", 6);
//        setScore("§2", 5);
//        setScore("§3§l» Rank", 4);
//        setScore("§70§8. §fN/A §8(§f-§8)", 3);
//        setScore("§3", 2);
//        setScore("§3§l» Server", 1);
//        setScore("Logisch.tv", 0);
    }

    @Override
    public void update() {
        if(player == null || !player.isOnline()) return;
        List<? extends Player> allTop = Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.getGameMode().equals(GameMode.SURVIVAL))
                .sorted((p1, p2) -> Integer.compare(p2.getLocation().getBlockY(), p1.getLocation().getBlockY()))
                .toList();
        List<? extends Player> top = allTop.stream()
                .limit(9)
                .toList();
        List<String> topNames = new ArrayList<>();
        int count = 0;
        for(Player p : top) {
            String name = p.getName().length() > 13 ? p.getName().substring(0, 11) + "..." : p.getName();
            if(name.length() > 13) {
                name = name.substring(0, 13);
            }
            topNames.add(++count + "§8. §f" + name + " §8(§f" + p.getLocation().getBlockY() + "§8)");
        }

        for(int i = 0; i < 16; i++) {
            removeScore(i);
        }

        int score = 0;
        setScore("Logisch.tv", score++);
        setScore("§3§l» Server", score++);
        setScore("§3", score++);
        setScore((allTop.indexOf(player)+1) + "§8. §f" + player.getName() + " §8(§f" + player.getLocation().getBlockY() + "§8)", score++);
        setScore("§3§l» Rank", score++);
        setScore("§2", score++);
        for(String name : topNames.reversed()) {
            setScore(name, score++);
        }
        setScore("§3§l» Top 3", score++);
        setScore("§0", score++);
    }

}
