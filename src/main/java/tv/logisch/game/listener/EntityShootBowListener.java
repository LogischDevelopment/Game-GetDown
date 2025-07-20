package tv.logisch.game.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class EntityShootBowListener implements Listener {

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent e) {
        if(!(e.getEntity() instanceof Player p)) return;
        if(e.getBow() == null) return;

        p.getInventory().remove(e.getBow());

    }

}
