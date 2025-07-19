package tv.logisch.game;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import tv.logisch.api.LogiAPI;
import tv.logisch.game.listener.JoinListener;
import tv.logisch.game.objects.GameConfig;
import tv.logisch.game.utils.Config;

import java.io.File;
import java.util.logging.Logger;

@Getter
@Accessors(fluent = true)
public final class GetDown extends JavaPlugin {

    @Getter @Accessors(fluent = true)
    private static GetDown instance;

    private final String prefix = "§6§lGETDOWN §8» §7";
    private Logger logger;
    private GameConfig gameConfig;
    private LogiAPI logiAPI;

    @Override
    public void onLoad() {
        instance = this;
        logger = getLogger();
    }

    @Override
    public void onEnable() {

        this.logiAPI = new LogiAPI(new Config(new File(Bukkit.getPluginsFolder().getPath()+"/getdown/config.json")).get("logisch.api.key").getAsString());
        this.gameConfig = new GameConfig().initialize();

        System.setProperty("LOGISCH_TYPE", "GAME");
        String hostUUID = gameConfig.hostUUID() != null ? gameConfig.hostUUID().toString() : "null";
        String hostName = gameConfig.hostName() != null ? gameConfig.hostName() : "null";
        System.setProperty("LOGISCH_FLAGS", "host="+hostUUID+";hostName="+hostName+";sendJoinMe=true;retrieveJoinMe=false");

        getLogger().info("GetDown plugin has been enabled!");


        /* EVENT REGISTRATION */
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinListener(), this);

        /* COMMAND REGISTRATION */

        Bukkit.createWorld(new WorldCreator("waiting"));
        Bukkit.getWorlds().forEach(w -> {
            w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            w.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        });

    }

    @Override

    public void onDisable() {
        getLogger().info("GetDown plugin has been disabled!");
    }


    /**
     *
     * - PlayerMoveEvent -> on gold block -> add coins to player
     * - PlayerMoveEvent -> on diamond block -> add bonus coins to player
     * - PlayerMoveEvent -> on lapis block -> add random item to player (crossbow, bow)
     * - PlayerMoveEvent -> on obsidian block -> replace obsidian to slime or do nothing (customizable chance)
     * - PlayerMoveEvent -> on redstone block -> give random effect to player
     * - Settings GUI for host
     * - PlayerMoveEvent -> y <= 0 -> player finished
     *
     * ABLAUF:
     * - Game state = WAITING
     * - Player joins -> add to waiting world
     *    - set game mode to adventure
     * - host trigger start
     *    - Game state = STARTING
     *    - teleport all players to game world
     *    - run countdown
     *    - Game state = RUNNING
     * - top 3 players get bonus coins
     * - 3 players finished -> Game state = SHOPPING
     * * - Shopping phase
     *      - teleport players to waiting world
     *      - open shop GUI for players
     *      - start countdown (customizable)
     *   - Game state = PVP
     *   - teleport players to PVP world
     *  - 1 player left -> Game state = END + end game
     *
     */

}
