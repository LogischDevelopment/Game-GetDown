package tv.logisch.game.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;
import tv.logisch.game.GetDown;
import tv.logisch.game.enums.GameState;
import tv.logisch.game.manager.GameManager;

import java.util.Collection;
import java.util.List;

public class SkipCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack ctx, String[] args) {
        CommandSender sender = ctx.getExecutor();

        if(!(sender instanceof Player p)) {
            if(sender == null) return;
            sender.sendMessage(GetDown.instance().prefix()+"§cThis command can only be used by players.");
            return;
        }

        if(!p.hasPermission("logisch.getdown.admin") && !GameManager.get().isHost(p.getUniqueId())) {
            sender.sendMessage(GetDown.instance().prefix()+"§cYou do not have permission to use this command.");
            return;
        }

        if(!GameManager.get().state().equals(GameState.SHOPPING)) {
            sender.sendMessage(GetDown.instance().prefix()+"§cYou cannot skip this Phase.");
            return;
        }
        if(GameManager.get().shoppingTime() <= 10) {
            sender.sendMessage(GetDown.instance().prefix()+"§cYou cannot skip this Phase, because there are only "+GameManager.get().shoppingTime()+" seconds left.");
            return;
        }
        GameManager.get().shoppingTime(10);
        return;
    }

    @Override
    public Collection<String> suggest(CommandSourceStack ctx, String[] args) {
        return List.of();
    }

    @Override
    public boolean canUse(CommandSender sender) {
        if(!(sender instanceof Player p)) return false;
        return p.hasPermission("logisch.getdown.admin") || GameManager.get().isHost(p.getUniqueId());
    }

    @Override
    public @Nullable String permission() {
        return "logisch.getdown.admin";
    }
}
