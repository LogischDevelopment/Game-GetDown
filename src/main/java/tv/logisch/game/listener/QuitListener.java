package tv.logisch.game.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerQuitEvent e) {

        e.quitMessage(Component.empty());
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(Component.text("§c§lQUIT §8» §7" + e.getPlayer().getName()));
        }

    }

}
