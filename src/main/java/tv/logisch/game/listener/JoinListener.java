package tv.logisch.game.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tv.logisch.game.GetDown;
import tv.logisch.game.enums.GameState;
import tv.logisch.game.manager.GameManager;

public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        GameState state = GameManager.get().state();
        if(state.equals(GameState.WAITING)) {
            p.teleport(p.getWorld().getSpawnLocation());
            p.setGameMode(GameMode.ADVENTURE);
            return;
        }
        if(state.equals(GameState.RUNNING)) {
            p.teleport(GameManager.get().gameWorld().getSpawnLocation());
            p.setGameMode(GameMode.SURVIVAL);
            return;
        }
        if(state.equals(GameState.STARTING) || state.equals(GameState.SHOPPING) || state.equals(GameState.PVP) || state.equals(GameState.ENDED)) {
            p.kick(Component.text(GetDown.instance().prefix()+"§cThe game is already in progress!"));
            return;
        }

        e.joinMessage(Component.empty());
        for(Player pl : Bukkit.getOnlinePlayers()) {
            pl.sendMessage(Component.text("§a§lJOIN §8» §7" + p.getName()));
        }

    }

}
