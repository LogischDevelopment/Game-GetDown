package tv.logisch.game.utils;

import org.bukkit.entity.Player;
import tv.logisch.game.GetDown;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class Velocity {

    public static void sendToVelocity(Player player, String message) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        try {
            out.write(message.getBytes());
            player.sendPluginMessage(GetDown.instance(), "logisch:actions", stream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
