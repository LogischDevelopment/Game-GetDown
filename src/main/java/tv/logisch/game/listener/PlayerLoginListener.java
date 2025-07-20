package tv.logisch.game.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import tv.logisch.game.enums.GameState;
import tv.logisch.game.manager.GameManager;

public class PlayerLoginListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {

        GameState state = GameManager.get().state();

        if(state.equals(GameState.SHOPPING) || state.equals(GameState.PVP) || state.equals(GameState.ENDED)) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Component.text("§cThe game is already in progress!"));
        }

    }

}
