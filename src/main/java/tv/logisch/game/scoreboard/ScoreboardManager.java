package tv.logisch.game.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.bukkit.scoreboard.Scoreboard;

public abstract class ScoreboardManager {
    protected final Scoreboard scoreboard;
    protected Objective objective;

    public final Player player;
    private final String displayName;

    public ScoreboardManager(Player player, String displayName) {
        this.player = player;
        this.displayName = displayName;

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        player.setScoreboard(this.scoreboard);
    }

    public abstract void createScoreboard();

    public abstract void update();


    public void start() {
        Objective display = this.scoreboard.getObjective("logigd");
        if(display != null) {
            display.unregister();
        }

        this.objective = this.scoreboard.registerNewObjective("logigd", Criteria.DUMMY, Component.text(displayName));
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        createScoreboard();
    }

    public void unregister() {
        Objective obj = this.scoreboard.getObjective("logigd");
        if(obj != null) {
            obj.unregister();
        }
    }


    public void setDisplayName(String displayName) {
        this.objective.displayName(Component.text(displayName));
    }

    public void setScore(String content, int score) {
        Team team = getTeamByScore(score);

        if(team == null) {
            return;
        }

        team.prefix(Component.text(content));
        showScore(score);
    }

    public void removeScore(int score) {
        hideScore(score);
    }

    private EntryName getEntryNameByScore(int score) {
        for(EntryName name : EntryName.values()) {
            if(score == name.getEntry()) {
                return name;
            }
        }

        return null;
    }

    private Team getTeamByScore(int score) {
        EntryName name = getEntryNameByScore(score);

        if(name == null) {
            return null;
        }

        Team team = scoreboard.getEntryTeam(name.getEntryName());

        if(team != null) {
            return team;
        }

        team = scoreboard.registerNewTeam(name.name());
        team.addEntry(name.getEntryName());
        return team;
    }

    private void showScore(int score) {
        EntryName name = getEntryNameByScore(score);

        if(name == null) {
            return;
        }

        if(objective.getScore(name.getEntryName()).isScoreSet()) {
            return;
        }

        objective.getScore(name.getEntryName()).setScore(score);
    }

    private void hideScore(int score) {
        EntryName name = getEntryNameByScore(score);

        if(name == null) {
            return;
        }

        if(!objective.getScore(name.getEntryName()).isScoreSet()) {
            return;
        }

        scoreboard.resetScores(name.getEntryName());
    }

    public OfflinePlayer getPlayer() {
        return (OfflinePlayer) this.player;
    }
}
