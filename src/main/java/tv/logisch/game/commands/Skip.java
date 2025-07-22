package tv.logisch.game.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import tv.logisch.game.GetDown;
import tv.logisch.game.enums.GameState;
import tv.logisch.game.manager.GameManager;

public class Skip implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if(!sender.hasPermission("logisch.getdown.admin") && !GameManager.get().isHost(sender.getName())) {
            sender.sendMessage(GetDown.instance().prefix()+"§cYou do not have permission to use this command.");
            return true;
        }

        if(!GameManager.get().state().equals(GameState.SHOPPING)) {
            sender.sendMessage(GetDown.instance().prefix()+"§cYou cannot skip this Phase.");
            return true;
        }
        GameManager.get().shoppingTime(10);
        return true;
    }
}
