package tv.logisch.game.listener;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import tv.logisch.game.GetDown;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        if (e.getBlockPlaced().getType().equals(Material.BRICKS)) {
            // Bukkit.getScheduler().runTaskLater(GetDown.instance(), () -> {
            //     e.getBlockPlaced().setType(Material.AIR);
            // }, 7*20L);

            Block block = e.getBlock();
            Location location = block.getLocation();

            Bukkit.getScheduler().runTaskLater(GetDown.instance(), () -> {
                location.getWorld().spawnParticle(Particle.BLOCK_CRUMBLE, location.add(0.5, 0.5, 0.5), 20, block.getBlockData());

                location.getWorld().playSound(location, Sound.BLOCK_STONE_BREAK, 1.0f, 1.0f);

                block.setType(Material.AIR);
            }, 7 * 20);

            return;
        }
        if(e.getBlockPlaced().getType().equals(Material.TNT)) {
            e.getBlockPlaced().setType(Material.AIR);

            Location loc = e.getBlockPlaced().getLocation().add(0.5, 0, 0.5);
            TNTPrimed tnt = (TNTPrimed) loc.getWorld().spawn(loc, TNTPrimed.class);
            tnt.setSource(e.getPlayer());
            tnt.setFuseTicks(80); // 4 seconds
            return;
        }
        if(e.getBlockPlaced().getType().equals(Material.COBWEB)) {
            Bukkit.getScheduler().runTaskLater(GetDown.instance(), () -> {
                e.getBlockPlaced().setType(Material.AIR);
            }, 16*20L);
            return;
        }
        e.setCancelled(true);
    }

}
