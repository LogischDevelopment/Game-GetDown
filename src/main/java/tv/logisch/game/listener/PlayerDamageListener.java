package tv.logisch.game.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import tv.logisch.game.GetDown;
import tv.logisch.game.enums.GameState;
import tv.logisch.game.manager.GameManager;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player p)) return;

        GameState state = GameManager.get().state();
        if(state.equals(GameState.WAITING) || state.equals(GameState.STARTING) || state.equals(GameState.FINISHED) || state.equals(GameState.SHOPPING) || state.equals(GameState.ENDED)) {
            e.setCancelled(true);
            return;
        }

        if(state.equals(GameState.RUNNING)) {
            if(e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                e.setCancelled(true);
                p.teleport(GameManager.get().gameWorld().getSpawnLocation());
                return;
            }
            if(e.getFinalDamage() >= p.getHealth()) {
                e.setCancelled(true);
                p.setHealth(20.0);
                p.getInventory().clear();
                p.getInventory().setArmorContents(new ItemStack[0]);
                p.teleport(p.getWorld().getSpawnLocation());
            }
            return;
        }

        if(state.equals(GameState.PVP)) {

            if(e.getFinalDamage() >= p.getHealth()) {
                e.setCancelled(true);
                p.setHealth(20.0); // reset health
                p.setGameMode(GameMode.SPECTATOR);
                AtomicInteger remaining = new AtomicInteger(0);
                AtomicReference<Player> winner = new AtomicReference<>(null);
                Bukkit.getOnlinePlayers().forEach(target -> {
                    target.sendMessage(Component.text(GetDown.instance().prefix() + "§c" + p.getName() + " §7ist ausgeschieden!"));
                    if(target.getGameMode() != GameMode.SPECTATOR) {
                        remaining.getAndIncrement();
                        winner.set(target);
                    }
                });
                if(remaining.get() <= 1) {
                    GameManager.get().stop(winner.get());
                }
            }

        }

    }

}
