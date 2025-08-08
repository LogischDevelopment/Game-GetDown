package tv.logisch.game.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockBreakAnimator {

    public static void animateBreak(JavaPlugin plugin, Block block) {
        Location loc = block.getLocation();
        int entityId = loc.hashCode();

        for (int i = 0; i <= 9; i++) {
            final int stage = i;
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                sendBreakAnimation(loc, stage, entityId);
            }, i * 15L);
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            sendBreakAnimation(loc, -1, entityId);
            block.setType(Material.AIR);
            loc.getWorld().playSound(loc, Sound.BLOCK_STONE_BREAK, 1f, 1f);
        }, 7 * 20L);
    }

    private static void sendBreakAnimation(Location loc, int stage, int entityId) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
        packet.getIntegers().write(0, entityId);
        BlockPosition blockPosition = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        packet.getBlockPositionModifier().write(0, blockPosition);
        packet.getIntegers().write(1, stage);

        try {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                ProtocolLibrary.getProtocolManager().sendServerPacket(onlinePlayer, packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
