package tv.logisch.game.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import tv.logisch.game.enums.GameState;
import tv.logisch.game.manager.GameManager;

public class PlayerDropListener implements Listener {

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {

        GameState state = GameManager.get().state();
        if(!state.equals(GameState.PVP)) {
            e.setCancelled(true);
            return;
        }

    }

}
