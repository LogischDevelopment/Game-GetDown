package tv.logisch.game.manager;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import tv.logisch.game.GetDown;
import tv.logisch.game.enums.GameState;
import tv.logisch.game.scoreboard.Scoreboard;
import tv.logisch.game.utils.Format;
import tv.logisch.game.worlds.WorldManager;
import tv.logisch.game.worlds.WorldObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

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
    private WorldManager worldManager;

    private World waitingWorld;
    private World gameWorld;
    private World pvpWorld;

    private String worldName;
    private int shoppingTime;
    private int percentage;

    private NamespacedKey shopKey = new NamespacedKey("logisch_getdown", "shop");
    private NamespacedKey settingKey = new NamespacedKey("logisch_getdown", "settings");

    public List<Player> playersFinished;

    public GameManager() {
        this.state = GameState.WAITING;
        this.playerCoinManager = new PlayerCoinManager();
        this.worldManager = new WorldManager();
        this.shoppingTime = 120;
        this.percentage = 20;
        this.worldName = "drop1";
        this.playersFinished = new ArrayList<>();
    }

    public void start() {
        this.state = GameState.STARTING;
        GetDown.instance().logger().info("worldManager = " + this.worldManager);
        GetDown.instance().logger().info("worldName = " + this.worldName);

        GetDown.instance().logger().info("A: Vor Aufruf generateWorld");
        CompletableFuture<WorldObject> generateWorld = this.worldManager.generateWorld(this.worldName);
        GetDown.instance().logger().info("B: Nach Aufruf generateWorld");

        generateWorld.thenAccept(worldRaw -> {
            if(!(worldRaw instanceof WorldObject world)) {
                GetDown.instance().logger().severe("Failed to generate world: " + this.worldName);
                Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(GetDown.instance().prefix() + "§cFehler beim Generieren der Welt!"));
                return;
            }
            Bukkit.getScheduler().runTask(GetDown.instance(), () -> {
                GetDown.instance().logger().info("World generated: " + world.name());
                this.gameWorld(Bukkit.getWorld(world.name()));
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
                            if(finalTime == 15 || finalTime == 10 || finalTime <= 5) {
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
                    this.state = GameState.RUNNING;
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        p.sendMessage(GetDown.instance().prefix() + "Das Spiel hat begonnen!");
                        p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        p.setLevel(0);
                        p.setExp(0);
                    });
                    Scoreboard.scoreboards.forEach(Scoreboard::start);
                    this.startScoreboardUpdater();
                });
            });
        })
        .exceptionally(ex -> {
            GetDown.instance().logger().severe("Fehler beim Generieren der Welt: " + ex.getMessage());
            ex.printStackTrace();
            Bukkit.getOnlinePlayers().forEach(p ->
                    p.sendMessage(GetDown.instance().prefix() + "§cFehler beim Generieren der Welt!"));
            return null;
        });
    }

    public void startScoreboardUpdater() {
        AtomicInteger taskId = new AtomicInteger(0);
        taskId.set(Bukkit.getScheduler().runTaskTimer(GetDown.instance(), () -> {
            if(this.state.equals(GameState.RUNNING)) {
                Scoreboard.scoreboards.forEach(Scoreboard::update);
            } else {
                Bukkit.getScheduler().cancelTask(taskId.get());
            }
        }, 10L, 10L).getTaskId());
    }

    public void startShopping() {
        this.state = GameState.SHOPPING;
        Scoreboard.scoreboards.forEach(Scoreboard::unregister);
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.teleport(this.waitingWorld.getSpawnLocation());
            p.getInventory().clear();
            p.setGameMode(GameMode.ADVENTURE);
            p.sendMessage(GetDown.instance().prefix() + "Die §fShopping-Phase §7hat begonnen!");
            p.sendMessage(GetDown.instance().prefix() + "Ihr habt §f" + Format.time(this.shoppingTime) + " §7Zeit, um einzukaufen!");
            p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            p.setLevel(999);

            ItemStack stack = new ItemStack(Material.CHEST);
            stack.editMeta(m -> {
                m.displayName(Component.text("§8» §6§lShop"));
                m.lore(List.of(
                        Component.text("§7Klicke hier, um den Shop zu öffnen!")
                ));
                m.getPersistentDataContainer().set(this.shopKey(), PersistentDataType.STRING, "open");
            });
            p.getInventory().setItem(8, stack);
            stack = new ItemStack(Material.ANVIL);
            stack.editMeta(m -> {
                m.displayName(Component.text("§8» §6§lAmboss"));
                m.lore(List.of(
                        Component.text("§7Interagiere mit dem Amboss, um deine Items zu verzaubern!")
                ));
                m.getPersistentDataContainer().set(this.shopKey, PersistentDataType.STRING, "anvil");
            });
            p.getInventory().setItem(7, stack);
        });
        AtomicInteger taskId = new AtomicInteger(0);
        taskId.set(Bukkit.getScheduler().runTaskTimer(GetDown.instance(), () -> {
            if(this.shoppingTime <= 0) {
                Bukkit.getScheduler().cancelTask(taskId.get());
                this.startPVP();
                return;
            }
            if(this.shoppingTime % 60 == 0 || this.shoppingTime == 30 || this.shoppingTime == 15 || this.shoppingTime == 10 || this.shoppingTime <= 5) {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    p.sendMessage(GetDown.instance().prefix() + "Die PVP-Phase beginnt in §f" + this.shoppingTime + " §7Sekunden!");
                    p.playSound(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    p.sendActionBar(Component.text(GetDown.instance().prefix()+"Current coins: §6"+this.playerCoinManager.getCoins(p)));
                });
            } else {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    p.sendActionBar(Component.text(GetDown.instance().prefix()+"Current coins: §6"+this.playerCoinManager.getCoins(p)));
                });
            }
            this.shoppingTime--;
        }, 20L, 20L).getTaskId());
    }

    public void startPVP() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.teleport(this.pvpWorld.getSpawnLocation());
            p.sendMessage(GetDown.instance().prefix() + "Die §fPVP-Phase §7hat begonnen!");
            p.playSound(p, Sound.ITEM_GOAT_HORN_SOUND_1, 1.0f, 1.0f);
            p.setGameMode(GameMode.SURVIVAL);
            p.setLevel(0);
            p.setExp(0);
            for (ItemStack content : p.getInventory().getContents()) {
                if(content == null) continue;
                if(content.getType().equals(Material.CHEST) || content.getType().equals(Material.ANVIL)) {
                    if(content.getPersistentDataContainer().has(this.shopKey, PersistentDataType.STRING)) {
                        content.setAmount(0);
                    }
                }
            }
        });
        this.state = GameState.PVP;
    }

    public void stop(Player winner) {
        this.state = GameState.ENDED;
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.sendMessage(GetDown.instance().prefix() + "Das Spiel ist beendet!");
            if(winner != null) {
                p.sendMessage(GetDown.instance().prefix() + "§f" + winner.getName() + " §7hat das Spiel §agewonnen§7!");
                p.sendTitlePart(TitlePart.TITLE, Component.text("§aGewinner: §f" + winner.getName()));
                p.sendTitlePart(TitlePart.SUBTITLE, Component.text("§7Herzlichen Glückwunsch!"));
            } else {
                p.sendMessage(GetDown.instance().prefix() + "§cEs gab keinen Gewinner!");
            }
            p.playSound(p, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
        });
        Bukkit.getScheduler().runTaskAsynchronously(GetDown.instance(), () -> {
            int seconds = 15;
            while (seconds > 0) {
                int finalSeconds = seconds;
                Bukkit.getOnlinePlayers().forEach(p -> {
                    if(finalSeconds == 15 || finalSeconds == 10 || finalSeconds <= 5) {
                        p.sendMessage(GetDown.instance().prefix() + "Der Server stoppt in §f" + finalSeconds + " §7Sekunden!");
                        p.playSound(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                seconds--;
            }
            Bukkit.shutdown();
        });
    }

    public boolean isHost(UUID uuid) {
        return GetDown.instance().gameConfig().hostUUID().equals(uuid);
    }
    public boolean isHost(String name) {
        return GetDown.instance().gameConfig().hostName().equalsIgnoreCase(name);
    }

}
