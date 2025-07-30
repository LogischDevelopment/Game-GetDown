package tv.logisch.game.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import tv.logisch.game.GetDown;
import tv.logisch.game.enums.GameState;
import tv.logisch.game.gui.settings.EffectGUI;
import tv.logisch.game.gui.world.WorldGUI;
import tv.logisch.game.manager.GameManager;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class EventCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack ctx, String[] args) {
        CommandSender sender = ctx.getExecutor();
        if(!(sender instanceof Player p)) {
            if(sender == null) return;
            sender.sendMessage(GetDown.instance().prefix()+"§cThis command can only be used by players.");
            return;
        }

        if(!sender.hasPermission("logisch.getdown.admin") && !GameManager.get().isHost(sender.getName())) {
            sender.sendMessage(GetDown.instance().prefix()+"§cYou do not have permission to use this command.");
            return;
        }

        if(args.length == 0) {
            sender.sendMessage(GetDown.instance().prefix()+"§cUsage: /event <start|settings|worlds>");
            return;
        }

        String subCommand = args[0].toLowerCase();

        if(subCommand.equalsIgnoreCase("start")) {
            if(!GameManager.get().state().equals(GameState.WAITING)) {
                sender.sendMessage(GetDown.instance().prefix()+"§cThe game is already in progress or has ended.");
                return;
            }
            GameManager.get().start();
            return;
        }

        if(subCommand.equalsIgnoreCase("settings")) {
            EffectGUI.get(p).open();
            return;
        }

        if(subCommand.equalsIgnoreCase("worlds")) {
            if(!GameManager.get().state().equals(GameState.WAITING)) {
                sender.sendMessage(GetDown.instance().prefix() + "§cYou can only open the worlds menu when the game is waiting.");
                return;
            }
            WorldGUI.get(p).open();
            return;
        }

    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack ctx, String[] args) {
        if(args.length == 0) {
            return List.of("start", "settings", "worlds");
        }
        if(args.length == 1) {
            return Stream.of("start", "settings", "worlds").filter(s -> s.startsWith(args[0].toLowerCase())).toList();
        }
        return List.of();
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return sender.hasPermission("logisch.getdown.admin") || GameManager.get().isHost(sender.getName());
    }

    @Override
    public @Nullable String permission() {
        return "logisch.getdown.admin";
    }
}
