package tv.logisch.game.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tv.logisch.game.GetDown;
import tv.logisch.game.gui.settings.EffectGUI;
import tv.logisch.game.manager.GameManager;

public class Settings implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!sender.hasPermission("logisch.getdown.admin") && !GameManager.get().isHost(sender.getName())) {
            sender.sendMessage(GetDown.instance().prefix()+"§cYou do not have permission to use this command.");
            return true;
        }

        EffectGUI.get((Player) sender).open();
        return true;
    }
}
