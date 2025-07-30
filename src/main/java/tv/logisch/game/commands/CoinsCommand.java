package tv.logisch.game.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;
import tv.logisch.game.GetDown;
import tv.logisch.game.manager.GameManager;

import java.util.Collection;
import java.util.List;

public class CoinsCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack ctx, String[] args) {
        CommandSender sender = ctx.getExecutor();

        if(!(sender instanceof org.bukkit.entity.Player p)) {
            if(sender == null) return;
            sender.sendMessage("§cThis command can only be used by players.");
            return;
        }

        if(!sender.hasPermission("logisch.getdown.admin") && !GameManager.get().isHost(p.getUniqueId())) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return;
        }

        if(args.length == 0) {
            int coins = GameManager.get().playerCoinManager().getCoins(p);
            p.sendMessage(GetDown.instance().prefix() + "Du hast §f" + coins + " §7Coins.");
            return;
        }

        if(args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null || !target.isOnline()) {
                p.sendMessage(GetDown.instance().prefix() + "§cDer Spieler wurde nicht gefunden.");
                return;
            }
            int coins = GameManager.get().playerCoinManager().getCoins(target);
            p.sendMessage(GetDown.instance().prefix() + "§f" + target.getName() + " §7hat §f" + coins + " §7Coins.");
            return;
        }

        if(args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null || !target.isOnline()) {
                p.sendMessage(GetDown.instance().prefix() + "§cDer Spieler wurde nicht gefunden.");
                return;
            }
            try {
                int coins = Integer.parseInt(args[1]);
                GameManager.get().playerCoinManager().setCoins(target, coins);
                p.sendMessage(GetDown.instance().prefix() + "§f" + target.getName() + " §7hat nun §f" + coins + " §7Coins.");
                return;
            } catch (NumberFormatException e) {
                p.sendMessage(GetDown.instance().prefix() + "§cBitte gib eine gültige Anzahl an Coins an.");
                return;
            }
        }
    }

    @Override
    public Collection<String> suggest(CommandSourceStack ctx, String[] args) {
        return List.of();
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("logisch.getdown.admin") || GameManager.get().isHost(sender.getName());
    }

    @Override
    public @Nullable String permission() {
        return "logisch.getdown.admin";
    }
}
