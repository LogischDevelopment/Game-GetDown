package tv.logisch.game.manager;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.*;
import org.bukkit.entity.Player;
import tv.logisch.game.GetDown;
import tv.logisch.game.enums.GameState;
import tv.logisch.game.utils.Format;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Accessors(fluent = true)
public class GameManager {

    private static GameManager instance;
    public static GameManager get() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    private GameState state;
    private PlayerCoinManager playerCoinManager;

    private World waitingWorld;
    private World gameWorld;
    private World pvpWorld;

    private String worldName;
    private int shoppingTime;
    private int percentage;

    public List<Player> playersFinished;

    public GameManager() {
        this.state = GameState.WAITING;
        this.playerCoinManager = new PlayerCoinManager();
        this.shoppingTime = 120;
        this.percentage = 30;
        this.worldName = "getdown";
        this.playersFinished = new ArrayList<>();
    }

    public void start() {
        this.state = GameState.STARTING;
        this.gameWorld(Bukkit.createWorld(new WorldCreator(this.worldName)));

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.teleport(this.gameWorld.getSpawnLocation());
            p.getInventory().clear();
            p.setGameMode(GameMode.SURVIVAL);
        });
        Bukkit.getScheduler().runTaskAsynchronously(GetDown.instance(), () -> {
            int time = 15;
            while (time > 0) {
                int finalTime = time;
                Bukkit.getOnlinePlayers().forEach(p -> {
                    if(finalTime == 15 || finalTime == 10 || finalTime == 5 || finalTime <= 3) {
                        p.sendMessage(GetDown.instance().prefix() + "Das Spiel startet in §f"+finalTime+" §7Sekunden!");
                        p.playSound(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    }
                    p.setLevel(finalTime);
                    p.setExp((float) finalTime / 15);
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                time--;
            }
            Bukkit.getOnlinePlayers().forEach(p -> {
                p.sendMessage(GetDown.instance().prefix() + "Das Spiel hat begonnen!");
                p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                p.setLevel(0);
                p.setExp(0);
            });
            this.state = GameState.RUNNING;
        });
    }

    public void startShopping() {
        this.state = GameState.SHOPPING;
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.teleport(this.waitingWorld.getSpawnLocation());
            p.getInventory().clear();
            p.setGameMode(GameMode.ADVENTURE);
            p.sendMessage(GetDown.instance().prefix() + "Die §fShopping-Phase §7hat begonnen!");
            p.sendMessage(GetDown.instance().prefix() + "Ihr habt §f" + Format.time(this.shoppingTime) + " §7Zeit, um einzukaufen!");
            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
        });
        Bukkit.getScheduler().runTaskLater(GetDown.instance(), () -> {
            this.state = GameState.PVP;
            Bukkit.getOnlinePlayers().forEach(p -> {
                p.teleport(this.pvpWorld.getSpawnLocation());
                p.sendMessage(GetDown.instance().prefix() + "Die §fPVP-Phase §7hat begonnen!");
                p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            });
        }, this.shoppingTime * 20L);
    }

    public void stop() {
        // TODO: Implement stop logic
    }

    public boolean isHost(UUID uuid) {
        return GetDown.instance().gameConfig().hostUUID().equals(uuid);
    }
    public boolean isHost(String name) {
        return GetDown.instance().gameConfig().hostName().equalsIgnoreCase(name);
    }

}
