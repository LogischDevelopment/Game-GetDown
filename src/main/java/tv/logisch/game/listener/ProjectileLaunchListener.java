package tv.logisch.game.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import tv.logisch.game.enums.GameState;
import tv.logisch.game.manager.GameManager;

public class ProjectileLaunchListener implements Listener {

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        if (GameManager.get().state().equals(GameState.PVP) || GameManager.get().state().equals(GameState.RUNNING)) {
            return;
        } else {
            e.setCancelled(true);
        }
    }

}
