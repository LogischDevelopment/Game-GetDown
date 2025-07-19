package tv.logisch.game.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import tv.logisch.game.GetDown;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getBlockPlaced().getType() == Material.BRICKS) {
            Bukkit.getScheduler().runTaskLater(GetDown.instance(), () -> {
                e.getBlockPlaced().setType(Material.AIR);
            }, 7*20L);
            return;
        }
        e.setCancelled(true);
    }

}
