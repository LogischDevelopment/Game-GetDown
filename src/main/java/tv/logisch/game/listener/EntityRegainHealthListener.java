package tv.logisch.game.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import tv.logisch.game.enums.GameState;
import tv.logisch.game.manager.GameManager;

public class EntityRegainHealthListener implements Listener {

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent e) {
        if(GameManager.get().state().equals(GameState.RUNNING)) {
            e.setAmount(e.getAmount()*0.25);
        }
    }

}
