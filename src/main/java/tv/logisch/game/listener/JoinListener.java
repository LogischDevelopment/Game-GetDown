package tv.logisch.game.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        e.joinMessage(Component.empty());
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(Component.text("§a§lJOIN §8» §7" + e.getPlayer().getName()));
        }

    }

}
