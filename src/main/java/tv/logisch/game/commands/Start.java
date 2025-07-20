package tv.logisch.game.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import tv.logisch.game.GetDown;
import tv.logisch.game.enums.GameState;
import tv.logisch.game.manager.GameManager;

public class Start implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if(!sender.hasPermission("logisch.getdown.admin") && !GameManager.get().isHost(sender.getName())) {
            sender.sendMessage(GetDown.instance().prefix()+"§cYou do not have permission to use this command.");
            return true;
        }

        if(!GameManager.get().state().equals(GameState.WAITING)) {
            sender.sendMessage(GetDown.instance().prefix()+"§cThe game is already in progress or has ended.");
            return true;
        }
        GameManager.get().start();
        return true;
    }
}
