package tv.logisch.game.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import tv.logisch.game.enums.GameState;
import tv.logisch.game.manager.GameManager;

public class PlayerFoodLevelChangeListener implements Listener {

    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent e) {
        if(!GameManager.get().state().equals(GameState.PVP)) e.setCancelled(true);
    }

}
