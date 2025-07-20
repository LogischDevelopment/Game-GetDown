package tv.logisch.game.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class TNTExplosionListener implements Listener {

    @EventHandler
    public void onTNTExplosion(EntityExplodeEvent e) {
        if (e.getEntityType().equals(EntityType.TNT)) {
            e.blockList().clear();
        }
    }

}
