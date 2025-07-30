package tv.logisch.game;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import tv.logisch.api.LogiAPI;
import tv.logisch.game.commands.*;
import tv.logisch.game.gui.settings.SettingGUIListener;
import tv.logisch.game.gui.shop.ShopGUIListener;
import tv.logisch.game.gui.world.WorldGUIListener;
import tv.logisch.game.listener.*;
import tv.logisch.game.manager.GameManager;
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
        pm.registerEvents(new QuitListener(), this);
        pm.registerEvents(new BlockBreakListener(), this);
        pm.registerEvents(new BlockPlaceListener(), this);
        pm.registerEvents(new PlayerMoveListener(), this);
        pm.registerEvents(new PlayerLoginListener(), this);
        pm.registerEvents(new ShopGUIListener(), this);
        pm.registerEvents(new PlayerInteractListener(), this);
        pm.registerEvents(new PlayerDamageListener(), this);
        pm.registerEvents(new TNTExplosionListener(), this);
        pm.registerEvents(new ProjectileHitListener(), this);
        pm.registerEvents(new EntityShootBowListener(), this);
        pm.registerEvents(new EntityDamageByEntityListener(), this);
        pm.registerEvents(new PlayerDropListener(), this);
        pm.registerEvents(new PlayerFoodLevelChangeListener(), this);
        pm.registerEvents(new SettingGUIListener(), this);
        pm.registerEvents(new WorldGUIListener(), this);
        pm.registerEvents(new EntityRegainHealthListener(), this);

        /* COMMAND REGISTRATION */
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, (event) -> {
            Commands registrar = event.registrar();
        });

        getCommand("start").setExecutor(new Start());
        getCommand("coins").setExecutor(new Coins());
        getCommand("skip").setExecutor(new Skip());
        getCommand("settings").setExecutor(new Settings());
        getCommand("worlds").setExecutor(new Worlds());

        GameManager.get().waitingWorld(Bukkit.createWorld(new WorldCreator("waiting")));
        GameManager.get().pvpWorld(Bukkit.createWorld(new WorldCreator("pvp")));
        Bukkit.getWorlds().forEach(w -> {
            w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            w.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            w.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
        });

    }

    @Override
    public void onDisable() {
        getLogger().info("GetDown plugin has been disabled!");
    }

}
