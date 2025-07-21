package tv.logisch.game.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tv.logisch.game.GetDown;
import tv.logisch.game.manager.GameManager;

public class Coins implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if(!sender.hasPermission("logisch.getdown.admin")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if(!(sender instanceof org.bukkit.entity.Player p)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return true;
        }

        if(args.length == 0) {
            int coins = GameManager.get().playerCoinManager().getCoins(p);
            p.sendMessage(GetDown.instance().prefix() + "Du hast §f" + coins + " §7Coins.");
            return true;
        }

        if(args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null || !target.isOnline()) {
                p.sendMessage(GetDown.instance().prefix() + "§cDer Spieler wurde nicht gefunden.");
                return true;
            }
            int coins = GameManager.get().playerCoinManager().getCoins(target);
            p.sendMessage(GetDown.instance().prefix() + "§f" + target.getName() + " §7hat §f" + coins + " §7Coins.");
            return true;
        }

        if(args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null || !target.isOnline()) {
                p.sendMessage(GetDown.instance().prefix() + "§cDer Spieler wurde nicht gefunden.");
                return true;
            }
            try {
                int coins = Integer.parseInt(args[1]);
                GameManager.get().playerCoinManager().setCoins(target, coins);
                p.sendMessage(GetDown.instance().prefix() + "§f" + target.getName() + " §7hat nun §f" + coins + " §7Coins.");
                return true;
            } catch (NumberFormatException e) {
                p.sendMessage(GetDown.instance().prefix() + "§cBitte gib eine gültige Anzahl an Coins an.");
                return true;
            }
        }

        return false;
    }
}
